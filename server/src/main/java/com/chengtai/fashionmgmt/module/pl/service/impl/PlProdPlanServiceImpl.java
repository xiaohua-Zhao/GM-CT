package com.chengtai.fashionmgmt.module.pl.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.chengtai.fashionmgmt.common.AbstractBaseService;
import com.chengtai.fashionmgmt.common.BusinessLogJson;
import com.chengtai.fashionmgmt.common.BusinessValidationException;
import com.chengtai.fashionmgmt.common.BusinessValueUtils;
import com.chengtai.fashionmgmt.module.auth.context.LoginUserContext;
import com.chengtai.fashionmgmt.module.pl.entity.PlProdPlan;
import com.chengtai.fashionmgmt.module.pl.mapper.PlProdPlanMapper;
import com.chengtai.fashionmgmt.module.pl.mapper.PlTaskMapper;
import com.chengtai.fashionmgmt.module.pl.service.PlProdPlanService;
import com.chengtai.fashionmgmt.module.sys.service.CodeGeneratorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;

/** 生产计划业务服务，保存、修改和删除均使用自定义 SQL。 */
@Slf4j
@Service
@RequiredArgsConstructor
public class PlProdPlanServiceImpl extends AbstractBaseService<PlProdPlanMapper, PlProdPlan> implements PlProdPlanService {
    private final PlProdPlanMapper mapper;
    private final PlTaskMapper taskMapper;
    private final CodeGeneratorService codeGeneratorService;
    private final BusinessLogJson logJson;

    /**
     * 新增生产计划：自动生成计划号、校验订单唯一性并写入数据库。
     */
    @Override
    @Transactional
    public boolean create(PlProdPlan request) {
        log.info("[PL_PROD_PLAN][CREATE][IN] class={} method={} request={}",
                "PlProdPlanServiceImpl", "create", logJson.toJson(request));
        request.setId(null);
        normalize(request);
        if (!StringUtils.hasText(request.getProdPlanNo())) {
            request.setProdPlanNo(codeGeneratorService.generate("PROD_PLAN").getPreviewCode());
        }
        validate(request, true);
        if (mapper.countByPlanNo(request.getProdPlanNo(), null) > 0) {
            throw new BusinessValidationException("生产计划单号已存在：" + request.getProdPlanNo());
        }
        if (mapper.countByOrderId(request.getOrderId(), null) > 0) {
            throw new BusinessValidationException("该订单已经存在有效生产计划");
        }
        int affected = mapper.insertBusiness(request, currentUserId());
        log.info("[PL_PROD_PLAN][CREATE][OUT] class={} method={} affected={} id={} planNo={}",
                "PlProdPlanServiceImpl", "create", affected, request.getId(), request.getProdPlanNo());
        return affected == 1;
    }

    /**
     * 修改生产计划：保护计划单号和关联订单，重新校验日期和数量后更新。
     */
    @Override
    @Transactional
    public boolean update(PlProdPlan request) {
        log.info("[PL_PROD_PLAN][UPDATE][IN] class={} method={} id={} request={}",
                "PlProdPlanServiceImpl", "update", request.getId(), logJson.toJson(request));
        PlProdPlan existing = findById(request.getId());
        if (existing == null) {
            throw new BusinessValidationException("生产计划不存在");
        }
        String originalPlanNo = existing.getProdPlanNo();
        Long originalOrderId = existing.getOrderId();
        BusinessValueUtils.copyNonNullProperties(request, existing);
        existing.setProdPlanNo(originalPlanNo);
        existing.setOrderId(originalOrderId);
        normalize(existing);
        validate(existing, false);
        int affected = mapper.updateBusiness(existing);
        log.info("[PL_PROD_PLAN][UPDATE][OUT] class={} method={} affected={} id={} planNo={}",
                "PlProdPlanServiceImpl", "update", affected, existing.getId(), existing.getProdPlanNo());
        return affected == 1;
    }

    /**
     * 按当前分页和关键字查询未删除的生产计划。
     */
    @Override
    public IPage<PlProdPlan> page(long current, long size, String keyword, String keywordColumn) {
        QueryWrapper<PlProdPlan> query = new QueryWrapper<>();
        query.eq("is_del", 0);
        if (StringUtils.hasText(keyword)) {
            query.like(keywordColumn, keyword.trim());
        }
        query.orderByDesc("created_at");
        return page(new Page<>(current, size), query);
    }

    /**
     * 按主键查询未删除的生产计划。
     */
    @Override
    public PlProdPlan findById(Long id) {
        if (id == null) {
            return null;
        }
        return mapper.selectOne(new QueryWrapper<PlProdPlan>().eq("prod_plan_id", id).eq("is_del", 0));
    }

    /**
     * 逻辑删除生产计划；存在工序任务时禁止删除。
     */
    @Override
    @Transactional
    public boolean delete(Long id) {
        log.info("[PL_PROD_PLAN][DELETE][IN] class={} method={} id={}", "PlProdPlanServiceImpl", "delete", id);
        if (findById(id) == null) {
            return false;
        }
        if (taskMapper.countByPlanId(id) > 0) {
            throw new BusinessValidationException("该生产计划下已有工序任务，请先删除工序任务");
        }
        boolean success = mapper.logicalDelete(id) == 1;
        log.info("[PL_PROD_PLAN][DELETE][OUT] class={} method={} id={} success={}",
                "PlProdPlanServiceImpl", "delete", id, success);
        return success;
    }

    /**
     * 补齐生产计划默认状态和数量。
     */
    private void normalize(PlProdPlan entity) {
        entity.setProdQty(BusinessValueUtils.zero(entity.getProdQty()));
        if (entity.getStatus() == null) entity.setStatus(1);
    }

    /**
     * 校验生产计划的关联订单、开工完工日期、数量和计划单号。
     */
    private void validate(PlProdPlan entity, boolean creating) {
        if (creating && !StringUtils.hasText(entity.getProdPlanNo())) {
            throw new BusinessValidationException("生产计划单号不能为空");
        }
        if (entity.getOrderId() == null) {
            throw new BusinessValidationException("请选择订单");
        }
        if (!StringUtils.hasText(entity.getStartDate()) || !StringUtils.hasText(entity.getEndDate())) {
            throw new BusinessValidationException("请选择计划开工日和完工日");
        }
        if (entity.getEndDate().compareTo(entity.getStartDate()) < 0) {
            throw new BusinessValidationException("计划完工日不能早于开工日");
        }
        if (entity.getProdQty().compareTo(BigDecimal.ZERO) < 0) {
            throw new BusinessValidationException("生产数量不能小于0");
        }
    }

    /**
     * 获取当前登录用户 ID，用于记录生产计划创建人。
     */
    private Long currentUserId() {
        LoginUserContext.LoginUser user = LoginUserContext.get();
        return user == null ? null : user.id();
    }
}

package com.chengtai.fashionmgmt.module.pl.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.chengtai.fashionmgmt.common.AbstractBaseService;
import com.chengtai.fashionmgmt.common.BusinessLogJson;
import com.chengtai.fashionmgmt.common.BusinessValidationException;
import com.chengtai.fashionmgmt.common.BusinessValueUtils;
import com.chengtai.fashionmgmt.module.auth.context.LoginUserContext;
import com.chengtai.fashionmgmt.module.pl.entity.PlTask;
import com.chengtai.fashionmgmt.module.pl.mapper.PlProdPlanMapper;
import com.chengtai.fashionmgmt.module.pl.mapper.PlTaskMapper;
import com.chengtai.fashionmgmt.module.pl.service.PlTaskService;
import com.chengtai.fashionmgmt.module.sys.service.CodeGeneratorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.List;

/** 生产工序任务业务服务，保存、修改和删除均使用自定义 SQL。 */
@Slf4j
@Service
@RequiredArgsConstructor
public class PlTaskServiceImpl extends AbstractBaseService<PlTaskMapper, PlTask> implements PlTaskService {
    private final PlTaskMapper mapper;
    private final PlProdPlanMapper planMapper;
    private final CodeGeneratorService codeGeneratorService;
    private final BusinessLogJson logJson;

    /**
     * 新增工序任务：自动生成任务号、补齐质检和状态默认值并写入数据库。
     */
    @Override
    @Transactional
    public boolean create(PlTask request) {
        log.info("[PL_TASK][CREATE][IN] class={} method={} request={}", "PlTaskServiceImpl", "create", logJson.toJson(request));
        request.setId(null);
        normalize(request);
        if (!StringUtils.hasText(request.getTaskNo())) {
            request.setTaskNo(codeGeneratorService.generate("PROD_TASK").getPreviewCode());
        }
        validate(request, true);
        if (mapper.countByTaskNo(request.getTaskNo(), null) > 0) {
            throw new BusinessValidationException("工序任务号已存在：" + request.getTaskNo());
        }
        if (mapper.countByPlanStageDeptSeq(request.getProdPlanId(), request.getProcessStage(),
                request.getDeptId(), request.getSeqNo(), null) > 0) {
            throw new BusinessValidationException("同一计划、工序和执行组的拆分序号已存在");
        }
        int affected = mapper.insertBusiness(request, currentUserId());
        log.info("[PL_TASK][CREATE][OUT] class={} method={} affected={} id={} taskNo={}",
                "PlTaskServiceImpl", "create", affected, request.getId(), request.getTaskNo());
        return affected == 1;
    }

    /**
     * 修改工序任务：保护任务号和所属计划，重新校验工序和数量后更新。
     */
    @Override
    @Transactional
    public boolean update(PlTask request) {
        log.info("[PL_TASK][UPDATE][IN] class={} method={} id={} request={}",
                "PlTaskServiceImpl", "update", request.getId(), logJson.toJson(request));
        PlTask existing = findById(request.getId());
        if (existing == null) {
            throw new BusinessValidationException("工序任务不存在");
        }
        String originalTaskNo = existing.getTaskNo();
        Long originalPlanId = existing.getProdPlanId();
        BusinessValueUtils.copyNonNullProperties(request, existing);
        existing.setTaskNo(originalTaskNo);
        existing.setProdPlanId(originalPlanId);
        normalize(existing);
        validate(existing, false);
        if (mapper.countByPlanStageDeptSeq(existing.getProdPlanId(), existing.getProcessStage(),
                existing.getDeptId(), existing.getSeqNo(), existing.getId()) > 0) {
            throw new BusinessValidationException("同一计划、工序和执行组的拆分序号已存在");
        }
        int affected = mapper.updateBusiness(existing);
        log.info("[PL_TASK][UPDATE][OUT] class={} method={} affected={} id={} taskNo={}",
                "PlTaskServiceImpl", "update", affected, existing.getId(), existing.getTaskNo());
        return affected == 1;
    }

    /**
     * 按工序阶段分页查询未删除的工序任务。
     */
    @Override
    public IPage<PlTask> pageByStage(long current, long size, String keyword, String keywordColumn, Integer processStage) {
        QueryWrapper<PlTask> query = new QueryWrapper<>();
        query.eq("is_del", 0);
        if (processStage != null) {
            query.eq("process_stage", processStage);
        }
        if (StringUtils.hasText(keyword)) {
            query.like(keywordColumn, keyword.trim());
        }
        query.orderByDesc("created_at");
        return page(new Page<>(current, size), query);
    }

    /**
     * 分页查询全部工序任务。
     */
    @Override
    public IPage<PlTask> page(long current, long size, String keyword, String keywordColumn) {
        return pageByStage(current, size, keyword, keywordColumn, null);
    }

    /**
     * 按主键查询未删除的工序任务。
     */
    @Override
    public PlTask findById(Long id) {
        if (id == null) {
            return null;
        }
        return mapper.selectOne(new QueryWrapper<PlTask>().eq("task_id", id).eq("is_del", 0));
    }

    /**
     * 按生产计划 ID 查询有效工序任务。
     */
    @Override
    public List<PlTask> listByPlanId(Long prodPlanId) {
        return mapper.selectList(new QueryWrapper<PlTask>()
                .eq("prod_plan_id", prodPlanId)
                .eq("is_del", 0)
                .orderByAsc("process_stage", "seq_no"));
    }

    /**
     * 逻辑删除工序任务。
     */
    @Override
    @Transactional
    public boolean delete(Long id) {
        boolean success = mapper.logicalDelete(id) == 1;
        log.info("[PL_TASK][DELETE][OUT] class={} method={} id={} success={}",
                "PlTaskServiceImpl", "delete", id, success);
        return success;
    }

    /**
     * 补齐工序任务的序号、状态、数量和质检默认值。
     */
    private void normalize(PlTask entity) {
        if (entity.getProcessStage() == null) entity.setProcessStage(1);
        if (entity.getProdPlanId() != null && (entity.getSeqNo() == null || entity.getSeqNo() <= 0)) {
            entity.setSeqNo(mapper.nextSeqNo(entity.getProdPlanId(), entity.getProcessStage(), entity.getDeptId()));
        }
        if (entity.getStatus() == null) entity.setStatus(1);
        entity.setQtyPlan(BusinessValueUtils.zero(entity.getQtyPlan()));
        entity.setQtyDone(BusinessValueUtils.zero(entity.getQtyDone()));
        if (entity.getQcNeed() == null) entity.setQcNeed(1);
        if (entity.getQcStatus() == null) entity.setQcStatus(0);
        if (entity.getQcType() == null) entity.setQcType(2);
        entity.setQcQtyCheck(BusinessValueUtils.zero(entity.getQcQtyCheck()));
        entity.setQcQtyPass(BusinessValueUtils.zero(entity.getQcQtyPass()));
        entity.setQcQtyReject(BusinessValueUtils.zero(entity.getQcQtyReject()));
    }

    /**
     * 校验工序任务所属计划、工序阶段、数量、质检查询和计划日期。
     */
    private void validate(PlTask entity, boolean creating) {
        if (creating && !StringUtils.hasText(entity.getTaskNo())) {
            throw new BusinessValidationException("工序任务号不能为空");
        }
        if (entity.getProdPlanId() == null) {
            throw new BusinessValidationException("请选择生产计划");
        }
        if (planMapper.selectById(entity.getProdPlanId()) == null) {
            throw new BusinessValidationException("生产计划不存在");
        }
        if (entity.getProcessStage() == null || entity.getProcessStage() < 1 || entity.getProcessStage() > 6) {
            throw new BusinessValidationException("工序阶段必须在裁剪、缝制、剪线、整烫、包装、质检之间");
        }
        if (entity.getQtyPlan().signum() < 0 || entity.getQtyDone().signum() < 0) {
            throw new BusinessValidationException("计划数量和完成数量不能小于0");
        }
        if (StringUtils.hasText(entity.getStartDatePlan()) && StringUtils.hasText(entity.getEndDatePlan())
                && entity.getEndDatePlan().compareTo(entity.getStartDatePlan()) < 0) {
            throw new BusinessValidationException("计划结束日期不能早于开始日期");
        }
    }

    /**
     * 获取当前登录用户 ID，用于记录工序任务创建人。
     */
    private Long currentUserId() {
        LoginUserContext.LoginUser user = LoginUserContext.get();
        return user == null ? null : user.id();
    }
}

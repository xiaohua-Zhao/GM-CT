package com.chengtai.fashionmgmt.module.ms.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.chengtai.fashionmgmt.common.AbstractBaseService;
import com.chengtai.fashionmgmt.common.BusinessLogJson;
import com.chengtai.fashionmgmt.common.BusinessValidationException;
import com.chengtai.fashionmgmt.common.BusinessValueUtils;
import com.chengtai.fashionmgmt.module.auth.context.LoginUserContext;
import com.chengtai.fashionmgmt.module.ms.entity.MsOrder;
import com.chengtai.fashionmgmt.module.ms.mapper.MsOrderItemMapper;
import com.chengtai.fashionmgmt.module.ms.mapper.MsOrderMapper;
import com.chengtai.fashionmgmt.module.ms.service.MsOrderService;
import com.chengtai.fashionmgmt.module.sys.service.CodeGeneratorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;

/** 其他订单业务服务，保存、修改和删除均使用自定义 SQL。 */
@Slf4j
@Service
@RequiredArgsConstructor
public class MsOrderServiceImpl extends AbstractBaseService<MsOrderMapper, MsOrder> implements MsOrderService {
    private final MsOrderMapper mapper;
    private final MsOrderItemMapper itemMapper;
    private final CodeGeneratorService codeGeneratorService;
    private final BusinessLogJson logJson;

    /**
     * 新增其他订单：自动生成单号、补齐默认值、校验业务规则并写入数据库。
     */
    @Override
    @Transactional
    public boolean create(MsOrder request) {
        log.info("[MS_ORDER][CREATE][IN] class={} method={} request={}", "MsOrderServiceImpl", "create", logJson.toJson(request));
        request.setId(null);
        normalize(request);
        if (!StringUtils.hasText(request.getMiscNo())) {
            request.setMiscNo(codeGeneratorService.generate("MISC").getPreviewCode());
        }
        validate(request, true);
        if (mapper.countByMiscNo(request.getMiscNo(), null) > 0) {
            throw new BusinessValidationException("其他单号已存在：" + request.getMiscNo());
        }
        int affected = mapper.insertBusiness(request, currentUserId());
        log.info("[MS_ORDER][CREATE][OUT] class={} method={} affected={} id={} miscNo={}",
                "MsOrderServiceImpl", "create", affected, request.getId(), request.getMiscNo());
        return affected == 1;
    }

    /**
     * 修改其他订单：读取原记录、保护单号、重新校验金额和日期后更新数据库。
     */
    @Override
    @Transactional
    public boolean update(MsOrder request) {
        log.info("[MS_ORDER][UPDATE][IN] class={} method={} id={} request={}",
                "MsOrderServiceImpl", "update", request.getId(), logJson.toJson(request));
        MsOrder existing = findById(request.getId());
        if (existing == null) {
            throw new BusinessValidationException("其他订单不存在");
        }
        String originalMiscNo = existing.getMiscNo();
        BusinessValueUtils.copyNonNullProperties(request, existing);
        existing.setMiscNo(originalMiscNo);
        normalize(existing);
        validate(existing, false);
        int affected = mapper.updateBusiness(existing);
        log.info("[MS_ORDER][UPDATE][OUT] class={} method={} affected={} id={} miscNo={}",
                "MsOrderServiceImpl", "update", affected, existing.getId(), existing.getMiscNo());
        return affected == 1;
    }

    /**
     * 按当前分页和关键字查询未删除的其他订单。
     */
    @Override
    public IPage<MsOrder> page(long current, long size, String keyword, String keywordColumn) {
        QueryWrapper<MsOrder> query = new QueryWrapper<>();
        query.eq("is_del", 0);
        if (StringUtils.hasText(keyword)) {
            query.like(keywordColumn, keyword.trim());
        }
        query.orderByDesc("created_at");
        return page(new Page<>(current, size), query);
    }

    /**
     * 按主键查询未删除的其他订单。
     */
    @Override
    public MsOrder findById(Long id) {
        if (id == null) {
            return null;
        }
        return mapper.selectOne(new QueryWrapper<MsOrder>().eq("ms_order_id", id).eq("is_del", 0));
    }

    /**
     * 逻辑删除其他订单及其全部明细。
     */
    @Override
    @Transactional
    public boolean delete(Long id) {
        log.info("[MS_ORDER][DELETE][IN] class={} method={} id={}", "MsOrderServiceImpl", "delete", id);
        if (findById(id) == null) {
            return false;
        }
        itemMapper.logicalDeleteByMsOrderId(id);
        boolean success = mapper.logicalDelete(id) == 1;
        log.info("[MS_ORDER][DELETE][OUT] class={} method={} id={} success={}", "MsOrderServiceImpl", "delete", id, success);
        return success;
    }

    /**
     * 补齐其他订单默认状态，并重新计算不含税金额与价税合计。
     */
    private void normalize(MsOrder entity) {
        if (entity.getOrderType() == null) entity.setOrderType(1);
        if (entity.getRefType() == null) entity.setRefType(0);
        entity.setAmount(BusinessValueUtils.money(entity.getAmount()));
        entity.setTaxRate(BusinessValueUtils.taxRate(entity.getTaxRate()));
        entity.setAmountTax(BusinessValueUtils.taxIncluded(entity.getAmount(), entity.getTaxRate()));
        if (entity.getStatus() == null) entity.setStatus(1);
        if (entity.getAttachCount() == null || entity.getAttachCount() < 0) entity.setAttachCount(0);
    }

    /**
     * 校验其他订单的必填字段、关联单号、金额和到期日期规则。
     */
    private void validate(MsOrder entity, boolean creating) {
        if (creating && !StringUtils.hasText(entity.getMiscNo())) {
            throw new BusinessValidationException("其他单号不能为空");
        }
        if (!StringUtils.hasText(entity.getTitle())) {
            throw new BusinessValidationException("请填写费用或采购事由");
        }
        if (!StringUtils.hasText(entity.getApplyDate())) {
            throw new BusinessValidationException("请选择申请日期");
        }
        if (entity.getAmount().compareTo(BigDecimal.ZERO) < 0) {
            throw new BusinessValidationException("金额不能小于0");
        }
        if (entity.getRefType() != null && entity.getRefType() != 0 && !StringUtils.hasText(entity.getRefNo())) {
            throw new BusinessValidationException("选择关联类型后必须填写关联单号");
        }
        if ((Integer.valueOf(4).equals(entity.getOrderType()) || Integer.valueOf(5).equals(entity.getOrderType()))
                && !StringUtils.hasText(entity.getNextDueDate())) {
            throw new BusinessValidationException("消防检查或年检认证必须填写下次到期日");
        }
    }

    /**
     * 获取当前登录用户 ID，用于记录其他订单创建人。
     */
    private Long currentUserId() {
        LoginUserContext.LoginUser user = LoginUserContext.get();
        return user == null ? null : user.id();
    }
}

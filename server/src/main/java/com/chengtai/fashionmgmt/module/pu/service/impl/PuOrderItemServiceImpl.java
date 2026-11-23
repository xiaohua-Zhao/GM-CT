package com.chengtai.fashionmgmt.module.pu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.chengtai.fashionmgmt.common.AbstractBaseService;
import com.chengtai.fashionmgmt.common.BusinessLogJson;
import com.chengtai.fashionmgmt.common.BusinessValidationException;
import com.chengtai.fashionmgmt.common.BusinessValueUtils;
import com.chengtai.fashionmgmt.module.auth.context.LoginUserContext;
import com.chengtai.fashionmgmt.module.pu.entity.PuOrder;
import com.chengtai.fashionmgmt.module.pu.entity.PuOrderItem;
import com.chengtai.fashionmgmt.module.pu.mapper.PuOrderItemMapper;
import com.chengtai.fashionmgmt.module.pu.mapper.PuOrderMapper;
import com.chengtai.fashionmgmt.module.pu.service.PuOrderItemService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

/** 采购明细业务服务，保存和修改均使用自定义 SQL。 */
@Slf4j
@Service
@RequiredArgsConstructor
public class PuOrderItemServiceImpl extends AbstractBaseService<PuOrderItemMapper, PuOrderItem> implements PuOrderItemService {
    private final PuOrderItemMapper mapper;
    private final PuOrderMapper orderMapper;
    private final BusinessLogJson logJson;

    /** 新增业务数据：记录入参日志、校验规则、计算业务字段并执行自定义新增SQL。 */
    @Override
    @Transactional
    public boolean create(PuOrderItem request) {
        log.info("[PU_ORDER_ITEM][CREATE][IN] class={} method={} request={}", "PuOrderItemServiceImpl", "create", logJson.toJson(request));
        request.setId(null);
        normalize(request);
        validate(request);
        int affected = mapper.insertBusiness(request, currentUserId());
        orderMapper.recalculateTotals(request.getPoId());
        log.info("[PU_ORDER_ITEM][CREATE][OUT] class={} method={} affected={} itemId={} poId={} amount={} response={}",
                "PuOrderItemServiceImpl", "create", affected, request.getId(), request.getPoId(), request.getAmount(), logJson.toJson(request));
        return affected == 1;
    }

    /** 修改业务数据：读取原记录、合并非空字段、保护不可变字段并执行自定义修改SQL。 */
    @Override
    @Transactional
    public boolean update(PuOrderItem request) {
        log.info("[PU_ORDER_ITEM][UPDATE][IN] class={} method={} itemId={} request={}", "PuOrderItemServiceImpl", "update", request.getId(), logJson.toJson(request));
        PuOrderItem existing = mapper.selectById(request.getId());
        if (existing == null) throw new BusinessValidationException("采购明细不存在");
        Long originalPoId = existing.getPoId();
        BusinessValueUtils.copyNonNullProperties(request, existing);
        existing.setPoId(originalPoId);
        normalize(existing);
        validate(existing);
        int affected = mapper.updateBusiness(existing);
        orderMapper.recalculateTotals(existing.getPoId());
        log.info("[PU_ORDER_ITEM][UPDATE][OUT] class={} method={} affected={} itemId={} poId={} amount={} response={}",
                "PuOrderItemServiceImpl", "update", affected, existing.getId(), existing.getPoId(), existing.getAmount(), logJson.toJson(existing));
        return affected == 1;
    }

    /**
     * 按采购订单 ID 查询采购明细列表。
     */
    @Override
    public List<PuOrderItem> listByPoId(Long poId) {
        return list(new LambdaQueryWrapper<PuOrderItem>().eq(PuOrderItem::getPoId, poId));
    }

    /** 填充默认值并重新计算当前模块的业务金额和汇总字段。 */
    private void normalize(PuOrderItem entity) {
        if (!StringUtils.hasText(entity.getPart())) entity.setPart("整件");
        if (!StringUtils.hasText(entity.getUnit())) entity.setUnit("米");
        entity.setQty(BusinessValueUtils.zero(entity.getQty()));
        entity.setPrice(BusinessValueUtils.zero(entity.getPrice()));
        entity.setAmount(BusinessValueUtils.multiply(entity.getQty(), entity.getPrice()));
        entity.setQtyReceived(BusinessValueUtils.zero(entity.getQtyReceived()));
    }

    /** 校验当前模块必填项、关联关系和业务数值范围。 */
    private void validate(PuOrderItem entity) {
        if (entity.getPoId() == null) throw new BusinessValidationException("采购单ID不能为空");
        PuOrder order = orderMapper.selectById(entity.getPoId());
        if (order == null) throw new BusinessValidationException("采购订单不存在");
        if (entity.getMaterialId() == null) throw new BusinessValidationException("请选择物料");
        if (entity.getQty().signum() < 0) throw new BusinessValidationException("采购数量不能小于0");
    }

    /**
     * 获取当前登录用户 ID；未登录时返回 null，供自定义 SQL 记录创建人。
     */
    private Long currentUserId() {
        LoginUserContext.LoginUser user = LoginUserContext.get();
        return user == null ? null : user.id();
    }
}

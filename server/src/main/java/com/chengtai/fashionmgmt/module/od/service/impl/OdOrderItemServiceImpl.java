package com.chengtai.fashionmgmt.module.od.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.chengtai.fashionmgmt.common.AbstractBaseService;
import com.chengtai.fashionmgmt.common.BusinessLogJson;
import com.chengtai.fashionmgmt.common.BusinessValidationException;
import com.chengtai.fashionmgmt.common.BusinessValueUtils;
import com.chengtai.fashionmgmt.module.auth.context.LoginUserContext;
import com.chengtai.fashionmgmt.module.od.entity.OdOrder;
import com.chengtai.fashionmgmt.module.od.entity.OdOrderItem;
import com.chengtai.fashionmgmt.module.od.mapper.OdOrderItemMapper;
import com.chengtai.fashionmgmt.module.od.mapper.OdOrderMapper;
import com.chengtai.fashionmgmt.module.od.service.OdOrderItemService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

/** 订单明细业务服务，保存和修改均使用自定义 SQL。 */
@Slf4j
@Service
@RequiredArgsConstructor
public class OdOrderItemServiceImpl extends AbstractBaseService<OdOrderItemMapper, OdOrderItem> implements OdOrderItemService {
    private final OdOrderItemMapper mapper;
    private final OdOrderMapper orderMapper;
    private final BusinessLogJson logJson;

    /** 新增业务数据：记录入参日志、校验规则、计算业务字段并执行自定义新增SQL。 */
    @Override
    @Transactional
    public boolean create(OdOrderItem request) {
        log.info("[OD_ORDER_ITEM][CREATE][IN] class={} method={} request={}", "OdOrderItemServiceImpl", "create", logJson.toJson(request));
        request.setId(null);
        normalize(request);
        validate(request, true);
        if (mapper.countByItemNo(request.getOrderId(), request.getItemNo(), null) > 0) {
            throw new BusinessValidationException("订单内行号已存在：" + request.getItemNo());
        }
        int affected = mapper.insertBusiness(request, currentUserId());
        orderMapper.recalculateTotals(request.getOrderId());
        log.info("[OD_ORDER_ITEM][CREATE][OUT] class={} method={} affected={} itemId={} orderId={} amount={} response={}",
                "OdOrderItemServiceImpl", "create", affected, request.getId(), request.getOrderId(), request.getAmount(), logJson.toJson(request));
        return affected == 1;
    }

    /** 修改业务数据：读取原记录、合并非空字段、保护不可变字段并执行自定义修改SQL。 */
    @Override
    @Transactional
    public boolean update(OdOrderItem request) {
        log.info("[OD_ORDER_ITEM][UPDATE][IN] class={} method={} itemId={} request={}", "OdOrderItemServiceImpl", "update", request.getId(), logJson.toJson(request));
        OdOrderItem existing = mapper.selectById(request.getId());
        if (existing == null) throw new BusinessValidationException("订单明细不存在");
        Long originalOrderId = existing.getOrderId();
        BusinessValueUtils.copyNonNullProperties(request, existing);
        existing.setOrderId(originalOrderId);
        normalize(existing);
        validate(existing, false);
        if (mapper.countByItemNo(existing.getOrderId(), existing.getItemNo(), existing.getId()) > 0) {
            throw new BusinessValidationException("订单内行号已存在：" + existing.getItemNo());
        }
        int affected = mapper.updateBusiness(existing);
        orderMapper.recalculateTotals(existing.getOrderId());
        log.info("[OD_ORDER_ITEM][UPDATE][OUT] class={} method={} affected={} itemId={} orderId={} amount={} response={}",
                "OdOrderItemServiceImpl", "update", affected, existing.getId(), existing.getOrderId(), existing.getAmount(), logJson.toJson(existing));
        return affected == 1;
    }

    /**
     * 按订单 ID 查询订单明细列表。
     */
    @Override
    public List<OdOrderItem> listByOrderId(Long orderId) {
        return list(new LambdaQueryWrapper<OdOrderItem>().eq(OdOrderItem::getOrderId, orderId).orderByAsc(OdOrderItem::getItemNo));
    }

    /** 填充默认值并重新计算当前模块的业务金额和汇总字段。 */
    private void normalize(OdOrderItem entity) {
        if (entity.getItemNo() == null) entity.setItemNo(1);
        entity.setQty(BusinessValueUtils.zero(entity.getQty()));
        entity.setUnitPrice(BusinessValueUtils.zero(entity.getUnitPrice()));
        entity.setAmount(BusinessValueUtils.multiply(entity.getQty(), entity.getUnitPrice()));
        entity.setTaxRate(BusinessValueUtils.taxRate(entity.getTaxRate()));
        entity.setQtyShipped(BusinessValueUtils.zero(entity.getQtyShipped()));
    }

    /** 校验当前模块必填项、关联关系和业务数值范围。 */
    private void validate(OdOrderItem entity, boolean creating) {
        if (entity.getOrderId() == null) throw new BusinessValidationException("订单ID不能为空");
        OdOrder order = orderMapper.selectById(entity.getOrderId());
        if (order == null) throw new BusinessValidationException("订单不存在");
        if (entity.getStyleId() == null) throw new BusinessValidationException("请选择款式");
        if (!StringUtils.hasText(entity.getStyleCode())) throw new BusinessValidationException("款号不能为空");
        if (entity.getQty().signum() < 0) throw new BusinessValidationException("数量不能小于0");
    }

    /**
     * 获取当前登录用户 ID；未登录时返回 null，供自定义 SQL 记录创建人。
     */
    private Long currentUserId() {
        LoginUserContext.LoginUser user = LoginUserContext.get();
        return user == null ? null : user.id();
    }
}

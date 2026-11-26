package com.chengtai.fashionmgmt.module.od.service.impl;

import com.chengtai.fashionmgmt.common.AbstractBaseService;
import com.chengtai.fashionmgmt.common.BusinessLogJson;
import com.chengtai.fashionmgmt.common.BusinessValidationException;
import com.chengtai.fashionmgmt.common.BusinessValueUtils;
import com.chengtai.fashionmgmt.module.auth.context.LoginUserContext;
import com.chengtai.fashionmgmt.module.od.entity.OdOrder;
import com.chengtai.fashionmgmt.module.od.mapper.OdOrderMapper;
import com.chengtai.fashionmgmt.module.od.service.OdOrderService;
import com.chengtai.fashionmgmt.module.sys.service.CodeGeneratorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;

/** 订单业务服务，保存和修改均使用自定义 SQL。 */
@Slf4j
@Service
@RequiredArgsConstructor
public class OdOrderServiceImpl extends AbstractBaseService<OdOrderMapper, OdOrder> implements OdOrderService {
    private final OdOrderMapper mapper;
    private final CodeGeneratorService codeGeneratorService;
    private final BusinessLogJson logJson;

    /** 新增业务数据：记录入参日志、校验规则、计算业务字段并执行自定义新增SQL。 */
    @Override
    @Transactional
    public boolean create(OdOrder request) {
        log.info("[OD_ORDER][CREATE][IN] class={} method={} request={}", "OdOrderServiceImpl", "create", logJson.toJson(request));
        request.setId(null);
        request.setStatus(1);
        request.setReviewResult(0);
        request.setIsProducible(0);
        request.setDepositAmount(java.math.BigDecimal.ZERO);
        request.setCostAct(java.math.BigDecimal.ZERO);
        normalize(request);
        if (!StringUtils.hasText(request.getOrderNo())) {
            request.setOrderNo(codeGeneratorService.generate("ORDER").getPreviewCode());
        }
        validate(request, true);
        if (mapper.countByOrderNo(request.getOrderNo(), null) > 0) {
            throw new BusinessValidationException("订单号已存在：" + request.getOrderNo());
        }
        int affected = mapper.insertBusiness(request, currentUserId());
        log.info("[OD_ORDER][CREATE][OUT] class={} method={} affected={} orderId={} orderNo={} response={}",
                "OdOrderServiceImpl", "create", affected, request.getId(), request.getOrderNo(), logJson.toJson(request));
        return affected == 1;
    }

    /** 修改业务数据：读取原记录、合并非空字段、保护不可变字段并执行自定义修改SQL。 */
    @Override
    @Transactional
    public boolean update(OdOrder request) {
        log.info("[OD_ORDER][UPDATE][IN] class={} method={} orderId={} request={}", "OdOrderServiceImpl", "update", request.getId(), logJson.toJson(request));
        OdOrder existing = mapper.selectById(request.getId());
        if (existing == null) throw new BusinessValidationException("订单不存在");
        if (!isEditableStatus(existing)) {
            throw new BusinessValidationException("订单已开始生产或已关闭，不允许修改");
        }
        String originalOrderNo = existing.getOrderNo();
        Integer originalStatus = existing.getStatus();
        Integer originalReviewResult = existing.getReviewResult();
        Long originalReviewBy = existing.getReviewBy();
        String originalReviewTime = existing.getReviewTime();
        Integer originalCloseReasonType = existing.getCloseReasonType();
        String originalCloseReason = existing.getCloseReason();
        Long originalCloseBy = existing.getCloseBy();
        String originalCloseTime = existing.getCloseTime();
        Integer originalProducible = existing.getIsProducible();
        String originalReleaseTime = existing.getReleaseTime();
        java.math.BigDecimal originalDeposit = existing.getDepositAmount();
        java.math.BigDecimal originalCostAct = existing.getCostAct();
        BusinessValueUtils.copyNonNullProperties(request, existing);
        existing.setOrderNo(originalOrderNo);
        existing.setStatus(originalStatus);
        existing.setReviewResult(originalReviewResult);
        existing.setReviewBy(originalReviewBy);
        existing.setReviewTime(originalReviewTime);
        existing.setCloseReasonType(originalCloseReasonType);
        existing.setCloseReason(originalCloseReason);
        existing.setCloseBy(originalCloseBy);
        existing.setCloseTime(originalCloseTime);
        existing.setIsProducible(originalProducible);
        existing.setReleaseTime(originalReleaseTime);
        existing.setDepositAmount(originalDeposit);
        existing.setCostAct(originalCostAct);
        normalize(existing);
        validate(existing, false);
        if (mapper.countByOrderNo(existing.getOrderNo(), existing.getId()) > 0) {
            throw new BusinessValidationException("订单号已存在：" + existing.getOrderNo());
        }
        int affected = mapper.updateBusiness(existing);
        log.info("[OD_ORDER][UPDATE][OUT] class={} method={} affected={} orderId={} orderNo={} response={}",
                "OdOrderServiceImpl", "update", affected, existing.getId(), existing.getOrderNo(), logJson.toJson(existing));
        return affected == 1;
    }

    /** 判断通用接口是否允许修改该订单。 */
    private boolean isEditableStatus(OdOrder order) {
        if (Integer.valueOf(1).equals(order.getStatus()) || Integer.valueOf(2).equals(order.getStatus())) return true;
        if (Integer.valueOf(7).equals(order.getStatus())) {
            Integer previous = mapper.selectPreviousStatus(order.getId());
            return previous == null || Integer.valueOf(1).equals(previous) || Integer.valueOf(2).equals(previous);
        }
        return false;
    }

    /** 填充默认值并重新计算当前模块的业务金额和汇总字段。 */
    private void normalize(OdOrder entity) {
        if (entity.getOrderType() == null) entity.setOrderType(1);
        if (!StringUtils.hasText(entity.getCurrency())) entity.setCurrency("CNY");
        entity.setQtyTotal(BusinessValueUtils.zero(entity.getQtyTotal()));
        entity.setAmountTotal(BusinessValueUtils.money(entity.getAmountTotal()));
        entity.setTaxRate(BusinessValueUtils.taxRate(entity.getTaxRate()));
        entity.setAmountTax(BusinessValueUtils.taxIncluded(entity.getAmountTotal(), entity.getTaxRate()));
        entity.setCostPlan(BusinessValueUtils.money(entity.getCostPlan()));
        entity.setCostAct(BusinessValueUtils.money(entity.getCostAct()));
        entity.setDepositAmount(BusinessValueUtils.money(entity.getDepositAmount()));
        if (entity.getIsProducible() == null) entity.setIsProducible(0);
        if (entity.getStatus() == null) entity.setStatus(1);
        if (entity.getDelayDays() == null) entity.setDelayDays(0);
        if (entity.getDepositAmount().compareTo(entity.getAmountTax()) > 0) {
            throw new BusinessValidationException("已收定金不能大于价税合计");
        }
    }

    /** 校验当前模块必填项、关联关系和业务数值范围。 */
    private void validate(OdOrder entity, boolean creating) {
        if (creating && !StringUtils.hasText(entity.getOrderNo())) throw new BusinessValidationException("订单号不能为空");
        if (entity.getUnitId() == null) throw new BusinessValidationException("请选择客户单位");
        if (!StringUtils.hasText(entity.getOrderDate())) throw new BusinessValidationException("请选择订单日期");
        if (!StringUtils.hasText(entity.getDeliveryDatePlan())) throw new BusinessValidationException("请选择预计交付日期");
        if (entity.getAmountTotal().compareTo(BigDecimal.ZERO) < 0) throw new BusinessValidationException("订单金额不能小于0");
    }

    /**
     * 获取当前登录用户 ID；未登录时返回 null，供自定义 SQL 记录创建人。
     */
    private Long currentUserId() {
        LoginUserContext.LoginUser user = LoginUserContext.get();
        return user == null ? null : user.id();
    }
}

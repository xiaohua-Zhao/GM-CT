package com.chengtai.fashionmgmt.module.fin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.chengtai.fashionmgmt.common.AbstractBaseService;
import com.chengtai.fashionmgmt.common.BusinessLogJson;
import com.chengtai.fashionmgmt.common.BusinessValidationException;
import com.chengtai.fashionmgmt.common.BusinessValueUtils;
import com.chengtai.fashionmgmt.module.auth.context.LoginUserContext;
import com.chengtai.fashionmgmt.module.fin.entity.FinInvoice;
import com.chengtai.fashionmgmt.module.fin.mapper.FinInvoiceMapper;
import com.chengtai.fashionmgmt.module.fin.service.FinInvoiceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

/** 发票业务服务，保存和修改均使用自定义 SQL。 */
@Slf4j
@Service
@RequiredArgsConstructor
public class FinInvoiceServiceImpl extends AbstractBaseService<FinInvoiceMapper, FinInvoice> implements FinInvoiceService {
    private final FinInvoiceMapper mapper;
    private final BusinessLogJson logJson;

    /** 新增业务数据：记录入参日志、校验规则、计算业务字段并执行自定义新增SQL。 */
    @Override
    @Transactional
    public boolean create(FinInvoice request) {
        log.info("[FIN_INVOICE][CREATE][IN] class={} method={} request={}", "FinInvoiceServiceImpl", "create", logJson.toJson(request));
        request.setId(null);
        normalize(request);
        validate(request, true);
        if (mapper.countByInvoiceNo(request.getInvoiceNo(), null) > 0) throw new BusinessValidationException("发票号码已存在");
        int affected = mapper.insertBusiness(request, currentUserId());
        log.info("[FIN_INVOICE][CREATE][OUT] class={} method={} affected={} invoiceId={} invoiceNo={} response={}",
                "FinInvoiceServiceImpl", "create", affected, request.getId(), request.getInvoiceNo(), logJson.toJson(request));
        return affected == 1;
    }

    /** 修改业务数据：读取原记录、合并非空字段、保护不可变字段并执行自定义修改SQL。 */
    @Override
    @Transactional
    public boolean update(FinInvoice request) {
        log.info("[FIN_INVOICE][UPDATE][IN] class={} method={} invoiceId={} request={}", "FinInvoiceServiceImpl", "update", request.getId(), logJson.toJson(request));
        FinInvoice existing = mapper.selectById(request.getId());
        if (existing == null) throw new BusinessValidationException("发票不存在");
        String invoiceNo = existing.getInvoiceNo();
        Long orderId = existing.getOrderId();
        String orderNo = existing.getOrderNo();
        Long poId = existing.getPoId();
        String poNo = existing.getPoNo();
        BusinessValueUtils.copyNonNullProperties(request, existing);
        existing.setInvoiceNo(invoiceNo);
        existing.setOrderId(orderId); existing.setOrderNo(orderNo); existing.setPoId(poId); existing.setPoNo(poNo);
        normalize(existing);
        validate(existing, false);
        int affected = mapper.updateBusiness(existing);
        log.info("[FIN_INVOICE][UPDATE][OUT] class={} method={} affected={} invoiceId={} invoiceNo={} response={}",
                "FinInvoiceServiceImpl", "update", affected, existing.getId(), existing.getInvoiceNo(), logJson.toJson(existing));
        return affected == 1;
    }

    /**
     * 按订单 ID 查询发票列表。
     */
    @Override
    public List<FinInvoice> listByOrderId(Long orderId) {
        return list(new LambdaQueryWrapper<FinInvoice>().eq(FinInvoice::getOrderId, orderId));
    }

    /**
     * 按采购订单 ID 查询发票列表。
     */
    @Override
    public List<FinInvoice> listByPurchaseOrderId(Long poId) {
        return list(new LambdaQueryWrapper<FinInvoice>().eq(FinInvoice::getPoId, poId));
    }

    /** 填充默认值并重新计算当前模块的业务金额和汇总字段。 */
    private void normalize(FinInvoice entity) {
        if (entity.getInvoiceType() == null) entity.setInvoiceType(1);
        if (entity.getInvoiceKind() == null) entity.setInvoiceKind(1);
        if (entity.getPartnerType() == null) entity.setPartnerType(1);
        if (entity.getStatus() == null) entity.setStatus(1);
        entity.setAmount(BusinessValueUtils.money(entity.getAmount()));
        entity.setTaxRate(BusinessValueUtils.zero(entity.getTaxRate()));
        entity.setTaxAmount(BusinessValueUtils.money(entity.getAmount().multiply(entity.getTaxRate())));
        entity.setTotalAmount(BusinessValueUtils.money(entity.getAmount().add(entity.getTaxAmount())));
    }

    /** 校验当前模块必填项、关联关系和业务数值范围。 */
    private void validate(FinInvoice entity, boolean creating) {
        if (creating && !StringUtils.hasText(entity.getInvoiceNo())) throw new BusinessValidationException("发票号码不能为空");
        if (!StringUtils.hasText(entity.getInvoiceTitle())) throw new BusinessValidationException("发票抬头不能为空");
        if (!StringUtils.hasText(entity.getInvoiceDate())) throw new BusinessValidationException("开票日期不能为空");
        if (entity.getAmount().signum() < 0) throw new BusinessValidationException("发票金额不能小于0");
    }

    /**
     * 获取当前登录用户 ID；未登录时返回 null，供自定义 SQL 记录创建人。
     */
    private Long currentUserId() {
        LoginUserContext.LoginUser user = LoginUserContext.get();
        return user == null ? null : user.id();
    }
}

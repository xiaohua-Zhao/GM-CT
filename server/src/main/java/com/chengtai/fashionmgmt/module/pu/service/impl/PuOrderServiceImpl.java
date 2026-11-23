package com.chengtai.fashionmgmt.module.pu.service.impl;

import com.chengtai.fashionmgmt.common.AbstractBaseService;
import com.chengtai.fashionmgmt.common.BusinessLogJson;
import com.chengtai.fashionmgmt.common.BusinessValidationException;
import com.chengtai.fashionmgmt.common.BusinessValueUtils;
import com.chengtai.fashionmgmt.module.auth.context.LoginUserContext;
import com.chengtai.fashionmgmt.module.pu.entity.PuOrder;
import com.chengtai.fashionmgmt.module.pu.mapper.PuOrderMapper;
import com.chengtai.fashionmgmt.module.pu.service.PuOrderService;
import com.chengtai.fashionmgmt.module.sys.service.CodeGeneratorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;

/** 采购订单业务服务，保存和修改均使用自定义 SQL。 */
@Slf4j
@Service
@RequiredArgsConstructor
public class PuOrderServiceImpl extends AbstractBaseService<PuOrderMapper, PuOrder> implements PuOrderService {
    private final PuOrderMapper mapper;
    private final CodeGeneratorService codeGeneratorService;
    private final BusinessLogJson logJson;

    /** 新增业务数据：记录入参日志、校验规则、计算业务字段并执行自定义新增SQL。 */
    @Override
    @Transactional
    public boolean create(PuOrder request) {
        log.info("[PU_ORDER][CREATE][IN] class={} method={} request={}", "PuOrderServiceImpl", "create", logJson.toJson(request));
        request.setId(null);
        normalize(request);
        if (!StringUtils.hasText(request.getPoNo())) request.setPoNo(codeGeneratorService.generate("PURCHASE").getPreviewCode());
        validate(request, true);
        if (mapper.countByPoNo(request.getPoNo(), null) > 0) throw new BusinessValidationException("采购单号已存在：" + request.getPoNo());
        int affected = mapper.insertBusiness(request, currentUserId());
        log.info("[PU_ORDER][CREATE][OUT] class={} method={} affected={} poId={} poNo={} response={}",
                "PuOrderServiceImpl", "create", affected, request.getId(), request.getPoNo(), logJson.toJson(request));
        return affected == 1;
    }

    /** 修改业务数据：读取原记录、合并非空字段、保护不可变字段并执行自定义修改SQL。 */
    @Override
    @Transactional
    public boolean update(PuOrder request) {
        log.info("[PU_ORDER][UPDATE][IN] class={} method={} poId={} request={}", "PuOrderServiceImpl", "update", request.getId(), logJson.toJson(request));
        PuOrder existing = mapper.selectById(request.getId());
        if (existing == null) throw new BusinessValidationException("采购订单不存在");
        String originalPoNo = existing.getPoNo();
        BusinessValueUtils.copyNonNullProperties(request, existing);
        existing.setPoNo(originalPoNo);
        normalize(existing);
        validate(existing, false);
        if (mapper.countByPoNo(existing.getPoNo(), existing.getId()) > 0) throw new BusinessValidationException("采购单号已存在：" + existing.getPoNo());
        int affected = mapper.updateBusiness(existing);
        log.info("[PU_ORDER][UPDATE][OUT] class={} method={} affected={} poId={} poNo={} response={}",
                "PuOrderServiceImpl", "update", affected, existing.getId(), existing.getPoNo(), logJson.toJson(existing));
        return affected == 1;
    }

    /** 填充默认值并重新计算当前模块的业务金额和汇总字段。 */
    private void normalize(PuOrder entity) {
        if (entity.getPoType() == null) entity.setPoType(1);
        entity.setQtyTotal(BusinessValueUtils.zero(entity.getQtyTotal()));
        entity.setAmount(BusinessValueUtils.money(entity.getAmount()));
        entity.setTaxRate(BusinessValueUtils.taxRate(entity.getTaxRate()));
        entity.setAmountTax(BusinessValueUtils.taxIncluded(entity.getAmount(), entity.getTaxRate()));
        entity.setQtyReceived(BusinessValueUtils.zero(entity.getQtyReceived()));
        if (entity.getStatus() == null) entity.setStatus(1);
    }

    /** 校验当前模块必填项、关联关系和业务数值范围。 */
    private void validate(PuOrder entity, boolean creating) {
        if (creating && !StringUtils.hasText(entity.getPoNo())) throw new BusinessValidationException("采购单号不能为空");
        if (entity.getSupplierId() == null) throw new BusinessValidationException("请选择供应商");
        if (!StringUtils.hasText(entity.getPoDate())) throw new BusinessValidationException("请选择采购日期");
        if (entity.getAmount().compareTo(BigDecimal.ZERO) < 0) throw new BusinessValidationException("采购金额不能小于0");
    }

    /**
     * 获取当前登录用户 ID；未登录时返回 null，供自定义 SQL 记录创建人。
     */
    private Long currentUserId() {
        LoginUserContext.LoginUser user = LoginUserContext.get();
        return user == null ? null : user.id();
    }
}

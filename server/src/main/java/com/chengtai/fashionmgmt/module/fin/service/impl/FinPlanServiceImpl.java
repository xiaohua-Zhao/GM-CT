package com.chengtai.fashionmgmt.module.fin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.chengtai.fashionmgmt.common.AbstractBaseService;
import com.chengtai.fashionmgmt.common.BusinessLogJson;
import com.chengtai.fashionmgmt.common.BusinessValidationException;
import com.chengtai.fashionmgmt.common.BusinessValueUtils;
import com.chengtai.fashionmgmt.module.auth.context.LoginUserContext;
import com.chengtai.fashionmgmt.module.fin.entity.FinPlan;
import com.chengtai.fashionmgmt.module.fin.mapper.FinPlanMapper;
import com.chengtai.fashionmgmt.module.fin.service.FinPlanService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

/** 收付款计划业务服务，保存和修改均使用自定义 SQL。 */
@Slf4j
@Service
@RequiredArgsConstructor
public class FinPlanServiceImpl extends AbstractBaseService<FinPlanMapper, FinPlan> implements FinPlanService {
    private final FinPlanMapper mapper;
    private final BusinessLogJson logJson;

    /** 新增业务数据：记录入参日志、校验规则、计算业务字段并执行自定义新增SQL。 */
    @Override
    @Transactional
    public boolean create(FinPlan request) {
        log.info("[FIN_PLAN][CREATE][IN] class={} method={} request={}", "FinPlanServiceImpl", "create", logJson.toJson(request));
        request.setId(null);
        normalize(request, true);
        validate(request, true);
        checkUnique(request, null);
        int affected = mapper.insertBusiness(request, currentUserId());
        log.info("[FIN_PLAN][CREATE][OUT] class={} method={} affected={} planId={} planNo={} response={}",
                "FinPlanServiceImpl", "create", affected, request.getId(), request.getPlanNo(), logJson.toJson(request));
        return affected == 1;
    }

    /** 修改业务数据：读取原记录、合并非空字段、保护不可变字段并执行自定义修改SQL。 */
    @Override
    @Transactional
    public boolean update(FinPlan request) {
        log.info("[FIN_PLAN][UPDATE][IN] class={} method={} planId={} request={}", "FinPlanServiceImpl", "update", request.getId(), logJson.toJson(request));
        FinPlan existing = mapper.selectById(request.getId());
        if (existing == null) throw new BusinessValidationException("收付款计划不存在");
        String originalPlanNo = existing.getPlanNo();
        Integer originalSourceType = existing.getSourceType();
        Long originalSourceId = existing.getSourceId();
        java.math.BigDecimal originalPaidAmount = existing.getPaidAmount();
        String originalPaidDate = existing.getPaidDate();
        BusinessValueUtils.copyNonNullProperties(request, existing);
        existing.setPlanNo(originalPlanNo);
        existing.setSourceType(originalSourceType);
        existing.setSourceId(originalSourceId);
        existing.setPaidAmount(originalPaidAmount);
        existing.setPaidDate(originalPaidDate);
        normalize(existing, false);
        validate(existing, false);
        checkUnique(existing, existing.getId());
        int affected = mapper.updateBusiness(existing);
        log.info("[FIN_PLAN][UPDATE][OUT] class={} method={} affected={} planId={} planNo={} response={}",
                "FinPlanServiceImpl", "update", affected, existing.getId(), existing.getPlanNo(), logJson.toJson(existing));
        return affected == 1;
    }

    @Override
    public List<FinPlan> listByOrderId(Long orderId) { return listBySource(1, orderId); }

    @Override
    public List<FinPlan> listByPurchaseOrderId(Long poId) { return listBySource(2, poId); }

    @Override
    public List<FinPlan> listBySource(Integer sourceType, Long sourceId) {
        return list(new LambdaQueryWrapper<FinPlan>().eq(FinPlan::getSourceType, sourceType).eq(FinPlan::getSourceId, sourceId).orderByAsc(FinPlan::getSeqNo));
    }

    /** 填充默认值并重新计算当前模块的业务金额和汇总字段。 */
    private void normalize(FinPlan entity, boolean creating) {
        if (entity.getDirection() == null) entity.setDirection(1);
        if (entity.getSourceType() == null) entity.setSourceType(1);
        if (entity.getPartnerType() == null) entity.setPartnerType(1);
        if (entity.getSeqNo() == null) entity.setSeqNo(1);
        if (entity.getNodeType() == null) entity.setNodeType(1);
        if (entity.getStatus() == null) entity.setStatus(0);
        entity.setPlanAmount(BusinessValueUtils.money(entity.getPlanAmount()));
        if (creating) entity.setPaidAmount(BusinessValueUtils.zero(entity.getPaidAmount()));
    }

    /** 校验当前模块必填项、关联关系和业务数值范围。 */
    private void validate(FinPlan entity, boolean creating) {
        if (!StringUtils.hasText(entity.getPlanNo())) throw new BusinessValidationException("计划单号不能为空");
        if (entity.getSourceId() == null) throw new BusinessValidationException("来源单据不能为空");
        if (!StringUtils.hasText(entity.getPlanDate())) throw new BusinessValidationException("计划日期不能为空");
        if (entity.getPlanAmount().signum() < 0) throw new BusinessValidationException("计划金额不能小于0");
    }

    private void checkUnique(FinPlan entity, Long excludeId) {
        if (mapper.countByPlanNo(entity.getPlanNo(), excludeId) > 0) throw new BusinessValidationException("计划单号已存在");
        if (mapper.countBySourceSeq(entity.getSourceType(), entity.getSourceId(), entity.getSeqNo(), excludeId) > 0) throw new BusinessValidationException("同一单据节点序号已存在");
    }

    private Long currentUserId() {
        LoginUserContext.LoginUser user = LoginUserContext.get();
        return user == null ? null : user.id();
    }
}

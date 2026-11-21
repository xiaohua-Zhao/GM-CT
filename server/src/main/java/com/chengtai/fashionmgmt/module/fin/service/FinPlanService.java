package com.chengtai.fashionmgmt.module.fin.service;

import com.chengtai.fashionmgmt.common.BaseService;
import com.chengtai.fashionmgmt.module.fin.entity.FinPlan;
import java.util.List;

/** 收款计划服务接口。 */
public interface FinPlanService extends BaseService<FinPlan> {
    List<FinPlan> listByOrderId(Long orderId);
    List<FinPlan> listByPurchaseOrderId(Long poId);
    List<FinPlan> listBySource(Integer sourceType, Long sourceId);
}

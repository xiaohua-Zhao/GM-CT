package com.chengtai.fashionmgmt.module.fin.service;

import com.chengtai.fashionmgmt.common.BaseService;
import com.chengtai.fashionmgmt.module.fin.entity.FinPlan;
import java.util.List;

/** 收款计划服务接口。 */
public interface FinPlanService extends BaseService<FinPlan> {
    /**
     * 按订单 ID 查询收付款计划列表。
     */
    List<FinPlan> listByOrderId(Long orderId);
    /**
     * 按采购订单 ID 查询收付款计划列表。
     */
    List<FinPlan> listByPurchaseOrderId(Long poId);
    /**
     * 按来源类型和来源单据 ID 查询收付款计划列表。
     */
    List<FinPlan> listBySource(Integer sourceType, Long sourceId);
}

package com.chengtai.fashionmgmt.module.od.dto;

import com.chengtai.fashionmgmt.module.fin.entity.FinPlan;
import com.chengtai.fashionmgmt.module.od.entity.OdOrder;
import com.chengtai.fashionmgmt.module.od.entity.OdOrderItem;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/** 订单详情聚合响应：头表、明细、收款节点、指标和流程进度。 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OdOrderDetailResponse {
    private OdOrder order;
    private List<OdOrderItem> items;
    private List<FinPlan> plans;
    private OdOrderMetrics metrics;
    private List<OdOrderProgressNode> progress;
}

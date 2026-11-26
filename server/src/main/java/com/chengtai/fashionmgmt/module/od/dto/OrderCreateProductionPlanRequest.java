package com.chengtai.fashionmgmt.module.od.dto;

import lombok.Data;

import java.math.BigDecimal;

/** 订单创建生产计划请求。 */
@Data
public class OrderCreateProductionPlanRequest {
    private Long processId;
    private BigDecimal planQty;
}

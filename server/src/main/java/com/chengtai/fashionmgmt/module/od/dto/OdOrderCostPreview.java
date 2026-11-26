package com.chengtai.fashionmgmt.module.od.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/** 按款式用料和工序工价核算的订单成本预览。 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OdOrderCostPreview {
    private BigDecimal materialCost;
    private BigDecimal laborCost;
    private BigDecimal totalCost;
}

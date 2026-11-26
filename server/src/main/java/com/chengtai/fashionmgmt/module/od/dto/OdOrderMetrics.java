package com.chengtai.fashionmgmt.module.od.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/** 订单毛利与可做性评审指标。 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OdOrderMetrics {
    private BigDecimal grossProfit;
    private BigDecimal grossMarginRate;
    private BigDecimal costCompareBase;
    private BigDecimal minGrossMargin;
    private boolean viable;
    private String warningLevel;
}

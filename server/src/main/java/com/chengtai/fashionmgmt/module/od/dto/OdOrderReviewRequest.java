package com.chengtai.fashionmgmt.module.od.dto;

import lombok.Data;

/** 订单可做性评审请求。 */
@Data
public class OdOrderReviewRequest {
    private Integer reviewResult;
    private Integer closeReasonType;
    private String closeReason;
}

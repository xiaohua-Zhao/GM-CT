package com.chengtai.fashionmgmt.module.od.dto;

import lombok.Data;

/** 订单关闭请求。 */
@Data
public class OdOrderCloseRequest {
    private Integer closeReasonType;
    private String closeReason;
}

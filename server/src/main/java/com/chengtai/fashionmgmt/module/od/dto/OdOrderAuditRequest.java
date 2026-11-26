package com.chengtai.fashionmgmt.module.od.dto;

import lombok.Data;

/** 订单审核请求。 */
@Data
public class OdOrderAuditRequest {
    private String auditResult;
    private String auditRemark;
}

package com.chengtai.fashionmgmt.module.sm.dto;

import lombok.Data;

/** 客户样衣确认请求。 */
@Data
public class SmSampleConfirmRequest {
    private Integer confirmResult;
    private String confirmDate;
    private String remark;
}

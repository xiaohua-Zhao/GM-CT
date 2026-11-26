package com.chengtai.fashionmgmt.module.od.dto;

import lombok.Data;

import java.util.List;

/** 订单批量生成样衣请求。 */
@Data
public class OrderCreateSamplesRequest {
    private List<Long> selectedItemIds;
}

package com.chengtai.fashionmgmt.module.od.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/** 订单流程进度节点。 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OdOrderProgressNode {
    private String name;
    private String state;
}

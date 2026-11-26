package com.chengtai.fashionmgmt.module.od.dto;

import com.chengtai.fashionmgmt.module.od.entity.OdOrder;
import com.chengtai.fashionmgmt.module.od.entity.OdOrderItem;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/** 订单头与明细一次性保存请求。 */
@Data
@EqualsAndHashCode(callSuper = true)
public class OdOrderSaveRequest extends OdOrder {
    private List<OdOrderItem> items;
}

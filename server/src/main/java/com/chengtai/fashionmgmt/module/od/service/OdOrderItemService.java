package com.chengtai.fashionmgmt.module.od.service;

import com.chengtai.fashionmgmt.common.BaseService;
import com.chengtai.fashionmgmt.module.od.entity.OdOrderItem;
import java.util.List;

/** 订单明细服务接口。 */
public interface OdOrderItemService extends BaseService<OdOrderItem> {
    List<OdOrderItem> listByOrderId(Long orderId);
}

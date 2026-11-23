package com.chengtai.fashionmgmt.module.od.service;

import com.chengtai.fashionmgmt.common.BaseService;
import com.chengtai.fashionmgmt.module.od.entity.OdOrderItem;
import java.util.List;

/** 订单明细服务接口。 */
public interface OdOrderItemService extends BaseService<OdOrderItem> {
    /**
     * 按订单 ID 查询订单明细列表。
     */
    List<OdOrderItem> listByOrderId(Long orderId);
}

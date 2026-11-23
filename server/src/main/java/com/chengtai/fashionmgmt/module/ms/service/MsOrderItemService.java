package com.chengtai.fashionmgmt.module.ms.service;

import com.chengtai.fashionmgmt.common.BaseService;
import com.chengtai.fashionmgmt.module.ms.entity.MsOrderItem;

import java.util.List;

/** 其他订单明细服务接口。 */
public interface MsOrderItemService extends BaseService<MsOrderItem> {
    /**
     * 按其他订单 ID 查询明细列表。
     */
    List<MsOrderItem> listByMsOrderId(Long msOrderId);
}

package com.chengtai.fashionmgmt.module.pu.service;

import com.chengtai.fashionmgmt.common.BaseService;
import com.chengtai.fashionmgmt.module.pu.entity.PuOrderItem;
import java.util.List;

/** 采购明细服务接口。 */
public interface PuOrderItemService extends BaseService<PuOrderItem> {
    List<PuOrderItem> listByPoId(Long poId);
}

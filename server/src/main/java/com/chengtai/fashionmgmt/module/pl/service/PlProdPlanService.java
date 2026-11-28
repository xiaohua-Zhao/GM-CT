package com.chengtai.fashionmgmt.module.pl.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.chengtai.fashionmgmt.common.BaseService;
import com.chengtai.fashionmgmt.module.od.entity.OdOrder;
import com.chengtai.fashionmgmt.module.pl.entity.PlProdPlan;

/** 生产计划服务接口。 */
public interface PlProdPlanService extends BaseService<PlProdPlan> {
    /** 分页查询评审通过、样衣已放行、待生产且未关闭、尚未创建生产计划的订单。 */
    IPage<OdOrder> pageEligibleOrders(long current, long size, String keyword, Long unitId,
                                      String orderDateStart, String orderDateEnd,
                                      String deliveryDateStart, String deliveryDateEnd);
}

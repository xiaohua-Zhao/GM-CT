package com.chengtai.fashionmgmt.module.od.controller;

import com.chengtai.fashionmgmt.common.BaseCrudController;
import com.chengtai.fashionmgmt.common.BaseService;
import com.chengtai.fashionmgmt.module.od.entity.OdOrder;
import com.chengtai.fashionmgmt.module.od.service.OdOrderService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 销售订单接口，调用链：Controller -> Service -> Mapper -> t_od_order。
 */
@Tag(name = "销售订单")
@RestController
@RequestMapping("/api/od/orders")
@RequiredArgsConstructor
public class OdOrderController extends BaseCrudController<OdOrder> {
    private final OdOrderService service;

    @Override
    protected BaseService<OdOrder> service() { return service; }

    @Override
    protected String keywordField() { return "order_no"; }

    @Override
    protected String moduleName() { return "销售订单"; }
}

package com.chengtai.fashionmgmt.module.pu.controller;

import com.chengtai.fashionmgmt.common.BaseCrudController;
import com.chengtai.fashionmgmt.common.BaseService;
import com.chengtai.fashionmgmt.module.pu.entity.PuOrder;
import com.chengtai.fashionmgmt.module.pu.service.PuOrderService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 采购订单接口，调用链：Controller -> Service -> Mapper -> t_pu_order。
 */
@Tag(name = "采购订单")
@RestController
@RequestMapping("/api/pu/orders")
@RequiredArgsConstructor
public class PuOrderController extends BaseCrudController<PuOrder> {
    private final PuOrderService service;

    @Override
    protected BaseService<PuOrder> service() { return service; }

    @Override
    protected String keywordField() { return "po_no"; }

    @Override
    protected String moduleName() { return "采购订单"; }
}

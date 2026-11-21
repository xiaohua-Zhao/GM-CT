package com.chengtai.fashionmgmt.module.od.controller;

import com.chengtai.fashionmgmt.common.BaseCrudController;
import com.chengtai.fashionmgmt.common.BusinessLogJson;
import com.chengtai.fashionmgmt.common.Result;
import com.chengtai.fashionmgmt.common.BaseService;
import com.chengtai.fashionmgmt.module.od.entity.OdOrderItem;
import com.chengtai.fashionmgmt.module.od.service.OdOrderItemService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 订单明细接口，调用链：Controller -> Service -> Mapper -> t_od_order_item。
 */
@Slf4j
@Tag(name = "订单明细")
@RestController
@RequestMapping("/api/od/order-items")
@RequiredArgsConstructor
public class OdOrderItemController extends BaseCrudController<OdOrderItem> {
    private final OdOrderItemService service;
    private final BusinessLogJson logJson;

    @Override
    protected BaseService<OdOrderItem> service() { return service; }

    @Override
    protected String keywordField() { return "style_code"; }

    @Override
    protected String moduleName() { return "订单明细"; }

    /** 按订单ID查询全部明细。 */
    /** 按销售订单查询明细，并记录订单ID和返回结果。 */
    @GetMapping("/by-order/{orderId}")
    public Result<java.util.List<OdOrderItem>> byOrder(@PathVariable Long orderId) {
        log.info("[CONTROLLER][订单明细][BY_ORDER][IN] class={} method={} orderId={}", getClass().getSimpleName(), "byOrder", orderId);
        java.util.List<OdOrderItem> result = service.listByOrderId(orderId);
        log.info("[CONTROLLER][订单明细][BY_ORDER][OUT] class={} method={} orderId={} count={} result={}", getClass().getSimpleName(), "byOrder", orderId, result.size(), logJson.toJson(result));
        return Result.success(result);
    }
}

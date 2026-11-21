package com.chengtai.fashionmgmt.module.pu.controller;

import com.chengtai.fashionmgmt.common.BaseCrudController;
import com.chengtai.fashionmgmt.common.BusinessLogJson;
import com.chengtai.fashionmgmt.common.Result;
import com.chengtai.fashionmgmt.common.BaseService;
import com.chengtai.fashionmgmt.module.pu.entity.PuOrderItem;
import com.chengtai.fashionmgmt.module.pu.service.PuOrderItemService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 采购明细接口，调用链：Controller -> Service -> Mapper -> t_pu_order_item。
 */
@Slf4j
@Tag(name = "采购明细")
@RestController
@RequestMapping("/api/pu/order-items")
@RequiredArgsConstructor
public class PuOrderItemController extends BaseCrudController<PuOrderItem> {
    private final PuOrderItemService service;
    private final BusinessLogJson logJson;

    @Override
    protected BaseService<PuOrderItem> service() { return service; }

    @Override
    protected String keywordField() { return "part"; }

    @Override
    protected String moduleName() { return "采购明细"; }

    /** 按采购订单查询明细，并记录采购单ID和返回结果。 */
    @GetMapping("/by-po/{poId}")
    public Result<java.util.List<PuOrderItem>> byPo(@PathVariable Long poId) {
        log.info("[CONTROLLER][采购明细][BY_PO][IN] class={} method={} poId={}", getClass().getSimpleName(), "byPo", poId);
        java.util.List<PuOrderItem> result = service.listByPoId(poId);
        log.info("[CONTROLLER][采购明细][BY_PO][OUT] class={} method={} poId={} count={} result={}", getClass().getSimpleName(), "byPo", poId, result.size(), logJson.toJson(result));
        return Result.success(result);
    }
}

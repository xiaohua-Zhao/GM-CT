package com.chengtai.fashionmgmt.module.ms.controller;

import com.chengtai.fashionmgmt.common.BaseCrudController;
import com.chengtai.fashionmgmt.common.BaseService;
import com.chengtai.fashionmgmt.common.Result;
import com.chengtai.fashionmgmt.module.ms.entity.MsOrderItem;
import com.chengtai.fashionmgmt.module.ms.service.MsOrderItemService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/** 其他订单明细接口，调用链：Controller -> Service -> Mapper -> ct_gm.t_ms_order_item。 */
@Tag(name = "其他订单明细")
@RestController
@RequestMapping("/api/ms/order-items")
@RequiredArgsConstructor
public class MsOrderItemController extends BaseCrudController<MsOrderItem> {
    private final MsOrderItemService service;

    /**
     * 返回其他订单明细模块的业务 Service。
     */
    @Override
    protected BaseService<MsOrderItem> service() {
        return service;
    }

    /**
     * 返回其他订单明细模糊查询使用的字段。
     */
    @Override
    protected String keywordField() {
        return "item_name";
    }

    /**
     * 返回其他订单明细模块名称，用于接口日志和 Swagger。
     */
    @Override
    protected String moduleName() {
        return "其他订单明细";
    }

    /**
     * 按其他订单 ID 查询明细列表。
     */
    @Operation(summary = "按其他订单查询明细")
    @GetMapping("/by-ms-order/{msOrderId}")
    public Result<List<MsOrderItem>> byMsOrder(@PathVariable Long msOrderId) {
        return Result.success(service.listByMsOrderId(msOrderId));
    }
}

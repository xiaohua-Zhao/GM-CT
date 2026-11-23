package com.chengtai.fashionmgmt.module.ms.controller;

import com.chengtai.fashionmgmt.common.BaseCrudController;
import com.chengtai.fashionmgmt.common.BaseService;
import com.chengtai.fashionmgmt.module.ms.entity.MsOrder;
import com.chengtai.fashionmgmt.module.ms.service.MsOrderService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** 其他订单接口，调用链：Controller -> Service -> Mapper -> ct_gm.t_ms_order。 */
@Tag(name = "其他订单")
@RestController
@RequestMapping("/api/ms/orders")
@RequiredArgsConstructor
public class MsOrderController extends BaseCrudController<MsOrder> {
    private final MsOrderService service;

    /**
     * 返回其他订单模块的业务 Service。
     */
    @Override
    protected BaseService<MsOrder> service() {
        return service;
    }

    /**
     * 返回其他订单列表模糊查询使用的字段。
     */
    @Override
    protected String keywordField() {
        return "misc_no";
    }

    /**
     * 返回其他订单模块名称，用于接口日志和 Swagger。
     */
    @Override
    protected String moduleName() {
        return "其他订单";
    }
}

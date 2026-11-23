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
 * 订单接口，调用链：Controller -> Service -> Mapper -> t_od_order。
 */
@Tag(name = "订单")
@RestController
@RequestMapping("/api/od/orders")
@RequiredArgsConstructor
public class OdOrderController extends BaseCrudController<OdOrder> {
    private final OdOrderService service;

    /**
     * 返回订单模块的业务 Service，供通用 CRUD 基类完成分层调用。
     */
    @Override
    protected BaseService<OdOrder> service() { return service; }

    /**
     * 返回订单列表模糊查询使用的数据库字段名。
     */
    @Override
    protected String keywordField() { return "order_no"; }

    /**
     * 返回“订单”模块名称，用于接口日志、Swagger 描述和错误提示。
     */
    @Override
    protected String moduleName() { return "订单"; }
}

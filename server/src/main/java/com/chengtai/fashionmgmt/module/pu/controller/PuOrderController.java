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

    /**
     * 返回采购订单模块的业务 Service，供通用 CRUD 基类完成分层调用。
     */
    @Override
    protected BaseService<PuOrder> service() { return service; }

    /**
     * 返回采购订单列表模糊查询使用的数据库字段名。
     */
    @Override
    protected String keywordField() { return "po_no"; }

    /**
     * 返回“采购订单”模块名称，用于接口日志、Swagger 描述和错误提示。
     */
    @Override
    protected String moduleName() { return "采购订单"; }
}

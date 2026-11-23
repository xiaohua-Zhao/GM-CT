package com.chengtai.fashionmgmt.module.st.controller;

import com.chengtai.fashionmgmt.common.BaseService;
import com.chengtai.fashionmgmt.common.BaseCrudController;
import com.chengtai.fashionmgmt.module.st.entity.StStock;
import com.chengtai.fashionmgmt.module.st.service.StStockService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** 库存管理接口。 */
@Tag(name = "库存管理")
@RestController
@RequestMapping("/api/st/stocks")
@RequiredArgsConstructor
public class StStockController extends BaseCrudController<StStock> {
    private final StStockService service;

    /**
     * 返回库存管理业务服务。
     */
    @Override
    protected BaseService<StStock> service() { return service; }

    /**
     * 返回库存管理列表模糊查询字段。
     */
    @Override
    protected String keywordField() { return "item_name"; }

    /**
     * 返回库存管理模块名称。
     */
    @Override
    protected String moduleName() { return "库存管理"; }
}

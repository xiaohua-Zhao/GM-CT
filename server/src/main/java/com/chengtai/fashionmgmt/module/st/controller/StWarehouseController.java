package com.chengtai.fashionmgmt.module.st.controller;

import com.chengtai.fashionmgmt.common.BaseService;
import com.chengtai.fashionmgmt.common.CodeAwareCrudController;
import com.chengtai.fashionmgmt.module.st.entity.StWarehouse;
import com.chengtai.fashionmgmt.module.st.service.StWarehouseService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** 仓库管理接口。 */
@Tag(name = "仓库管理")
@RestController
@RequestMapping("/api/st/warehouses")
@RequiredArgsConstructor
public class StWarehouseController extends CodeAwareCrudController<StWarehouse> {
    private final StWarehouseService service;

    /**
     * 返回仓库管理业务服务。
     */
    @Override
    protected BaseService<StWarehouse> service() { return service; }

    /**
     * 返回仓库管理列表模糊查询字段。
     */
    @Override
    protected String keywordField() { return "wh_name"; }

    /**
     * 返回仓库管理模块名称。
     */
    @Override
    protected String moduleName() { return "仓库管理"; }

    /**
     * 返回仓库管理自动编码业务类型。
     */
    @Override
    protected String codeBizType() { return "WAREHOUSE"; }

    /**
     * 读取仓库管理业务编码。
     */
    @Override
    protected String getBusinessCode(StWarehouse entity) { return entity.getWhCode(); }

    /**
     * 写回仓库管理业务编码。
     */
    @Override
    protected void setBusinessCode(StWarehouse entity, String code) { entity.setWhCode(code); }
}

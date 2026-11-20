package com.chengtai.fashionmgmt.module.sys.controller;

import com.chengtai.fashionmgmt.common.BaseCrudController;
import com.chengtai.fashionmgmt.common.BaseService;
import com.chengtai.fashionmgmt.module.sys.entity.SysSupplier;
import com.chengtai.fashionmgmt.module.sys.service.SysSupplierService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** 供应商管理接口，调用链：Controller -> Service -> Mapper -> t_sys_supplier。 */
@Tag(name = "供应商管理")
@RestController
@RequestMapping("/api/sys/suppliers")
@RequiredArgsConstructor
public class SysSupplierController extends BaseCrudController<SysSupplier> {
    private final SysSupplierService service;

    @Override
    protected BaseService<SysSupplier> service() { return service; }

    @Override
    protected String keywordField() { return "sup_name"; }

    @Override
    protected String moduleName() { return "供应商管理"; }
}

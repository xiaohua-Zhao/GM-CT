package com.chengtai.fashionmgmt.module.sys.controller;

import com.chengtai.fashionmgmt.common.BaseCrudController;
import com.chengtai.fashionmgmt.common.BaseService;
import com.chengtai.fashionmgmt.module.sys.entity.SysMaterial;
import com.chengtai.fashionmgmt.module.sys.service.SysMaterialService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** 物料管理接口，调用链：Controller -> Service -> Mapper -> t_sys_material。 */
@Tag(name = "物料管理")
@RestController
@RequestMapping("/api/sys/materials")
@RequiredArgsConstructor
public class SysMaterialController extends BaseCrudController<SysMaterial> {
    private final SysMaterialService service;

    @Override
    protected BaseService<SysMaterial> service() { return service; }

    @Override
    protected String keywordField() { return "mat_name"; }

    @Override
    protected String moduleName() { return "物料管理"; }
}

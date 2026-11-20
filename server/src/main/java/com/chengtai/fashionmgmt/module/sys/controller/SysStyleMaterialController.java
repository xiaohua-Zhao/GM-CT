package com.chengtai.fashionmgmt.module.sys.controller;

import com.chengtai.fashionmgmt.common.BaseCrudController;
import com.chengtai.fashionmgmt.common.BaseService;
import com.chengtai.fashionmgmt.module.sys.entity.SysStyleMaterial;
import com.chengtai.fashionmgmt.module.sys.service.SysStyleMaterialService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 款式用料接口，调用链：Controller -> Service -> Mapper -> t_sys_style_material。
 * 维护款式各部位的材料单耗、损耗率和主辅料标识。
 */
@Tag(name = "款式用料")
@RestController
@RequestMapping("/api/sys/style-materials")
@RequiredArgsConstructor
public class SysStyleMaterialController extends BaseCrudController<SysStyleMaterial> {
    private final SysStyleMaterialService service;

    @Override
    protected BaseService<SysStyleMaterial> service() { return service; }

    @Override
    protected String keywordField() { return "part"; }

    @Override
    protected String moduleName() { return "款式用料"; }
}

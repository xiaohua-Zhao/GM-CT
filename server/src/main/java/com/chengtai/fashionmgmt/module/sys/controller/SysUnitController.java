package com.chengtai.fashionmgmt.module.sys.controller;

import com.chengtai.fashionmgmt.common.CodeAwareCrudController;
import com.chengtai.fashionmgmt.common.BaseService;
import com.chengtai.fashionmgmt.module.sys.entity.SysUnit;
import com.chengtai.fashionmgmt.module.sys.service.SysUnitService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** 单位管理接口，调用链：Controller -> Service -> Mapper -> t_sys_unit。 */
@Tag(name = "单位管理")
@RestController
@RequestMapping("/api/sys/units")
@RequiredArgsConstructor
public class SysUnitController extends CodeAwareCrudController<SysUnit> {
    private final SysUnitService service;

    @Override
    protected String codeBizType() { return "UNIT"; }

    @Override
    protected String getBusinessCode(SysUnit entity) { return entity.getUnitCode(); }

    @Override
    protected void setBusinessCode(SysUnit entity, String code) { entity.setUnitCode(code); }

    @Override
    protected BaseService<SysUnit> service() { return service; }

    @Override
    protected String keywordField() { return "unit_name"; }

    @Override
    protected String moduleName() { return "单位管理"; }
}

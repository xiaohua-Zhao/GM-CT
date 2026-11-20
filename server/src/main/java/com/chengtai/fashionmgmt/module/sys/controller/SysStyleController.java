package com.chengtai.fashionmgmt.module.sys.controller;

import com.chengtai.fashionmgmt.common.BaseCrudController;
import com.chengtai.fashionmgmt.common.BaseService;
import com.chengtai.fashionmgmt.module.sys.entity.SysStyle;
import com.chengtai.fashionmgmt.module.sys.service.SysStyleService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** 款式管理接口，调用链：Controller -> Service -> Mapper -> t_sys_style。 */
@Tag(name = "款式管理")
@RestController
@RequestMapping("/api/sys/styles")
@RequiredArgsConstructor
public class SysStyleController extends BaseCrudController<SysStyle> {
    private final SysStyleService service;

    @Override
    protected BaseService<SysStyle> service() { return service; }

    @Override
    protected String keywordField() { return "style_name"; }

    @Override
    protected String moduleName() { return "款式管理"; }
}

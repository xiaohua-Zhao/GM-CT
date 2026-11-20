package com.chengtai.fashionmgmt.module.sys.controller;

import com.chengtai.fashionmgmt.common.BaseCrudController;
import com.chengtai.fashionmgmt.common.BaseService;
import com.chengtai.fashionmgmt.module.sys.entity.SysCommon;
import com.chengtai.fashionmgmt.module.sys.service.SysCommonService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** 公共信息接口，调用链：Controller -> Service -> Mapper -> t_sys_common。 */
@Tag(name = "公共信息")
@RestController
@RequestMapping("/api/sys/commons")
@RequiredArgsConstructor
public class SysCommonController extends BaseCrudController<SysCommon> {
    private final SysCommonService service;

    @Override
    protected BaseService<SysCommon> service() { return service; }

    @Override
    protected String keywordField() { return "dict_name"; }

    @Override
    protected String moduleName() { return "公共信息"; }
}

package com.chengtai.fashionmgmt.module.sys.controller;

import com.chengtai.fashionmgmt.common.BaseCrudController;
import com.chengtai.fashionmgmt.common.BaseService;
import com.chengtai.fashionmgmt.module.sys.entity.SysStyleProcess;
import com.chengtai.fashionmgmt.module.sys.service.SysStyleProcessService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 款式工序接口，调用链：Controller -> Service -> Mapper -> t_sys_style_process。
 * 维护款式对应的工序顺序、标准工时和计件单价。
 */
@Tag(name = "款式工序")
@RestController
@RequestMapping("/api/sys/style-processes")
@RequiredArgsConstructor
public class SysStyleProcessController extends BaseCrudController<SysStyleProcess> {
    private final SysStyleProcessService service;

    @Override
    protected BaseService<SysStyleProcess> service() { return service; }

    @Override
    protected String keywordField() { return "process_name"; }

    @Override
    protected String moduleName() { return "款式工序"; }
}

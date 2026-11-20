package com.chengtai.fashionmgmt.module.sys.controller;

import com.chengtai.fashionmgmt.common.BaseCrudController;
import com.chengtai.fashionmgmt.common.BaseService;
import com.chengtai.fashionmgmt.module.sys.entity.SysProcess;
import com.chengtai.fashionmgmt.module.sys.service.SysProcessService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** 工序管理接口，调用链：Controller -> Service -> Mapper -> t_sys_process。 */
@Tag(name = "工序管理")
@RestController
@RequestMapping("/api/sys/processes")
@RequiredArgsConstructor
public class SysProcessController extends BaseCrudController<SysProcess> {
    private final SysProcessService service;

    @Override
    protected BaseService<SysProcess> service() { return service; }

    @Override
    protected String keywordField() { return "process_name"; }

    @Override
    protected String moduleName() { return "工序管理"; }
}

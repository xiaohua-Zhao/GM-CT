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

    /**
     * 返回款式工序模块的业务 Service，供通用 CRUD 基类完成分层调用。
     */
    @Override
    protected BaseService<SysStyleProcess> service() { return service; }

    /**
     * 返回款式工序列表模糊查询使用的数据库字段名。
     */
    @Override
    protected String keywordField() { return "process_name"; }

    /**
     * 返回“款式工序”模块名称，用于接口日志、Swagger 描述和错误提示。
     */
    @Override
    protected String moduleName() { return "款式工序"; }
}

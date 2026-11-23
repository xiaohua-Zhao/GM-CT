package com.chengtai.fashionmgmt.module.sys.controller;

import com.chengtai.fashionmgmt.common.CodeAwareCrudController;
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
public class SysProcessController extends CodeAwareCrudController<SysProcess> {
    private final SysProcessService service;

    /**
     * 返回工序模块使用的自动编码业务类型。
     */
    @Override
    protected String codeBizType() { return "PROCESS"; }

    /**
     * 读取工序实体的业务编码，用于新增时判断是否需要自动生成。
     */
    @Override
    protected String getBusinessCode(SysProcess entity) { return entity.getProcessCode(); }

    /**
     * 将后台生成的业务编码写回工序实体。
     */
    @Override
    protected void setBusinessCode(SysProcess entity, String code) { entity.setProcessCode(code); }

    /**
     * 返回工序模块的业务 Service，供通用 CRUD 基类完成分层调用。
     */
    @Override
    protected BaseService<SysProcess> service() { return service; }

    /**
     * 返回工序列表模糊查询使用的数据库字段名。
     */
    @Override
    protected String keywordField() { return "process_name"; }

    /**
     * 返回“工序”模块名称，用于接口日志、Swagger 描述和错误提示。
     */
    @Override
    protected String moduleName() { return "工序管理"; }
}

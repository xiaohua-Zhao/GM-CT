package com.chengtai.fashionmgmt.module.sys.controller;

import com.chengtai.fashionmgmt.common.BaseCrudController;
import com.chengtai.fashionmgmt.common.BaseService;
import com.chengtai.fashionmgmt.module.sys.entity.SysAttach;
import com.chengtai.fashionmgmt.module.sys.service.SysAttachService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 附件管理接口，调用链：Controller -> Service -> Mapper -> t_sys_attach。
 * 集中维护业务单据的图片、合同、工艺单和质检报告等附件。
 */
@Tag(name = "附件管理")
@RestController
@RequestMapping("/api/sys/attachments")
@RequiredArgsConstructor
public class SysAttachController extends BaseCrudController<SysAttach> {
    private final SysAttachService service;

    /**
     * 返回附件模块的业务 Service，供通用 CRUD 基类完成分层调用。
     */
    @Override
    protected BaseService<SysAttach> service() { return service; }

    /**
     * 返回附件列表模糊查询使用的数据库字段名。
     */
    @Override
    protected String keywordField() { return "file_name"; }

    /**
     * 返回“附件”模块名称，用于接口日志、Swagger 描述和错误提示。
     */
    @Override
    protected String moduleName() { return "附件管理"; }
}

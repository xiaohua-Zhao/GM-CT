package com.chengtai.fashionmgmt.module.ct.controller;

import com.chengtai.fashionmgmt.common.BaseCrudController;
import com.chengtai.fashionmgmt.common.BaseService;
import com.chengtai.fashionmgmt.module.ct.entity.CtPermission;
import com.chengtai.fashionmgmt.module.ct.service.CtPermissionService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 权限管理接口，调用链：Controller -> Service -> Mapper -> t_ct_permission。
 * 按角色、部门或人员授予页面操作权限。
 */
@Tag(name = "权限管理")
@RestController
@RequestMapping("/api/ct/permissions")
@RequiredArgsConstructor
public class CtPermissionController extends BaseCrudController<CtPermission> {
    private final CtPermissionService service;

    /**
     * 返回权限模块的业务 Service，供通用 CRUD 基类完成分层调用。
     */
    @Override
    protected BaseService<CtPermission> service() { return service; }

    /**
     * 返回权限列表模糊查询使用的数据库字段名。
     */
    @Override
    protected String keywordField() { return "action_code"; }

    /**
     * 返回“权限”模块名称，用于接口日志、Swagger 描述和错误提示。
     */
    @Override
    protected String moduleName() { return "权限管理"; }
}

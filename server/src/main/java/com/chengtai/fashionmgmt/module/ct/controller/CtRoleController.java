package com.chengtai.fashionmgmt.module.ct.controller;

import com.chengtai.fashionmgmt.common.BaseCrudController;
import com.chengtai.fashionmgmt.common.BaseService;
import com.chengtai.fashionmgmt.module.ct.entity.CtRole;
import com.chengtai.fashionmgmt.module.ct.service.CtRoleService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** 角色管理接口，调用链：Controller -> Service -> Mapper -> t_ct_role。 */
@Tag(name = "角色管理")
@RestController
@RequestMapping("/api/ct/roles")
@RequiredArgsConstructor
public class CtRoleController extends BaseCrudController<CtRole> {
    private final CtRoleService service;

    /**
     * 返回角色模块的业务 Service，供通用 CRUD 基类完成分层调用。
     */
    @Override
    protected BaseService<CtRole> service() { return service; }

    /**
     * 返回角色列表模糊查询使用的数据库字段名。
     */
    @Override
    protected String keywordField() { return "role_name"; }

    /**
     * 返回“角色”模块名称，用于接口日志、Swagger 描述和错误提示。
     */
    @Override
    protected String moduleName() { return "角色管理"; }
}

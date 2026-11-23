package com.chengtai.fashionmgmt.module.ct.controller;

import com.chengtai.fashionmgmt.common.BaseCrudController;
import com.chengtai.fashionmgmt.common.BaseService;
import com.chengtai.fashionmgmt.module.ct.entity.CtDeptRole;
import com.chengtai.fashionmgmt.module.ct.service.CtDeptRoleService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 部门角色关系接口，调用链：Controller -> Service -> Mapper -> t_ct_dept_role。
 * 维护部门默认角色及生效日期。
 */
@Tag(name = "部门角色关系")
@RestController
@RequestMapping("/api/ct/dept-roles")
@RequiredArgsConstructor
public class CtDeptRoleController extends BaseCrudController<CtDeptRole> {
    private final CtDeptRoleService service;

    /**
     * 返回部门角色关联模块的业务 Service，供通用 CRUD 基类完成分层调用。
     */
    @Override
    protected BaseService<CtDeptRole> service() { return service; }

    /**
     * 返回部门角色关联列表模糊查询使用的数据库字段名。
     */
    @Override
    protected String keywordField() { return "remark"; }

    /**
     * 返回“部门角色关联”模块名称，用于接口日志、Swagger 描述和错误提示。
     */
    @Override
    protected String moduleName() { return "部门角色关系"; }
}

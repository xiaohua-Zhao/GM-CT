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

    @Override
    protected BaseService<CtDeptRole> service() { return service; }

    @Override
    protected String keywordField() { return "remark"; }

    @Override
    protected String moduleName() { return "部门角色关系"; }
}

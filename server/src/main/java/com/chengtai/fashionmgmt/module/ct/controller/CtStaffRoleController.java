package com.chengtai.fashionmgmt.module.ct.controller;

import com.chengtai.fashionmgmt.common.BaseCrudController;
import com.chengtai.fashionmgmt.common.BaseService;
import com.chengtai.fashionmgmt.module.ct.entity.CtStaffRole;
import com.chengtai.fashionmgmt.module.ct.service.CtStaffRoleService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 人员角色关系接口，调用链：Controller -> Service -> Mapper -> t_ct_staff_role。
 * 维护人员与角色的多角色授予关系。
 */
@Tag(name = "人员角色关系")
@RestController
@RequestMapping("/api/ct/staff-roles")
@RequiredArgsConstructor
public class CtStaffRoleController extends BaseCrudController<CtStaffRole> {
    private final CtStaffRoleService service;

    @Override
    protected BaseService<CtStaffRole> service() { return service; }

    @Override
    protected String keywordField() { return "remark"; }

    @Override
    protected String moduleName() { return "人员角色关系"; }
}

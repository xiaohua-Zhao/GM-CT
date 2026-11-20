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

    @Override
    protected BaseService<CtRole> service() { return service; }

    @Override
    protected String keywordField() { return "role_name"; }

    @Override
    protected String moduleName() { return "角色管理"; }
}

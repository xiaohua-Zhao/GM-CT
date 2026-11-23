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

    /**
     * 返回员工角色关联模块的业务 Service，供通用 CRUD 基类完成分层调用。
     */
    @Override
    protected BaseService<CtStaffRole> service() { return service; }

    /**
     * 返回员工角色关联列表模糊查询使用的数据库字段名。
     */
    @Override
    protected String keywordField() { return "remark"; }

    /**
     * 返回“员工角色关联”模块名称，用于接口日志、Swagger 描述和错误提示。
     */
    @Override
    protected String moduleName() { return "人员角色关系"; }
}

package com.chengtai.fashionmgmt.module.ct.controller;

import com.chengtai.fashionmgmt.common.BaseCrudController;
import com.chengtai.fashionmgmt.common.BaseService;
import com.chengtai.fashionmgmt.module.ct.entity.CtStaffDept;
import com.chengtai.fashionmgmt.module.ct.service.CtStaffDeptService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 人员部门关系接口，调用链：Controller -> Service -> Mapper -> t_ct_staff_dept。
 * 维护人员与部门的多对多关系、主部门和加入日期。
 */
@Tag(name = "人员部门关系")
@RestController
@RequestMapping("/api/ct/staff-depts")
@RequiredArgsConstructor
public class CtStaffDeptController extends BaseCrudController<CtStaffDept> {
    private final CtStaffDeptService service;

    @Override
    protected BaseService<CtStaffDept> service() { return service; }

    @Override
    protected String keywordField() { return "remark"; }

    @Override
    protected String moduleName() { return "人员部门关系"; }
}

package com.chengtai.fashionmgmt.module.ct.controller;

import com.chengtai.fashionmgmt.common.CodeAwareCrudController;
import com.chengtai.fashionmgmt.common.BaseService;
import com.chengtai.fashionmgmt.module.ct.entity.CtDept;
import com.chengtai.fashionmgmt.module.ct.service.CtDeptService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** 部门管理接口，调用链：Controller -> Service -> Mapper -> t_ct_dept。 */
@Tag(name = "部门管理")
@RestController
@RequestMapping("/api/ct/depts")
@RequiredArgsConstructor
public class CtDeptController extends CodeAwareCrudController<CtDept> {
    private final CtDeptService service;

    @Override
    protected String codeBizType() { return "DEPT"; }

    @Override
    protected String getBusinessCode(CtDept entity) { return entity.getDeptCode(); }

    @Override
    protected void setBusinessCode(CtDept entity, String code) { entity.setDeptCode(code); }

    @Override
    protected BaseService<CtDept> service() { return service; }

    @Override
    protected String keywordField() { return "dept_name"; }

    @Override
    protected String moduleName() { return "部门管理"; }
}

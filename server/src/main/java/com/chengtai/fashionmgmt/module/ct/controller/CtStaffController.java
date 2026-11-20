package com.chengtai.fashionmgmt.module.ct.controller;

import com.chengtai.fashionmgmt.common.BaseCrudController;
import com.chengtai.fashionmgmt.common.BaseService;
import com.chengtai.fashionmgmt.module.ct.entity.CtStaff;
import com.chengtai.fashionmgmt.module.ct.service.CtStaffService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** 人员管理接口，调用链：Controller -> Service -> Mapper -> t_ct_staff。 */
@Tag(name = "人员管理")
@RestController
@RequestMapping("/api/ct/staffs")
@RequiredArgsConstructor
public class CtStaffController extends BaseCrudController<CtStaff> {
    private final CtStaffService service;

    @Override
    protected BaseService<CtStaff> service() { return service; }

    @Override
    protected String keywordField() { return "staff_name"; }

    @Override
    protected String moduleName() { return "人员管理"; }
}

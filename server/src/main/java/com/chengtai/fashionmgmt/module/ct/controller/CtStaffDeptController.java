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

    /**
     * 返回员工部门关联模块的业务 Service，供通用 CRUD 基类完成分层调用。
     */
    @Override
    protected BaseService<CtStaffDept> service() { return service; }

    /**
     * 返回员工部门关联列表模糊查询使用的数据库字段名。
     */
    @Override
    protected String keywordField() { return "remark"; }

    /**
     * 返回“员工部门关联”模块名称，用于接口日志、Swagger 描述和错误提示。
     */
    @Override
    protected String moduleName() { return "人员部门关系"; }
}

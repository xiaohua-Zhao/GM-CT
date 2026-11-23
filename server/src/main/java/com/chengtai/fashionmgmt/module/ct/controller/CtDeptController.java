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

    /**
     * 返回部门模块使用的自动编码业务类型。
     */
    @Override
    protected String codeBizType() { return "DEPT"; }

    /**
     * 读取部门实体的业务编码，用于新增时判断是否需要自动生成。
     */
    @Override
    protected String getBusinessCode(CtDept entity) { return entity.getDeptCode(); }

    /**
     * 将后台生成的业务编码写回部门实体。
     */
    @Override
    protected void setBusinessCode(CtDept entity, String code) { entity.setDeptCode(code); }

    /**
     * 返回部门模块的业务 Service，供通用 CRUD 基类完成分层调用。
     */
    @Override
    protected BaseService<CtDept> service() { return service; }

    /**
     * 返回部门列表模糊查询使用的数据库字段名。
     */
    @Override
    protected String keywordField() { return "dept_name"; }

    /**
     * 返回“部门”模块名称，用于接口日志、Swagger 描述和错误提示。
     */
    @Override
    protected String moduleName() { return "部门管理"; }
}

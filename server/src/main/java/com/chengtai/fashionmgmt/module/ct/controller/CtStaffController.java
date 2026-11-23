package com.chengtai.fashionmgmt.module.ct.controller;

import com.chengtai.fashionmgmt.common.CodeAwareCrudController;
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
public class CtStaffController extends CodeAwareCrudController<CtStaff> {
    private final CtStaffService service;

    /**
     * 返回员工模块使用的自动编码业务类型。
     */
    @Override
    protected String codeBizType() { return "STAFF"; }

    /**
     * 读取员工实体的业务编码，用于新增时判断是否需要自动生成。
     */
    @Override
    protected String getBusinessCode(CtStaff entity) { return entity.getStaffCode(); }

    /**
     * 将后台生成的业务编码写回员工实体。
     */
    @Override
    protected void setBusinessCode(CtStaff entity, String code) { entity.setStaffCode(code); }

    /**
     * 返回员工模块的业务 Service，供通用 CRUD 基类完成分层调用。
     */
    @Override
    protected BaseService<CtStaff> service() { return service; }

    /**
     * 返回员工列表模糊查询使用的数据库字段名。
     */
    @Override
    protected String keywordField() { return "staff_name"; }

    /**
     * 返回“员工”模块名称，用于接口日志、Swagger 描述和错误提示。
     */
    @Override
    protected String moduleName() { return "人员管理"; }
}

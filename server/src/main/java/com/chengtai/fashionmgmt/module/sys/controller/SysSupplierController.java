package com.chengtai.fashionmgmt.module.sys.controller;

import com.chengtai.fashionmgmt.common.CodeAwareCrudController;
import com.chengtai.fashionmgmt.common.BaseService;
import com.chengtai.fashionmgmt.module.sys.entity.SysSupplier;
import com.chengtai.fashionmgmt.module.sys.service.SysSupplierService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** 供应商管理接口，调用链：Controller -> Service -> Mapper -> t_sys_supplier。 */
@Tag(name = "供应商管理")
@RestController
@RequestMapping("/api/sys/suppliers")
@RequiredArgsConstructor
public class SysSupplierController extends CodeAwareCrudController<SysSupplier> {
    private final SysSupplierService service;

    /**
     * 返回供应商模块使用的自动编码业务类型。
     */
    @Override
    protected String codeBizType() { return "SUPPLIER"; }

    /**
     * 读取供应商实体的业务编码，用于新增时判断是否需要自动生成。
     */
    @Override
    protected String getBusinessCode(SysSupplier entity) { return entity.getSupCode(); }

    /**
     * 将后台生成的业务编码写回供应商实体。
     */
    @Override
    protected void setBusinessCode(SysSupplier entity, String code) { entity.setSupCode(code); }

    /**
     * 返回供应商模块的业务 Service，供通用 CRUD 基类完成分层调用。
     */
    @Override
    protected BaseService<SysSupplier> service() { return service; }

    /**
     * 返回供应商列表模糊查询使用的数据库字段名。
     */
    @Override
    protected String keywordField() { return "sup_name"; }

    /**
     * 返回“供应商”模块名称，用于接口日志、Swagger 描述和错误提示。
     */
    @Override
    protected String moduleName() { return "供应商管理"; }
}

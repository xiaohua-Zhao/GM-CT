package com.chengtai.fashionmgmt.module.sys.controller;

import com.chengtai.fashionmgmt.common.CodeAwareCrudController;
import com.chengtai.fashionmgmt.common.BaseService;
import com.chengtai.fashionmgmt.module.sys.entity.SysUnit;
import com.chengtai.fashionmgmt.module.sys.service.SysUnitService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** 单位管理接口，调用链：Controller -> Service -> Mapper -> t_sys_unit。 */
@Tag(name = "单位管理")
@RestController
@RequestMapping("/api/sys/units")
@RequiredArgsConstructor
public class SysUnitController extends CodeAwareCrudController<SysUnit> {
    private final SysUnitService service;

    /**
     * 返回单位模块使用的自动编码业务类型。
     */
    @Override
    protected String codeBizType() { return "UNIT"; }

    /**
     * 读取单位实体的业务编码，用于新增时判断是否需要自动生成。
     */
    @Override
    protected String getBusinessCode(SysUnit entity) { return entity.getUnitCode(); }

    /**
     * 将后台生成的业务编码写回单位实体。
     */
    @Override
    protected void setBusinessCode(SysUnit entity, String code) { entity.setUnitCode(code); }

    /**
     * 返回单位模块的业务 Service，供通用 CRUD 基类完成分层调用。
     */
    @Override
    protected BaseService<SysUnit> service() { return service; }

    /**
     * 返回单位列表模糊查询使用的数据库字段名。
     */
    @Override
    protected String keywordField() { return "unit_name"; }

    /**
     * 返回“单位”模块名称，用于接口日志、Swagger 描述和错误提示。
     */
    @Override
    protected String moduleName() { return "单位管理"; }
}

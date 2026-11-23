package com.chengtai.fashionmgmt.module.sys.controller;

import com.chengtai.fashionmgmt.common.CodeAwareCrudController;
import com.chengtai.fashionmgmt.common.BaseService;
import com.chengtai.fashionmgmt.module.sys.entity.SysMaterial;
import com.chengtai.fashionmgmt.module.sys.service.SysMaterialService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** 物料管理接口，调用链：Controller -> Service -> Mapper -> t_sys_material。 */
@Tag(name = "物料管理")
@RestController
@RequestMapping("/api/sys/materials")
@RequiredArgsConstructor
public class SysMaterialController extends CodeAwareCrudController<SysMaterial> {
    private final SysMaterialService service;

    /**
     * 返回物料模块使用的自动编码业务类型。
     */
    @Override
    protected String codeBizType() { return "MATERIAL"; }

    /**
     * 读取物料实体的业务编码，用于新增时判断是否需要自动生成。
     */
    @Override
    protected String getBusinessCode(SysMaterial entity) { return entity.getMatCode(); }

    /**
     * 将后台生成的业务编码写回物料实体。
     */
    @Override
    protected void setBusinessCode(SysMaterial entity, String code) { entity.setMatCode(code); }

    /**
     * 返回物料模块的业务 Service，供通用 CRUD 基类完成分层调用。
     */
    @Override
    protected BaseService<SysMaterial> service() { return service; }

    /**
     * 返回物料列表模糊查询使用的数据库字段名。
     */
    @Override
    protected String keywordField() { return "mat_name"; }

    /**
     * 返回“物料”模块名称，用于接口日志、Swagger 描述和错误提示。
     */
    @Override
    protected String moduleName() { return "物料管理"; }
}

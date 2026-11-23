package com.chengtai.fashionmgmt.module.sys.controller;

import com.chengtai.fashionmgmt.common.BaseCrudController;
import com.chengtai.fashionmgmt.common.BaseService;
import com.chengtai.fashionmgmt.module.sys.entity.SysStyleMaterial;
import com.chengtai.fashionmgmt.module.sys.service.SysStyleMaterialService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 款式用料接口，调用链：Controller -> Service -> Mapper -> t_sys_style_material。
 * 维护款式各部位的材料单耗、损耗率和主辅料标识。
 */
@Tag(name = "款式用料")
@RestController
@RequestMapping("/api/sys/style-materials")
@RequiredArgsConstructor
public class SysStyleMaterialController extends BaseCrudController<SysStyleMaterial> {
    private final SysStyleMaterialService service;

    /**
     * 返回款式用料模块的业务 Service，供通用 CRUD 基类完成分层调用。
     */
    @Override
    protected BaseService<SysStyleMaterial> service() { return service; }

    /**
     * 返回款式用料列表模糊查询使用的数据库字段名。
     */
    @Override
    protected String keywordField() { return "part"; }

    /**
     * 返回“款式用料”模块名称，用于接口日志、Swagger 描述和错误提示。
     */
    @Override
    protected String moduleName() { return "款式用料"; }
}

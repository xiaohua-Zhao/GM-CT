package com.chengtai.fashionmgmt.module.sys.controller;

import com.chengtai.fashionmgmt.common.CodeAwareCrudController;
import com.chengtai.fashionmgmt.common.BaseService;
import com.chengtai.fashionmgmt.module.sys.entity.SysStyle;
import com.chengtai.fashionmgmt.module.sys.service.SysStyleService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** 款式管理接口，调用链：Controller -> Service -> Mapper -> t_sys_style。 */
@Tag(name = "款式管理")
@RestController
@RequestMapping("/api/sys/styles")
@RequiredArgsConstructor
public class SysStyleController extends CodeAwareCrudController<SysStyle> {
    private final SysStyleService service;

    /**
     * 返回款式模块使用的自动编码业务类型。
     */
    @Override
    protected String codeBizType() { return "STYLE"; }

    /**
     * 读取款式实体的业务编码，用于新增时判断是否需要自动生成。
     */
    @Override
    protected String getBusinessCode(SysStyle entity) { return entity.getStyleCode(); }

    /**
     * 将后台生成的业务编码写回款式实体。
     */
    @Override
    protected void setBusinessCode(SysStyle entity, String code) { entity.setStyleCode(code); }

    /**
     * 返回款式模块的业务 Service，供通用 CRUD 基类完成分层调用。
     */
    @Override
    protected BaseService<SysStyle> service() { return service; }

    /**
     * 返回款式列表模糊查询使用的数据库字段名。
     */
    @Override
    protected String keywordField() { return "style_name"; }

    /**
     * 返回“款式”模块名称，用于接口日志、Swagger 描述和错误提示。
     */
    @Override
    protected String moduleName() { return "款式管理"; }
}

package com.chengtai.fashionmgmt.module.ct.controller;

import com.chengtai.fashionmgmt.common.BaseCrudController;
import com.chengtai.fashionmgmt.common.BaseService;
import com.chengtai.fashionmgmt.module.ct.entity.CtPage;
import com.chengtai.fashionmgmt.module.ct.service.CtPageService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** 页面管理接口，调用链：Controller -> Service -> Mapper -> t_ct_page。 */
@Tag(name = "页面管理")
@RestController
@RequestMapping("/api/ct/pages")
@RequiredArgsConstructor
public class CtPageController extends BaseCrudController<CtPage> {
    private final CtPageService service;

    /**
     * 返回页面模块的业务 Service，供通用 CRUD 基类完成分层调用。
     */
    @Override
    protected BaseService<CtPage> service() { return service; }

    /**
     * 返回页面列表模糊查询使用的数据库字段名。
     */
    @Override
    protected String keywordField() { return "page_name"; }

    /**
     * 返回“页面”模块名称，用于接口日志、Swagger 描述和错误提示。
     */
    @Override
    protected String moduleName() { return "页面管理"; }
}

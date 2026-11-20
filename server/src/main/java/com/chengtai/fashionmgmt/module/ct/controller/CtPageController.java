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

    @Override
    protected BaseService<CtPage> service() { return service; }

    @Override
    protected String keywordField() { return "page_name"; }

    @Override
    protected String moduleName() { return "页面管理"; }
}

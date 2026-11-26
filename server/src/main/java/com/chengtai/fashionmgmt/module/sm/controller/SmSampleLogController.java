package com.chengtai.fashionmgmt.module.sm.controller;

import com.chengtai.fashionmgmt.common.BaseCrudController;
import com.chengtai.fashionmgmt.common.BaseService;
import com.chengtai.fashionmgmt.module.sm.entity.SmSampleLog;
import com.chengtai.fashionmgmt.module.sm.service.SmSampleLogService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** 样衣跟单记录接口。 */
@Tag(name = "样衣跟单记录")
@RestController
@RequestMapping("/api/sm/sample-logs")
@RequiredArgsConstructor
public class SmSampleLogController extends BaseCrudController<SmSampleLog> {
    private final SmSampleLogService service;
    /** 返回样衣跟单记录业务服务。 */
    @Override protected BaseService<SmSampleLog> service() { return service; }
    /** 返回样衣跟单记录模糊查询字段。 */
    @Override protected String keywordField() { return "content"; }
    /** 返回样衣跟单记录模块名称。 */
    @Override protected String moduleName() { return "样衣跟单记录"; }
}

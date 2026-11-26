package com.chengtai.fashionmgmt.module.sm.controller;

import com.chengtai.fashionmgmt.common.BaseCrudController;
import com.chengtai.fashionmgmt.common.BaseService;
import com.chengtai.fashionmgmt.module.sm.entity.SmSampleProcess;
import com.chengtai.fashionmgmt.module.sm.service.SmSampleProcessService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** 样衣工序接口。 */
@Tag(name = "样衣工序")
@RestController
@RequestMapping("/api/sm/sample-processes")
@RequiredArgsConstructor
public class SmSampleProcessController extends BaseCrudController<SmSampleProcess> {
    private final SmSampleProcessService service;
    /** 返回样衣工序业务服务。 */
    @Override protected BaseService<SmSampleProcess> service() { return service; }
    /** 返回样衣工序模糊查询字段。 */
    @Override protected String keywordField() { return "part_name"; }
    /** 返回样衣工序模块名称。 */
    @Override protected String moduleName() { return "样衣工序"; }
}

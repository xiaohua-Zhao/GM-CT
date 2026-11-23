package com.chengtai.fashionmgmt.module.wg.controller;

import com.chengtai.fashionmgmt.common.BaseService;
import com.chengtai.fashionmgmt.common.CodeAwareCrudController;
import com.chengtai.fashionmgmt.module.wg.entity.PrReport;
import com.chengtai.fashionmgmt.module.wg.service.PrReportService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** 报工流水接口。 */
@Tag(name = "报工流水")
@RestController
@RequestMapping("/api/wg/reports")
@RequiredArgsConstructor
public class PrReportController extends CodeAwareCrudController<PrReport> {
    private final PrReportService service;

    /**
     * 返回报工流水业务服务。
     */
    @Override
    protected BaseService<PrReport> service() { return service; }

    /**
     * 返回报工流水列表模糊查询字段。
     */
    @Override
    protected String keywordField() { return "report_no"; }

    /**
     * 返回报工流水模块名称。
     */
    @Override
    protected String moduleName() { return "报工流水"; }

    /**
     * 返回报工流水自动编码业务类型。
     */
    @Override
    protected String codeBizType() { return "PIECE_REPORT"; }

    /**
     * 读取报工流水业务编码。
     */
    @Override
    protected String getBusinessCode(PrReport entity) { return entity.getReportNo(); }

    /**
     * 写回报工流水业务编码。
     */
    @Override
    protected void setBusinessCode(PrReport entity, String code) { entity.setReportNo(code); }
}

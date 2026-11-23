package com.chengtai.fashionmgmt.module.wg.controller;

import com.chengtai.fashionmgmt.common.BaseService;
import com.chengtai.fashionmgmt.common.CodeAwareCrudController;
import com.chengtai.fashionmgmt.module.wg.entity.WgPaymentBatch;
import com.chengtai.fashionmgmt.module.wg.service.WgPaymentBatchService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** 工资发放批次接口。 */
@Tag(name = "工资发放批次")
@RestController
@RequestMapping("/api/wg/payment-batches")
@RequiredArgsConstructor
public class WgPaymentBatchController extends CodeAwareCrudController<WgPaymentBatch> {
    private final WgPaymentBatchService service;

    /**
     * 返回工资发放批次业务服务。
     */
    @Override
    protected BaseService<WgPaymentBatch> service() { return service; }

    /**
     * 返回工资发放批次列表模糊查询字段。
     */
    @Override
    protected String keywordField() { return "batch_no"; }

    /**
     * 返回工资发放批次模块名称。
     */
    @Override
    protected String moduleName() { return "工资发放批次"; }

    /**
     * 返回工资发放批次自动编码业务类型。
     */
    @Override
    protected String codeBizType() { return "WAGE_BATCH"; }

    /**
     * 读取工资发放批次业务编码。
     */
    @Override
    protected String getBusinessCode(WgPaymentBatch entity) { return entity.getBatchNo(); }

    /**
     * 写回工资发放批次业务编码。
     */
    @Override
    protected void setBusinessCode(WgPaymentBatch entity, String code) { entity.setBatchNo(code); }
}

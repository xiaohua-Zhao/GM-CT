package com.chengtai.fashionmgmt.module.wg.controller;

import com.chengtai.fashionmgmt.common.BaseService;
import com.chengtai.fashionmgmt.common.CodeAwareCrudController;
import com.chengtai.fashionmgmt.module.wg.entity.WgBill;
import com.chengtai.fashionmgmt.module.wg.service.WgBillService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** 工资单接口。 */
@Tag(name = "工资单")
@RestController
@RequestMapping("/api/wg/bills")
@RequiredArgsConstructor
public class WgBillController extends CodeAwareCrudController<WgBill> {
    private final WgBillService service;

    /**
     * 返回工资单业务服务。
     */
    @Override
    protected BaseService<WgBill> service() { return service; }

    /**
     * 返回工资单列表模糊查询字段。
     */
    @Override
    protected String keywordField() { return "bill_no"; }

    /**
     * 返回工资单模块名称。
     */
    @Override
    protected String moduleName() { return "工资单"; }

    /**
     * 返回工资单自动编码业务类型。
     */
    @Override
    protected String codeBizType() { return "WAGE_BILL"; }

    /**
     * 读取工资单业务编码。
     */
    @Override
    protected String getBusinessCode(WgBill entity) { return entity.getBillNo(); }

    /**
     * 写回工资单业务编码。
     */
    @Override
    protected void setBusinessCode(WgBill entity, String code) { entity.setBillNo(code); }
}

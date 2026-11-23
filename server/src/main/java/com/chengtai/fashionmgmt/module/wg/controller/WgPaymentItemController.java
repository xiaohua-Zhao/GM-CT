package com.chengtai.fashionmgmt.module.wg.controller;

import com.chengtai.fashionmgmt.common.BaseService;
import com.chengtai.fashionmgmt.common.BaseCrudController;
import com.chengtai.fashionmgmt.module.wg.entity.WgPaymentItem;
import com.chengtai.fashionmgmt.module.wg.service.WgPaymentItemService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** 工资发放明细接口。 */
@Tag(name = "工资发放明细")
@RestController
@RequestMapping("/api/wg/payment-items")
@RequiredArgsConstructor
public class WgPaymentItemController extends BaseCrudController<WgPaymentItem> {
    private final WgPaymentItemService service;

    /**
     * 返回工资发放明细业务服务。
     */
    @Override
    protected BaseService<WgPaymentItem> service() { return service; }

    /**
     * 返回工资发放明细列表模糊查询字段。
     */
    @Override
    protected String keywordField() { return "remark"; }

    /**
     * 返回工资发放明细模块名称。
     */
    @Override
    protected String moduleName() { return "工资发放明细"; }
}

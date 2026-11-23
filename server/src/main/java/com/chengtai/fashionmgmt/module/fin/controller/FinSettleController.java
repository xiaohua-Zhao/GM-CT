package com.chengtai.fashionmgmt.module.fin.controller;

import com.chengtai.fashionmgmt.common.BaseService;
import com.chengtai.fashionmgmt.common.CodeAwareCrudController;
import com.chengtai.fashionmgmt.module.fin.entity.FinSettle;
import com.chengtai.fashionmgmt.module.fin.service.FinSettleService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** 结算管理接口。 */
@Tag(name = "结算管理")
@RestController
@RequestMapping("/api/fin/settles")
@RequiredArgsConstructor
public class FinSettleController extends CodeAwareCrudController<FinSettle> {
    private final FinSettleService service;

    /**
     * 返回结算管理业务服务。
     */
    @Override
    protected BaseService<FinSettle> service() { return service; }

    /**
     * 返回结算管理列表模糊查询字段。
     */
    @Override
    protected String keywordField() { return "settle_no"; }

    /**
     * 返回结算管理模块名称。
     */
    @Override
    protected String moduleName() { return "结算管理"; }

    /**
     * 返回结算管理自动编码业务类型。
     */
    @Override
    protected String codeBizType() { return "FIN_SETTLE"; }

    /**
     * 读取结算管理业务编码。
     */
    @Override
    protected String getBusinessCode(FinSettle entity) { return entity.getSettleNo(); }

    /**
     * 写回结算管理业务编码。
     */
    @Override
    protected void setBusinessCode(FinSettle entity, String code) { entity.setSettleNo(code); }
}

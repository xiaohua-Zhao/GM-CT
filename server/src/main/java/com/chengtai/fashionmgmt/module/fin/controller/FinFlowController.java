package com.chengtai.fashionmgmt.module.fin.controller;

import com.chengtai.fashionmgmt.common.BaseService;
import com.chengtai.fashionmgmt.common.CodeAwareCrudController;
import com.chengtai.fashionmgmt.module.fin.entity.FinFlow;
import com.chengtai.fashionmgmt.module.fin.service.FinFlowService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** 资金流水接口。 */
@Tag(name = "资金流水")
@RestController
@RequestMapping("/api/fin/flows")
@RequiredArgsConstructor
public class FinFlowController extends CodeAwareCrudController<FinFlow> {
    private final FinFlowService service;

    /**
     * 返回资金流水业务服务。
     */
    @Override
    protected BaseService<FinFlow> service() { return service; }

    /**
     * 返回资金流水列表模糊查询字段。
     */
    @Override
    protected String keywordField() { return "flow_no"; }

    /**
     * 返回资金流水模块名称。
     */
    @Override
    protected String moduleName() { return "资金流水"; }

    /**
     * 返回资金流水自动编码业务类型。
     */
    @Override
    protected String codeBizType() { return "FIN_FLOW"; }

    /**
     * 读取资金流水业务编码。
     */
    @Override
    protected String getBusinessCode(FinFlow entity) { return entity.getFlowNo(); }

    /**
     * 写回资金流水业务编码。
     */
    @Override
    protected void setBusinessCode(FinFlow entity, String code) { entity.setFlowNo(code); }
}

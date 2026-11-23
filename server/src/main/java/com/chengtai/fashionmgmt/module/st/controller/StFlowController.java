package com.chengtai.fashionmgmt.module.st.controller;

import com.chengtai.fashionmgmt.common.BaseService;
import com.chengtai.fashionmgmt.common.CodeAwareCrudController;
import com.chengtai.fashionmgmt.module.st.entity.StFlow;
import com.chengtai.fashionmgmt.module.st.service.StFlowService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** 出入库流水接口。 */
@Tag(name = "出入库流水")
@RestController
@RequestMapping("/api/st/flows")
@RequiredArgsConstructor
public class StFlowController extends CodeAwareCrudController<StFlow> {
    private final StFlowService service;

    /**
     * 返回出入库流水业务服务。
     */
    @Override
    protected BaseService<StFlow> service() { return service; }

    /**
     * 返回出入库流水列表模糊查询字段。
     */
    @Override
    protected String keywordField() { return "item_name"; }

    /**
     * 返回出入库流水模块名称。
     */
    @Override
    protected String moduleName() { return "出入库流水"; }

    /**
     * 返回出入库流水自动编码业务类型。
     */
    @Override
    protected String codeBizType() { return "STOCK_FLOW"; }

    /**
     * 读取出入库流水业务编码。
     */
    @Override
    protected String getBusinessCode(StFlow entity) { return entity.getFlowNo(); }

    /**
     * 写回出入库流水业务编码。
     */
    @Override
    protected void setBusinessCode(StFlow entity, String code) { entity.setFlowNo(code); }
}

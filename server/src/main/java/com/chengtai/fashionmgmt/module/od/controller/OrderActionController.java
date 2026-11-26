package com.chengtai.fashionmgmt.module.od.controller;

import com.chengtai.fashionmgmt.common.Result;
import com.chengtai.fashionmgmt.module.od.dto.OdOrderAuditRequest;
import com.chengtai.fashionmgmt.module.od.dto.OdOrderCloseRequest;
import com.chengtai.fashionmgmt.module.od.dto.OrderCreateProductionPlanRequest;
import com.chengtai.fashionmgmt.module.od.dto.OrderCreateSamplesRequest;
import com.chengtai.fashionmgmt.module.od.entity.OdOrder;
import com.chengtai.fashionmgmt.module.od.service.OdOrderFlowService;
import com.chengtai.fashionmgmt.module.pl.entity.PlProdPlan;
import com.chengtai.fashionmgmt.module.sm.entity.SmSample;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/** 订单流程动作接口：审核、生成样衣、创建生产计划和关闭。 */
@Tag(name = "订单流程动作")
@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderActionController {
    private final OdOrderFlowService service;

    /** 审核订单，通过进入已审核，不通过进入待复议。 */
    @Operation(summary = "订单审核")
    @PutMapping("/{id}/audit")
    public Result<OdOrder> audit(@PathVariable Long id, @RequestBody OdOrderAuditRequest request) {
        return Result.success("审核完成", service.audit(id, request));
    }

    /** 按订单明细批量生成样衣。 */
    @Operation(summary = "制作样衣")
    @PostMapping("/{id}/create-samples")
    public Result<List<SmSample>> createSamples(@PathVariable Long id, @RequestBody OrderCreateSamplesRequest request) {
        return Result.success("样衣生成成功", service.createSamples(id, request));
    }

    /** 为待生产订单创建生产计划。 */
    @Operation(summary = "创建生产计划")
    @PostMapping("/{id}/create-production-plan")
    public Result<PlProdPlan> createProductionPlan(@PathVariable Long id,
                                                   @RequestBody OrderCreateProductionPlanRequest request) {
        return Result.success("生产计划创建成功", service.createProductionPlan(id, request));
    }

    /** 关闭订单。 */
    @Operation(summary = "关闭订单")
    @PutMapping("/{id}/close")
    public Result<OdOrder> close(@PathVariable Long id, @RequestBody OdOrderCloseRequest request) {
        return Result.success("订单已关闭", service.close(id, request));
    }
}

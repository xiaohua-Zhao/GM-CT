package com.chengtai.fashionmgmt.module.fin.controller;

import com.chengtai.fashionmgmt.common.BaseCrudController;
import com.chengtai.fashionmgmt.common.BaseService;
import com.chengtai.fashionmgmt.common.BusinessLogJson;
import com.chengtai.fashionmgmt.common.Result;
import com.chengtai.fashionmgmt.module.fin.entity.FinPlan;
import com.chengtai.fashionmgmt.module.fin.service.FinPlanService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/** 收款计划接口。 */
@Slf4j
@Tag(name="收款计划")
@RestController
@RequestMapping("/api/fin/plans")
@RequiredArgsConstructor
public class FinPlanController extends BaseCrudController<FinPlan> {
    private final FinPlanService service;
    private final BusinessLogJson logJson;
    @Override protected BaseService<FinPlan> service(){return service;}
    @Override protected String keywordField(){return "plan_no";}
    @Override protected String moduleName(){return "收款计划";}

    /** 按销售订单查询收付款计划。 */
    @GetMapping("/by-order/{orderId}")
    public Result<List<FinPlan>> byOrder(@PathVariable Long orderId){
        log.info("[CONTROLLER][付款计划][BY_ORDER][IN] orderId={}", orderId);
        List<FinPlan> result=service.listByOrderId(orderId);
        log.info("[CONTROLLER][付款计划][BY_ORDER][OUT] orderId={} count={} result={}",orderId,result.size(),logJson.toJson(result));
        return Result.success(result);
    }

    /** 按采购订单查询付款计划。 */
    @GetMapping("/by-purchase/{poId}")
    public Result<List<FinPlan>> byPurchase(@PathVariable Long poId){
        log.info("[CONTROLLER][付款计划][BY_PURCHASE][IN] poId={}",poId);
        List<FinPlan> result=service.listByPurchaseOrderId(poId);
        log.info("[CONTROLLER][付款计划][BY_PURCHASE][OUT] poId={} count={} result={}",poId,result.size(),logJson.toJson(result));
        return Result.success(result);
    }

    /** 按来源类型和来源ID查询收付款计划。 */
    @GetMapping("/by-source/{sourceType}/{sourceId}")
    public Result<List<FinPlan>> bySource(@PathVariable Integer sourceType,@PathVariable Long sourceId){
        log.info("[CONTROLLER][付款计划][BY_SOURCE][IN] sourceType={} sourceId={}",sourceType,sourceId);
        List<FinPlan> result=service.listBySource(sourceType,sourceId);
        log.info("[CONTROLLER][付款计划][BY_SOURCE][OUT] sourceType={} sourceId={} count={} result={}",sourceType,sourceId,result.size(),logJson.toJson(result));
        return Result.success(result);
    }
}

package com.chengtai.fashionmgmt.module.od.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.chengtai.fashionmgmt.common.BusinessLogJson;
import com.chengtai.fashionmgmt.common.Result;
import com.chengtai.fashionmgmt.module.od.dto.OdOrderCloseRequest;
import com.chengtai.fashionmgmt.module.od.dto.OdOrderCostPreview;
import com.chengtai.fashionmgmt.module.od.dto.OdOrderDetailResponse;
import com.chengtai.fashionmgmt.module.od.dto.OdOrderReviewRequest;
import com.chengtai.fashionmgmt.module.od.dto.OdOrderSaveRequest;
import com.chengtai.fashionmgmt.module.od.entity.OdOrder;
import com.chengtai.fashionmgmt.module.od.entity.OdOrderItem;
import com.chengtai.fashionmgmt.module.od.service.OdOrderFlowService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/** 货物订单流程接口。 */
@Slf4j
@RestController
@RequestMapping("/api/od/order")
@RequiredArgsConstructor
public class OdOrderFlowController {
    private final OdOrderFlowService service;
    private final BusinessLogJson logJson;

    /** 分页查询货物订单列表。 */
    @GetMapping("/list")
    public Result<IPage<OdOrder>> list(@RequestParam(defaultValue = "1") long current,
                                       @RequestParam(defaultValue = "10") long size,
                                       @RequestParam(required = false) String keyword,
                                       @RequestParam(required = false) Long unitId,
                                       @RequestParam(required = false) Long salesmanId,
                                       @RequestParam(required = false) Integer status,
                                       @RequestParam(required = false) String deliveryStart,
                                       @RequestParam(required = false) String deliveryEnd,
                                       @RequestParam(required = false) Long deptId) {
        log.info("[OD_ORDER_FLOW][LIST][IN] current={} size={} keyword={} unitId={} salesmanId={} status={} deliveryStart={} deliveryEnd={} deptId={}",
                current, size, keyword, unitId, salesmanId, status, deliveryStart, deliveryEnd, deptId);
        IPage<OdOrder> result = service.listOrders(current, size, keyword, unitId, salesmanId, status, deliveryStart, deliveryEnd, deptId);
        log.info("[OD_ORDER_FLOW][LIST][OUT] total={} result={}", result.getTotal(), logJson.toJson(result));
        return Result.success(result);
    }

    /** 按当前明细预览款式用料和用工成本。 */
    @PostMapping("/cost-preview")
    public Result<OdOrderCostPreview> previewCost(@RequestBody List<OdOrderItem> items) {
        log.info("[OD_ORDER_FLOW][COST_PREVIEW][IN] items={}", logJson.toJson(items));
        OdOrderCostPreview result = service.previewCost(items);
        log.info("[OD_ORDER_FLOW][COST_PREVIEW][OUT] result={}", logJson.toJson(result));
        return Result.success(result);
    }

    /** 查询订单详情聚合数据。 */
    @GetMapping("/{id}")
    public Result<OdOrderDetailResponse> detail(@PathVariable Long id) {
        log.info("[OD_ORDER_FLOW][DETAIL][IN] id={}", id);
        OdOrderDetailResponse result = service.detail(id);
        log.info("[OD_ORDER_FLOW][DETAIL][OUT] id={} result={}", id, logJson.toJson(result));
        return Result.success(result);
    }

    /** 新建订单及全部明细。 */
    @PostMapping
    public Result<OdOrder> create(@RequestBody OdOrderSaveRequest request) {
        log.info("[OD_ORDER_FLOW][CREATE][IN] request={}", logJson.toJson(request));
        OdOrder order = service.saveOrder(request, null);
        log.info("[OD_ORDER_FLOW][CREATE][OUT] id={} orderNo={}", order.getId(), order.getOrderNo());
        return Result.success("订单保存成功", order);
    }

    /** 编辑订单及全部明细。 */
    @PutMapping("/{id}")
    public Result<OdOrder> update(@PathVariable Long id, @RequestBody OdOrderSaveRequest request) {
        log.info("[OD_ORDER_FLOW][UPDATE][IN] id={} request={}", id, logJson.toJson(request));
        OdOrder order = service.saveOrder(request, id);
        log.info("[OD_ORDER_FLOW][UPDATE][OUT] id={} orderNo={}", order.getId(), order.getOrderNo());
        return Result.success("订单保存成功", order);
    }

    /** 提交订单可做性评审。 */
    @PostMapping("/{id}/review")
    public Result<OdOrder> review(@PathVariable Long id, @RequestBody OdOrderReviewRequest request) {
        log.info("[OD_ORDER_FLOW][REVIEW][IN] id={} request={}", id, logJson.toJson(request));
        OdOrder order = service.review(id, request);
        log.info("[OD_ORDER_FLOW][REVIEW][OUT] id={} reviewResult={} status={}", id, order.getReviewResult(), order.getStatus());
        return Result.success("评审完成", order);
    }

    /** 暂停订单。 */
    @PostMapping("/{id}/pause")
    public Result<OdOrder> pause(@PathVariable Long id) {
        log.info("[OD_ORDER_FLOW][PAUSE][IN] id={}", id);
        OdOrder order = service.pause(id);
        log.info("[OD_ORDER_FLOW][PAUSE][OUT] id={} status={}", id, order.getStatus());
        return Result.success("订单已暂停", order);
    }

    /** 恢复暂停订单。 */
    @PostMapping("/{id}/resume")
    public Result<OdOrder> resume(@PathVariable Long id) {
        log.info("[OD_ORDER_FLOW][RESUME][IN] id={}", id);
        OdOrder order = service.resume(id);
        log.info("[OD_ORDER_FLOW][RESUME][OUT] id={} status={}", id, order.getStatus());
        return Result.success("订单已恢复", order);
    }

    /** 按客户原因关闭订单。 */
    @PostMapping("/{id}/close")
    public Result<OdOrder> close(@PathVariable Long id, @RequestBody OdOrderCloseRequest request) {
        log.info("[OD_ORDER_FLOW][CLOSE][IN] id={} request={}", id, logJson.toJson(request));
        OdOrder order = service.close(id, request);
        log.info("[OD_ORDER_FLOW][CLOSE][OUT] id={} status={}", id, order.getStatus());
        return Result.success("订单已关闭", order);
    }
}

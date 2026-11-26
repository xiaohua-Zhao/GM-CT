package com.chengtai.fashionmgmt.module.sm.controller;

import com.chengtai.fashionmgmt.common.BaseService;
import com.chengtai.fashionmgmt.common.CodeAwareCrudController;
import com.chengtai.fashionmgmt.common.Result;
import com.chengtai.fashionmgmt.module.od.entity.OdOrder;
import com.chengtai.fashionmgmt.module.sm.dto.SmSampleConfirmRequest;
import com.chengtai.fashionmgmt.module.sm.entity.SmSample;
import com.chengtai.fashionmgmt.module.sm.entity.SmSampleLog;
import com.chengtai.fashionmgmt.module.sm.service.SmSampleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/** 样衣管理接口。 */
@Tag(name = "样衣管理")
@RestController
@RequestMapping("/api/sm/samples")
@RequiredArgsConstructor
public class SmSampleController extends CodeAwareCrudController<SmSample> {
    private final SmSampleService service;

    /** 返回样衣业务服务。 */
    @Override protected BaseService<SmSample> service() { return service; }
    /** 返回样衣模糊查询字段。 */
    @Override protected String keywordField() { return "sample_no"; }
    /** 返回样衣模块名称。 */
    @Override protected String moduleName() { return "样衣管理"; }
    /** 返回样衣自动编码业务类型。 */
    @Override protected String codeBizType() { return "SAMPLE"; }
    /** 读取样衣编号。 */
    @Override protected String getBusinessCode(SmSample entity) { return entity.getSampleNo(); }
    /** 写入样衣编号。 */
    @Override protected void setBusinessCode(SmSample entity, String code) { entity.setSampleNo(code); }

    /** 查询可制作样衣的订单。 */
    @Operation(summary = "查询可制作样衣订单")
    @GetMapping("/eligible-orders")
    public Result<List<OdOrder>> eligibleOrders(@RequestParam(required = false) String keyword) {
        return Result.success(service.eligibleOrders(keyword));
    }

    /** 提交客户确认。 */
    @Operation(summary = "提交客户确认")
    @PostMapping("/{id}/send")
    public Result<SmSample> send(@PathVariable Long id) {
        return Result.success("已提交客户确认", service.send(id));
    }

    /** 客户确认样衣。 */
    @Operation(summary = "客户确认样衣")
    @PostMapping("/{id}/confirm")
    public Result<SmSample> confirm(@PathVariable Long id, @RequestBody SmSampleConfirmRequest request) {
        return Result.success("样衣确认完成", service.confirm(id, request));
    }

    /** 查询样衣跟单记录。 */
    @Operation(summary = "查询样衣跟单记录")
    @GetMapping("/{id}/logs")
    public Result<List<SmSampleLog>> logs(@PathVariable Long id) {
        return Result.success(service.logs(id));
    }
}

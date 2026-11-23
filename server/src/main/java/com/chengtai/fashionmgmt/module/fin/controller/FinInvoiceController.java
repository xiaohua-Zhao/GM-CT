package com.chengtai.fashionmgmt.module.fin.controller;

import com.chengtai.fashionmgmt.common.BaseCrudController;
import com.chengtai.fashionmgmt.common.BaseService;
import com.chengtai.fashionmgmt.common.BusinessLogJson;
import com.chengtai.fashionmgmt.common.Result;
import com.chengtai.fashionmgmt.module.fin.entity.FinInvoice;
import com.chengtai.fashionmgmt.module.fin.service.FinInvoiceService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/** 发票信息接口。 */
@Slf4j
@Tag(name="发票信息")
@RestController
@RequestMapping("/api/fin/invoices")
@RequiredArgsConstructor
public class FinInvoiceController extends BaseCrudController<FinInvoice> {
    private final FinInvoiceService service;
    private final BusinessLogJson logJson;
    /**
     * 返回发票模块的业务 Service，供通用 CRUD 基类完成分层调用。
     */
    @Override protected BaseService<FinInvoice> service(){return service;}
    /**
     * 返回发票列表模糊查询使用的数据库字段名。
     */
    @Override protected String keywordField(){return "invoice_no";}
    /**
     * 返回“发票”模块名称，用于接口日志、Swagger 描述和错误提示。
     */
    @Override protected String moduleName(){return "发票信息";}

    /** 按订单查询关联发票。 */
    @GetMapping("/by-order/{orderId}")
    public Result<List<FinInvoice>> byOrder(@PathVariable Long orderId){
        log.info("[CONTROLLER][发票][BY_ORDER][IN] orderId={}",orderId);
        List<FinInvoice> result=service.listByOrderId(orderId);
        log.info("[CONTROLLER][发票][BY_ORDER][OUT] orderId={} count={} result={}",orderId,result.size(),logJson.toJson(result));
        return Result.success(result);
    }

    /** 按采购订单查询关联发票。 */
    @GetMapping("/by-purchase/{poId}")
    public Result<List<FinInvoice>> byPurchase(@PathVariable Long poId){
        log.info("[CONTROLLER][发票][BY_PURCHASE][IN] poId={}",poId);
        List<FinInvoice> result=service.listByPurchaseOrderId(poId);
        log.info("[CONTROLLER][发票][BY_PURCHASE][OUT] poId={} count={} result={}",poId,result.size(),logJson.toJson(result));
        return Result.success(result);
    }
}

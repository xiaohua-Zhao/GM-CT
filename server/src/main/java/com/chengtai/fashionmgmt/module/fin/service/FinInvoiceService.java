package com.chengtai.fashionmgmt.module.fin.service;

import com.chengtai.fashionmgmt.common.BaseService;
import com.chengtai.fashionmgmt.module.fin.entity.FinInvoice;
import java.util.List;

/** 发票信息服务接口。 */
public interface FinInvoiceService extends BaseService<FinInvoice> {
    /**
     * 按订单 ID 查询发票列表。
     */
    List<FinInvoice> listByOrderId(Long orderId);
    /**
     * 按采购订单 ID 查询发票列表。
     */
    List<FinInvoice> listByPurchaseOrderId(Long poId);
}

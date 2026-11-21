package com.chengtai.fashionmgmt.module.fin.service;

import com.chengtai.fashionmgmt.common.BaseService;
import com.chengtai.fashionmgmt.module.fin.entity.FinInvoice;
import java.util.List;

/** 发票信息服务接口。 */
public interface FinInvoiceService extends BaseService<FinInvoice> {
    List<FinInvoice> listByOrderId(Long orderId);
    List<FinInvoice> listByPurchaseOrderId(Long poId);
}

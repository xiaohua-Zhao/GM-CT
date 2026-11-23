package com.chengtai.fashionmgmt.module.fin.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.chengtai.fashionmgmt.common.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/** 发票信息，对应 t_fin_invoice。 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("ct_gm.t_fin_invoice")
public class FinInvoice extends BaseEntity {
    @TableId(value="fin_invoice_id",type=IdType.AUTO)
    private Long id;
    private Long deptId;
    private String invoiceNo;
    private Integer invoiceType;
    private Integer invoiceKind;
    private String invoiceTitle;
    private String taxNo;
    private Integer partnerType;
    private Long partnerId;
    private String partnerName;
    private Long orderId;
    private String orderNo;
    private Long poId;
    private String poNo;
    private Long finSettleId;
    private Long finFlowId;
    private BigDecimal amount;
    private BigDecimal taxRate;
    private BigDecimal taxAmount;
    private BigDecimal totalAmount;
    private String invoiceDate;
    private Integer status;
    private String remark;
}

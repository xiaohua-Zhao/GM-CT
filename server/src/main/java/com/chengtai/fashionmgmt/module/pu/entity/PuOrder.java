package com.chengtai.fashionmgmt.module.pu.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.chengtai.fashionmgmt.common.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.math.BigDecimal;

/**
 * 采购订单实体，对应表 t_pu_order。
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_pu_order")
public class PuOrder extends BaseEntity {
    @TableId(value = "po_id", type = IdType.AUTO)
    private Long id;

    private Long deptId; // 归属部门ID
    private String poNo; // 采购单号
    private Long orderId; // 关联销售订单ID
    private Long prodPlanId; // 关联生产计划ID
    private Long supplierId; // 供应商ID
    private Integer poType; // 采购类型
    private String poDate; // 采购日期
    private String expectArriveDate; // 预计到货日期
    private BigDecimal qtyTotal; // 采购总数量
    private BigDecimal amount; // 采购金额
    private BigDecimal taxRate; // 税率
    private BigDecimal amountTax; // 价税合计
    private BigDecimal qtyReceived; // 已到货数量
    private Integer status; // 采购状态
    private String stockInNo; // 汇总入库单号
    private Long buyerId; // 采购员ID
    private String remark; // 备注
}

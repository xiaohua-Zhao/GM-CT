package com.chengtai.fashionmgmt.module.ms.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.chengtai.fashionmgmt.common.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/** 其他订单实体，对应表 ct_gm.t_ms_order。 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("ct_gm.t_ms_order")
public class MsOrder extends BaseEntity {
    @TableId(value = "ms_order_id", type = IdType.AUTO)
    private Long id;

    private String miscNo; // 其他单号
    private String title; // 事由
    private Integer orderType; // 单据类型
    private Integer refType; // 关联类型
    private String refNo; // 关联单号
    private Long unitId; // 往来单位
    private Long supplierId; // 供应商
    private Long applicantId; // 申请人
    private Long deptId; // 申请部门
    private String applyDate; // 申请日期
    private String contractNo; // 合同或发票号
    private BigDecimal amount; // 不含税金额
    private BigDecimal taxRate; // 税率
    private BigDecimal amountTax; // 价税合计
    private String payDatePlan; // 预计付款日期
    private String expectDate; // 预计完成日期
    private String finishDate; // 实际完成日期
    private String nextDueDate; // 下次到期日期
    private String certNo; // 证书或验收单号
    private Integer status; // 单据状态
    private Integer attachCount; // 附件数量
    private String remark; // 备注
}

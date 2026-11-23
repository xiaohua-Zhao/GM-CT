package com.chengtai.fashionmgmt.module.wg.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.chengtai.fashionmgmt.common.LogicEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.math.BigDecimal;

/** 工资发放批次实体，对应表 ct_gm.t_wg_payment_batch。 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("ct_gm.t_wg_payment_batch")
public class WgPaymentBatch extends LogicEntity {
    @TableId(value = "payment_batch_id", type = IdType.AUTO)
    private Long id;

    private Long deptId; // 归属部门
    private String batchNo; // 发放批次号
    private String period; // 工资月份
    private BigDecimal totalAmount; // 应发合计
    private Integer staffCount; // 人数
    private Integer status; // 状态
    private Integer paymentMethod; // 发放方式
    private Long laborSubmittedBy; // 劳资提交人
    private String laborSubmittedAt; // 劳资提交时间
    private Long financeConfirmedBy; // 财务核对人
    private String financeConfirmedAt; // 财务核对时间
    private Long approvedBy; // 批准人
    private String approvedAt; // 批准时间
    private Long paidBy; // 发放人
    private String paidAt; // 发放时间
    private String remark; // 备注
}

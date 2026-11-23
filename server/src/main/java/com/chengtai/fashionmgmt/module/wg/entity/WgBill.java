package com.chengtai.fashionmgmt.module.wg.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.chengtai.fashionmgmt.common.LogicEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.math.BigDecimal;

/** 工资单实体，对应表 ct_gm.t_wg_bill。 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("ct_gm.t_wg_bill")
public class WgBill extends LogicEntity {
    @TableId(value = "bill_id", type = IdType.AUTO)
    private Long id;

    private Long deptId; // 归属部门
    private String billNo; // 工资单号
    private String period; // 工资月份
    private Long staffId; // 员工
    private Integer staffCategory; // 人员类别
    private Integer wageType; // 计薪方式
    private BigDecimal qtyTotal; // 计件总数量
    private BigDecimal amountPiece; // 计件工资
    private BigDecimal amountTime; // 底薪/月薪
    private BigDecimal attendDays; // 出勤天数
    private BigDecimal bonus; // 奖励
    private BigDecimal deduct; // 扣款
    private BigDecimal amountPayable; // 应发金额
    private BigDecimal paidAmount; // 实发金额
    private String payDate; // 发放日期
    private Long leaderConfirmedBy; // 组长确认人
    private String leaderConfirmedAt; // 组长确认时间
    private Long laborReviewedBy; // 劳资审核人
    private String laborReviewedAt; // 劳资审核时间
    private Long submittedToFinanceBy; // 提交财务人
    private String submittedToFinanceAt; // 提交财务时间
    private Long financeReviewedBy; // 财务核对人
    private String financeReviewedAt; // 财务核对时间
    private Long approvedBy; // 老板批准人
    private String approvedAt; // 批准时间
    private Integer payMethod; // 发放方式
    private Long paymentBatchId; // 发放批次
    private Long paidBy; // 出纳
    private String paidAt; // 实际发放时间
    private Integer status; // 状态
    private String remark; // 备注
}

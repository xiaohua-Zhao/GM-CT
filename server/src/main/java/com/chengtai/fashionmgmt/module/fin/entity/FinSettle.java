package com.chengtai.fashionmgmt.module.fin.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.chengtai.fashionmgmt.common.LogicEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.math.BigDecimal;

/** 结算管理实体，对应表 ct_gm.t_fin_settle。 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("ct_gm.t_fin_settle")
public class FinSettle extends LogicEntity {
    @TableId(value = "fin_settle_id", type = IdType.AUTO)
    private Long id;

    private Long deptId; // 归属部门
    private String settleNo; // 结算单号
    private Integer settleType; // 结算类型
    private Integer partnerType; // 往来类型
    private Long partnerId; // 往来对象ID
    private String partnerName; // 往来对象名称
    private String periodStart; // 结算起日
    private String periodEnd; // 结算止日
    private Integer flowCnt; // 汇总笔数
    private BigDecimal amountTotal; // 结算合计
    private BigDecimal discountAmount; // 折让金额
    private BigDecimal settleAmount; // 结算金额
    private BigDecimal paidAmount; // 已收付金额
    private String settleDate; // 结算日期
    private Integer status; // 状态
    private Long confirmBy; // 确认人
    private String confirmTime; // 确认时间
    private String remark; // 备注
}

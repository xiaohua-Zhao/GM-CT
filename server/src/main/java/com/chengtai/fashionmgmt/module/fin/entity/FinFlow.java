package com.chengtai.fashionmgmt.module.fin.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.chengtai.fashionmgmt.common.LogicEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.math.BigDecimal;

/** 资金流水实体，对应表 ct_gm.t_fin_flow。 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("ct_gm.t_fin_flow")
public class FinFlow extends LogicEntity {
    @TableId(value = "fin_flow_id", type = IdType.AUTO)
    private Long id;

    private Long deptId; // 归属部门
    private String flowNo; // 流水单号
    private String businessKey; // 幂等业务键
    private Integer flowType; // 流水类型
    private Integer partnerType; // 往来类型
    private Long partnerId; // 往来对象ID
    private String partnerName; // 往来对象名称
    private Integer sourceType; // 来源类型
    private Long sourceId; // 来源单据ID
    private String sourceNo; // 来源单号
    private String bizDate; // 业务日期
    private String dueDate; // 到期日期
    private BigDecimal amount; // 发生金额
    private BigDecimal settledAmount; // 已核销金额
    private Integer settleStatus; // 结清状态
    private Long sourceFlowId; // 核销对象流水
    private Long finSettleId; // 结算单
    private Long finPlanId; // 收付款计划
    private Long accountId; // 收付账户
    private Integer payMethod; // 收付方式
    private String invoiceNo; // 发票号
    private Long operatorId; // 经办人
    private Integer status; // 审核状态
    private String remark; // 备注
}

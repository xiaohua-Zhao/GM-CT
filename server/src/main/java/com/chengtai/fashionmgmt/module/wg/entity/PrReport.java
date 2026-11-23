package com.chengtai.fashionmgmt.module.wg.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.chengtai.fashionmgmt.common.LogicEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.math.BigDecimal;

/** 报工流水实体，对应表 ct_gm.t_pr_report。 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("ct_gm.t_pr_report")
public class PrReport extends LogicEntity {
    @TableId(value = "report_id", type = IdType.AUTO)
    private Long id;

    private Long deptId; // 归属部门
    private String reportNo; // 报工批次号
    private Integer seqNo; // 行号
    private Long prodPlanId; // 生产计划
    private Long taskId; // 工序任务
    private Long orderId; // 订单
    private Integer processStage; // 工序
    private Long leaderId; // 提交组长
    private Long staffId; // 工人
    private String workDate; // 报工日期
    private BigDecimal qty; // 完成件数
    private BigDecimal qtyReject; // 不合格件数
    private Long qcRecordId; // 质检记录ID
    private BigDecimal price; // 单件工价
    private BigDecimal amount; // 计件金额
    private Integer status; // 审批状态
    private Long auditorId; // 财务审批人
    private String auditTime; // 审批时间
    private String remark; // 备注
}

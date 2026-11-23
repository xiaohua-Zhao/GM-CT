package com.chengtai.fashionmgmt.module.pl.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.chengtai.fashionmgmt.common.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/** 生产工序任务实体，对应表 ct_gm.t_pl_task。 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("ct_gm.t_pl_task")
public class PlTask extends BaseEntity {
    @TableId(value = "task_id", type = IdType.AUTO)
    private Long id;

    private Long prodPlanId; // 生产计划ID
    private String taskNo; // 工序任务号
    private Integer seqNo; // 拆分序号
    private Integer processStage; // 工序阶段
    private Long deptId; // 执行组
    private Long leaderId; // 组长
    private Integer status; // 任务状态
    private Long issuedBy; // 任务下发人
    private String issuedAt; // 任务下发时间
    private Long claimedBy; // 任务领取人
    private String claimedAt; // 任务领取时间
    private String actualStartAt; // 实际开始时间
    private String actualFinishAt; // 实际完成时间
    private BigDecimal qtyPlan; // 计划数量
    private BigDecimal qtyDone; // 完成数量
    private String startDatePlan; // 计划开始日期
    private String endDatePlan; // 计划结束日期
    private Integer qcNeed; // 是否需要质检
    private Integer qcStatus; // 质检状态
    private Integer qcType; // 质检类型
    private BigDecimal qcQtyCheck; // 累计抽检数量
    private BigDecimal qcQtyPass; // 累计合格数量
    private BigDecimal qcQtyReject; // 累计不合格数量
    private Long qcInspectorId; // 责任质检员
    private String qcLastDate; // 最近检验日期
    private String remark; // 备注
}

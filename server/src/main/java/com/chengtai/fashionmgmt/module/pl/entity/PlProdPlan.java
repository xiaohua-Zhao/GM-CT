package com.chengtai.fashionmgmt.module.pl.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.chengtai.fashionmgmt.common.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/** 生产计划实体，对应表 ct_gm.t_pl_prod_plan。 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("ct_gm.t_pl_prod_plan")
public class PlProdPlan extends BaseEntity {
    @TableId(value = "prod_plan_id", type = IdType.AUTO)
    private Long id;

    private Long deptId; // 归属部门
    private String prodPlanNo; // 生产计划单号
    private Long orderId; // 订单ID
    private BigDecimal prodQty; // 生产数量
    private String startDate; // 计划开工日期
    private String endDate; // 计划完工日期
    private Integer status; // 计划状态
    private Long plannerId; // 计划人或生产主管
    private Long releaseBy; // 下达人
    private String releaseTime; // 下达时间
    private String remark; // 备注
}

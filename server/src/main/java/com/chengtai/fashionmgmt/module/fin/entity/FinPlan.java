package com.chengtai.fashionmgmt.module.fin.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.chengtai.fashionmgmt.common.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/** 收付款计划，对应 t_fin_plan。 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_fin_plan")
public class FinPlan extends BaseEntity {
    @TableId(value="fin_plan_id",type=IdType.AUTO)
    private Long id;
    private Long deptId;
    private String planNo;
    private Integer direction;
    private Integer sourceType;
    private Long sourceId;
    private String sourceNo;
    private Integer partnerType;
    private Long partnerId;
    private String partnerName;
    private Integer seqNo;
    private Integer nodeType;
    private BigDecimal ratio;
    private String planDate;
    private BigDecimal planAmount;
    private BigDecimal paidAmount;
    private String paidDate;
    private Integer status;
    private String remark;
}

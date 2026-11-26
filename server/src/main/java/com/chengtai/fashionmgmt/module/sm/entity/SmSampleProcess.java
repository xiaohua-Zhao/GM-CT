package com.chengtai.fashionmgmt.module.sm.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.chengtai.fashionmgmt.common.LogicEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/** 样衣工序明细实体，对应 ct_gm.t_sm_sample_process。 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("ct_gm.t_sm_sample_process")
public class SmSampleProcess extends LogicEntity {
    @TableId(value = "sample_process_id", type = IdType.AUTO)
    private Long id;
    private Long deptId;
    private Long sampleId;
    private Long orderId;
    private Long styleId;
    private Integer processStage;
    private String partName;
    private BigDecimal price;
    private String unit;
    private String remark;
}

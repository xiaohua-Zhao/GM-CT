package com.chengtai.fashionmgmt.module.sm.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.chengtai.fashionmgmt.common.LogicEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/** 样衣跟单记录实体，对应 ct_gm.t_sm_sample_log。 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("ct_gm.t_sm_sample_log")
public class SmSampleLog extends LogicEntity {
    @TableId(value = "sample_log_id", type = IdType.AUTO)
    private Long id;
    private Long deptId;
    private Long sampleId;
    private String logTime;
    private Integer actionType;
    private String content;
    private Long operatorId;
    private String attachUrl;
    private String remark;
}

package com.chengtai.fashionmgmt.module.sys.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.chengtai.fashionmgmt.common.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/** 编码流水序列。 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_sys_code_sequence")
public class SysCodeSequence extends BaseEntity {
    @TableId(value = "sequence_id", type = IdType.AUTO)
    private Long id;
    private Long ruleId;
    private String periodKey;
    private Long currentValue = 0L;
    private Integer version = 0;
    private Integer isDel = 0;
}

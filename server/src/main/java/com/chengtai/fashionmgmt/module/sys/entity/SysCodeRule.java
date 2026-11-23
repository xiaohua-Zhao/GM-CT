package com.chengtai.fashionmgmt.module.sys.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.chengtai.fashionmgmt.common.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/** 通用编码规则。 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("ct_gm.t_sys_code_rule")
public class SysCodeRule extends BaseEntity {
    @TableId(value = "rule_id", type = IdType.AUTO)
    private Long id;
    private String ruleCode;
    private String ruleName;
    private String bizType;
    private String targetTable;
    private String targetColumn;
    private String prefix;
    private String dateFormat;
    @TableField("`separator`")
    private String separator;
    private Integer sequenceLength = 4;
    private Long sequenceStart = 1L;
    private Integer sequenceStep = 1;
    private String resetCycle = "NONE";
    private Integer allowManual = 1;
    private Integer enabled = 1;
    private String remark;
    private Integer isDel = 0;
}

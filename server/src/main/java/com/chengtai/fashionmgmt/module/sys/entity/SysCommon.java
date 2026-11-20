package com.chengtai.fashionmgmt.module.sys.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.chengtai.fashionmgmt.common.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 公共信息实体，对应表 t_sys_common。
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_sys_common")
public class SysCommon extends BaseEntity {
    @TableId(value = "dict_id", type = IdType.AUTO)
    private Long id;
    private String dictType; // 字典类型
    private String dictCode; // 字典编码
    private String dictName; // 字典名称
    private Long parentId; // 上级字典ID
    private String extValue; // 扩展值
    private Integer sortNo = 0; // 排序
    private Integer isSystem = 0; // 是否系统内置
    private Integer status = 1; // 状态
    private String remark; // 备注
}
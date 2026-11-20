package com.chengtai.fashionmgmt.module.ct.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.chengtai.fashionmgmt.common.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 角色管理实体，对应表 t_ct_role。
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_ct_role")
public class CtRole extends BaseEntity {
    @TableId(value = "role_id", type = IdType.AUTO)
    private Long id;
    private String roleCode; // 角色编码
    private String roleName; // 角色名称
    private Integer roleType = 2; // 角色类型
    private Integer dataScope = 2; // 数据范围
    private Integer inheritDept = 1; // 是否部门继承
    private Integer status = 1; // 状态
    private Integer sortNo = 0; // 排序
    private String remark; // 备注
}
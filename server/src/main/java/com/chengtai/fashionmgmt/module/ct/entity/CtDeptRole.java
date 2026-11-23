package com.chengtai.fashionmgmt.module.ct.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.chengtai.fashionmgmt.common.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 部门角色关系实体，对应表 t_ct_dept_role。
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("ct_gm.t_ct_dept_role")
public class CtDeptRole extends BaseEntity {
    @TableId(value = "dept_role_id", type = IdType.AUTO)
    private Long id;

    private Long deptId; // 部门ID
    private Long roleId; // 角色ID
    private Integer status = 1; // 关系状态
    private String effectiveDate; // 生效日期
    private String remark; // 备注
}

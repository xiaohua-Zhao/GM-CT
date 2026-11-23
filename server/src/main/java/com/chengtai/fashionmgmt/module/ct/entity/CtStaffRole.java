package com.chengtai.fashionmgmt.module.ct.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.chengtai.fashionmgmt.common.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 人员角色关系实体，对应表 t_ct_staff_role。
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("ct_gm.t_ct_staff_role")
public class CtStaffRole extends BaseEntity {
    @TableId(value = "staff_role_id", type = IdType.AUTO)
    private Long id;

    private Long staffId; // 人员ID
    private Long roleId; // 角色ID
    private Integer grantType = 1; // 授予方式
    private Integer status = 1; // 关系状态
    private String remark; // 备注
}

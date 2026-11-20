package com.chengtai.fashionmgmt.module.ct.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.chengtai.fashionmgmt.common.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 人员部门关系实体，对应表 t_ct_staff_dept。
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_ct_staff_dept")
public class CtStaffDept extends BaseEntity {
    @TableId(value = "staff_dept_id", type = IdType.AUTO)
    private Long id;

    private Long staffId; // 人员ID
    private Long deptId; // 部门ID
    private Integer isPrimary = 0; // 是否主部门
    private String joinDate; // 加入日期
    private Integer status = 1; // 关系状态
    private String remark; // 备注
}

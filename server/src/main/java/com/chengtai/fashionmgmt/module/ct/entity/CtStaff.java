package com.chengtai.fashionmgmt.module.ct.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.chengtai.fashionmgmt.common.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 人员管理实体，对应表 t_ct_staff。
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_ct_staff")
public class CtStaff extends BaseEntity {
    @TableId(value = "staff_id", type = IdType.AUTO)
    private Long id;
    private Long deptId; // 部门ID
    private String staffCode; // 工号
    private String staffName; // 姓名
    private String loginName; // 登录名
    private Integer gender; // 性别
    private String phone; // 电话
    private String post; // 岗位
    private Integer status = 1; // 状态
    private String hireDate; // 入职日期
    private String leaveDate; // 离职日期
    private String lastLoginAt; // 最后登录时间
    private String remark; // 备注
}
package com.chengtai.fashionmgmt.module.ct.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.chengtai.fashionmgmt.common.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 部门管理实体，对应表 t_ct_dept。
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_ct_dept")
public class CtDept extends BaseEntity {
    @TableId(value = "dept_id", type = IdType.AUTO)
    private Long id;
    private String deptCode; // 部门编码
    private String deptName; // 部门名称
    private String shortName; // 简称
    private Long parentId; // 上级部门ID
    private String deptPath = "/"; // 部门路径
    private Integer deptLevel = 1; // 层级
    private Long leaderStaffId; // 负责人ID
    private Integer status = 1; // 状态
    private String effectiveDate; // 生效日期
    private String expireDate; // 失效日期
    private Integer sortNo = 0; // 排序
    private String remark; // 备注
}
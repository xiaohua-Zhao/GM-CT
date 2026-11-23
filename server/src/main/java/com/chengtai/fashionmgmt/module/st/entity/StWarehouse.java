package com.chengtai.fashionmgmt.module.st.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.chengtai.fashionmgmt.common.LogicEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/** 仓库管理实体，对应表 ct_gm.t_st_warehouse。 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("ct_gm.t_st_warehouse")
public class StWarehouse extends LogicEntity {
    @TableId(value = "warehouse_id", type = IdType.AUTO)
    private Long id;

    private Long deptId; // 归属部门
    private String whCode; // 仓库编码
    private String whName; // 仓库名称
    private Integer whType; // 仓库类型
    private String location; // 库位地址
    private Long managerId; // 仓管员
    private Integer status; // 状态
    private String remark; // 备注
}

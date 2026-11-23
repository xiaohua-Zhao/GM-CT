package com.chengtai.fashionmgmt.module.sys.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.chengtai.fashionmgmt.common.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 供应商管理实体，对应表 t_sys_supplier。
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("ct_gm.t_sys_supplier")
public class SysSupplier extends BaseEntity {
    @TableId(value = "supplier_id", type = IdType.AUTO)
    private Long id;
    private Long deptId; // 归属部门ID
    private String supCode; // 供应商编码
    private String supName; // 供应商名称
    private String shortName; // 简称
    private String category; // 合作品类
    private String grade; // 等级
    private String contact; // 联系人
    private String phone; // 电话
    private String taxNo; // 税号
    private String settleType; // 结算方式
    private Integer creditDays; // 账期天数
    private String payMethod; // 付款方式
    private String score; // 评分
    private String startDate; // 合作开始日期
    private Integer status = 1; // 状态
    private String remark; // 备注
}
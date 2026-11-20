package com.chengtai.fashionmgmt.module.sys.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.chengtai.fashionmgmt.common.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 单位管理实体，对应表 t_sys_unit。
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_sys_unit")
public class SysUnit extends BaseEntity {
    @TableId(value = "unit_id", type = IdType.AUTO)
    private Long id;
    private Long deptId; // 归属部门ID
    private String unitCode; // 单位编码
    private String unitName; // 单位名称
    private String shortName; // 简称
    private String unitType; // 单位类型
    private String grade; // 等级
    private String contact; // 联系人
    private String phone; // 电话
    private String taxNo; // 税号
    private String invoiceTitle; // 开票抬头
    private String settleType; // 结算方式
    private Integer creditDays; // 账期天数
    private String deliveryAddr; // 送货地址
    private Long salesmanId; // 业务员ID
    private Integer status = 1; // 状态
    private String remark; // 备注
}
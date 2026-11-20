package com.chengtai.fashionmgmt.module.sys.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.chengtai.fashionmgmt.common.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 银行账户实体，对应表 t_sys_bank。
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_sys_bank")
public class SysBank extends BaseEntity {
    @TableId(value = "bank_id", type = IdType.AUTO)
    private Long id;
    private Integer ownerType = 3; // 所属类型
    private Long ownerId = 0L; // 所属对象ID
    private Integer accountType = 1; // 账户类型
    private String bankName; // 开户行
    private String branchName; // 开户支行
    private String accountName; // 账户名称
    private String accountNo; // 账号
    private String currency = "CNY"; // 币种
    private Integer isDefault = 0; // 是否默认
    private Integer status = 1; // 状态
    private String effDate; // 启用日期
    private String remark; // 备注
}
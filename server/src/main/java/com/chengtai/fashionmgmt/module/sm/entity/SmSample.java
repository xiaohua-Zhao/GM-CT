package com.chengtai.fashionmgmt.module.sm.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.chengtai.fashionmgmt.common.LogicEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/** 样衣实体，对应 ct_gm.t_sm_sample。 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("ct_gm.t_sm_sample")
public class SmSample extends LogicEntity {
    @TableId(value = "sample_id", type = IdType.AUTO)
    private Long id;
    private Long deptId;
    private String sampleNo;
    private Long orderId;
    private Long styleId;
    private Integer sampleType;
    private Integer roundNo;
    private Integer qty;
    private Long makerId;
    private String sendDate;
    private String expectBackDate;
    private String backDate;
    private Integer confirmResult;
    private String confirmDate;
    private Long confirmBy;
    private BigDecimal fee;
    private String courierNo;
    private Integer status;
    private String remark;

    @com.baomidou.mybatisplus.annotation.TableField(exist = false)
    private String orderNo; // 订单号

    @com.baomidou.mybatisplus.annotation.TableField(exist = false)
    private Long unitId; // 客户单位ID

    @com.baomidou.mybatisplus.annotation.TableField(exist = false)
    private String customerName; // 客户公司名称

    @com.baomidou.mybatisplus.annotation.TableField(exist = false)
    private String contactName; // 客户联系人

    @com.baomidou.mybatisplus.annotation.TableField(exist = false)
    private String contactPhone; // 客户电话
}

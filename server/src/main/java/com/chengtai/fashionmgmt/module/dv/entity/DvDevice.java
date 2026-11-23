package com.chengtai.fashionmgmt.module.dv.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.chengtai.fashionmgmt.common.LogicEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.math.BigDecimal;

/** 设备台账实体，对应表 ct_gm.t_dv_device。 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("ct_gm.t_dv_device")
public class DvDevice extends LogicEntity {
    @TableId(value = "device_id", type = IdType.AUTO)
    private Long id;

    private Long deptId; // 归属部门
    private String deviceNo; // 设备编号
    private String deviceName; // 设备名称
    private Integer deviceType; // 设备类型
    private String brand; // 品牌
    private String model; // 型号
    private String serialNo; // 出厂编号
    private Long factoryId; // 归属分厂
    private String location; // 存放位置
    private Long keeperId; // 保管人
    private String purchaseDate; // 购入日期
    private BigDecimal price; // 设备原值
    private Integer status; // 设备状态
    private Long borrowFactoryId; // 借入分厂
    private Long borrowDeptId; // 借入组
    private String borrowDate; // 借调日期
    private String transportBy; // 运输人
    private String expectReturnDate; // 预计归还
    private String returnDate; // 实际归还
    private String remark; // 备注
}

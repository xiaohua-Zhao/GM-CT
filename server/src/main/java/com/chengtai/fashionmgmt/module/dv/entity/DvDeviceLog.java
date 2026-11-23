package com.chengtai.fashionmgmt.module.dv.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.chengtai.fashionmgmt.common.LogicEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.math.BigDecimal;

/** 设备保养维修实体，对应表 ct_gm.t_dv_device_log。 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("ct_gm.t_dv_device_log")
public class DvDeviceLog extends LogicEntity {
    @TableId(value = "device_log_id", type = IdType.AUTO)
    private Long id;

    private Long deptId; // 归属部门
    private Long deviceId; // 设备
    private String logNo; // 记录单号
    private Integer logType; // 记录类型
    private String logDate; // 记录日期
    private Long handlerId; // 处理人
    private String faultDesc; // 故障描述
    private String logContent; // 处理内容
    private BigDecimal costAmount; // 维修/保养费用
    private String nextDate; // 下次保养日期
    private Integer status; // 状态
    private Integer attachCount; // 附件数量
    private String remark; // 备注
}

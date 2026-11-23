package com.chengtai.fashionmgmt.module.sys.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.chengtai.fashionmgmt.common.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.math.BigDecimal;

/**
 * 工序管理实体，对应表 t_sys_process。
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("ct_gm.t_sys_process")
public class SysProcess extends BaseEntity {
    @TableId(value = "process_id", type = IdType.AUTO)
    private Long id;
    private String processCode; // 工序编码
    private String processName; // 工序名称
    private Integer processType = 2; // 工序类型
    private Long deptId; // 执行部门ID
    private BigDecimal stdMinute; // 标准工时
    private BigDecimal basePrice; // 基础工价
    private String needEquipment; // 需要设备
    private Integer isKey = 0; // 是否关键工序
    private Integer sortNo = 0; // 排序
    private Integer status = 1; // 状态
    private String remark; // 备注
}
package com.chengtai.fashionmgmt.module.st.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.chengtai.fashionmgmt.common.LogicEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.math.BigDecimal;

/** 出入库流水实体，对应表 ct_gm.t_st_flow。 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("ct_gm.t_st_flow")
public class StFlow extends LogicEntity {
    @TableId(value = "flow_id", type = IdType.AUTO)
    private Long id;

    private Long deptId; // 归属部门
    private String flowNo; // 出入库单号
    private Integer seqNo; // 行号
    private Integer direction; // 方向
    private Integer bizType; // 业务类型
    private Long warehouseId; // 仓库
    private Integer itemType; // 物品类型
    private Long itemId; // 物品ID
    private String itemCode; // 物品编码
    private String itemName; // 物品名称
    private String color; // 颜色
    private String size; // 尺码
    private String unit; // 单位
    private BigDecimal qty; // 数量
    private BigDecimal price; // 单价
    private BigDecimal amount; // 金额
    private String batchNo; // 批次号
    private Integer sourceType; // 来源类型
    private String sourceNo; // 来源单号
    private Long taskId; // 关联工序任务
    private Long supplierId; // 供应商
    private String flowDate; // 业务日期
    private Long operatorId; // 经办人
    private Integer status; // 记账状态
    private String remark; // 备注
}

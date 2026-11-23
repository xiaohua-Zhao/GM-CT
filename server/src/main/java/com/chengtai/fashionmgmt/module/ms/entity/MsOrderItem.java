package com.chengtai.fashionmgmt.module.ms.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.chengtai.fashionmgmt.common.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/** 其他订单明细实体，对应表 ct_gm.t_ms_order_item。 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("ct_gm.t_ms_order_item")
public class MsOrderItem extends BaseEntity {
    @TableId(value = "ms_order_item_id", type = IdType.AUTO)
    private Long id;

    private Long deptId; // 归属部门
    private Long msOrderId; // 其他订单ID
    private Integer seqNo; // 行号
    private String itemName; // 项目名称
    private String spec; // 规格型号
    private String unit; // 单位
    private BigDecimal qty; // 数量
    private BigDecimal price; // 单价
    private BigDecimal amount; // 金额
    private Integer assetFlag; // 是否计入固定资产
    private Long useDeptId; // 使用部门
    private String remark; // 备注
}

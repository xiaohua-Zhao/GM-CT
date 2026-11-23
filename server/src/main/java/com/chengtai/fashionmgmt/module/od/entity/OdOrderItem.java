package com.chengtai.fashionmgmt.module.od.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.chengtai.fashionmgmt.common.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.math.BigDecimal;

/**
 * 订单明细实体，对应表 t_od_order_item。
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("ct_gm.t_od_order_item")
public class OdOrderItem extends BaseEntity {
    @TableId(value = "order_item_id", type = IdType.AUTO)
    private Long id;

    private Long deptId; // 归属部门ID
    private Long orderId; // 订单ID
    private Integer itemNo; // 行号
    private Long styleId; // 款式ID
    private String styleCode; // 款号快照
    private String color; // 颜色
    private String size; // 尺码
    private BigDecimal qty; // 数量
    private BigDecimal unitPrice; // 单价
    private BigDecimal amount; // 金额
    private BigDecimal taxRate; // 税率
    private String deliveryDatePlan; // 预计交付日期
    private BigDecimal qtyShipped; // 已发数量
    private String techDesc; // 工艺要求
    private String remark; // 备注
}

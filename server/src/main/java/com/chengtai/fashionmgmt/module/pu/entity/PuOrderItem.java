package com.chengtai.fashionmgmt.module.pu.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.chengtai.fashionmgmt.common.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.math.BigDecimal;

/**
 * 采购明细实体，对应表 t_pu_order_item。
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("ct_gm.t_pu_order_item")
public class PuOrderItem extends BaseEntity {
    @TableId(value = "po_item_id", type = IdType.AUTO)
    private Long id;

    private Long deptId; // 归属部门ID
    private Long poId; // 采购单ID
    private Long materialId; // 物料ID
    private String part; // 使用部位
    private String unit; // 单位
    private BigDecimal qty; // 采购数量
    private BigDecimal price; // 采购单价
    private BigDecimal amount; // 采购金额
    private BigDecimal qtyReceived; // 已到货数量
    private String arriveDate; // 到货日期
    private String remark; // 备注
}

package com.chengtai.fashionmgmt.module.st.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.chengtai.fashionmgmt.common.LogicEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.math.BigDecimal;

/** 库存管理实体，对应表 ct_gm.t_st_stock。 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("ct_gm.t_st_stock")
public class StStock extends LogicEntity {
    @TableId(value = "stock_id", type = IdType.AUTO)
    private Long id;

    private Long deptId; // 归属部门
    private Long warehouseId; // 仓库
    private Integer itemType; // 物品类型
    private Long itemId; // 物品ID
    private String itemCode; // 物品编码
    private String itemName; // 物品名称
    private String spec; // 规格
    private String color; // 颜色
    private String size; // 尺码
    private String unit; // 单位
    private BigDecimal qtyOnHand; // 现存数量
    private BigDecimal lockedQty; // 占用数量
    private BigDecimal avgPrice; // 结存均价
    private BigDecimal safetyQty; // 安全库存
    private String lastInDate; // 最后入库日
    private String lastOutDate; // 最后出库日
    private Integer status; // 状态
    private String remark; // 备注
}

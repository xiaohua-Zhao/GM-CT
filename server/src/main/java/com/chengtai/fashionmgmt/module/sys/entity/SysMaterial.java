package com.chengtai.fashionmgmt.module.sys.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.chengtai.fashionmgmt.common.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.math.BigDecimal;

/**
 * 物料管理实体，对应表 t_sys_material。
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_sys_material")
public class SysMaterial extends BaseEntity {
    @TableId(value = "material_id", type = IdType.AUTO)
    private Long id;
    private Long deptId; // 归属部门ID
    private String matCode; // 物料编码
    private String matName; // 物料名称
    private Integer category = 1; // 物料类别
    private String matType; // 细分类型
    private String spec; // 规格
    private String color; // 颜色
    private BigDecimal gramWeight; // 克重
    private BigDecimal width; // 幅宽
    private String unit = "米"; // 单位
    private BigDecimal safetyQty = BigDecimal.ZERO; // 安全库存
    private BigDecimal stdPrice = BigDecimal.ZERO; // 标准单价
    private BigDecimal taxRate = new BigDecimal("0.13"); // 税率
    private Long supplierId; // 默认供应商ID
    private Integer status = 1; // 状态
    private String remark; // 备注
}
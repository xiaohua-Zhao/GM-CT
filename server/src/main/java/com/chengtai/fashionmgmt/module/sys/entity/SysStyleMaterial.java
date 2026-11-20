package com.chengtai.fashionmgmt.module.sys.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.chengtai.fashionmgmt.common.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.math.BigDecimal;

/**
 * 款式用料实体，对应表 t_sys_style_material。
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_sys_style_material")
public class SysStyleMaterial extends BaseEntity {
    @TableId(value = "style_material_id", type = IdType.AUTO)
    private Long id;

    private Long styleId; // 款式ID
    private Long materialId; // 材料ID
    private String part = "整件"; // 使用部位
    private String unit = "米"; // 单位
    private BigDecimal unitConsume = BigDecimal.ZERO; // 单件用量
    private BigDecimal lossRate = new BigDecimal("0.05"); // 损耗率
    private Integer isMain = 0; // 是否主料
    private Integer seqNo = 0; // 排序
    private String remark; // 备注
}

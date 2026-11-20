package com.chengtai.fashionmgmt.module.sys.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.chengtai.fashionmgmt.common.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.math.BigDecimal;

/**
 * 款式管理实体，对应表 t_sys_style。
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_sys_style")
public class SysStyle extends BaseEntity {
    @TableId(value = "style_id", type = IdType.AUTO)
    private Long id;
    private Long deptId; // 归属部门ID
    private String styleCode; // 款号
    private String styleName; // 款式名称
    private String custStyleCode; // 客户款号
    private Long unitId; // 客户单位ID
    private String category; // 品类
    private String season; // 季节
    private String year; // 年份
    private Integer gender; // 适用性别
    private String techDesc; // 工艺说明
    private BigDecimal quotePrice; // 报价
    private BigDecimal sampleFee; // 样衣费
    private String imgUrl; // 图片地址
    private Integer status = 1; // 状态
    private String remark; // 备注
}
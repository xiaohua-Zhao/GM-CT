package com.chengtai.fashionmgmt.module.sys.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.chengtai.fashionmgmt.common.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.math.BigDecimal;

/**
 * 款式工序实体，对应表 t_sys_style_process。
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_sys_style_process")
public class SysStyleProcess extends BaseEntity {
    @TableId(value = "style_process_id", type = IdType.AUTO)
    private Long id;

    private Long styleId; // 款式ID
    private Long processId; // 工序ID
    private String processName; // 工序名称快照
    private Integer seqNo = 1; // 工序顺序
    private BigDecimal stdMinute = BigDecimal.ZERO; // 标准工时
    private BigDecimal piecePrice = BigDecimal.ZERO; // 计件单价
    private BigDecimal difficultyRate = BigDecimal.ONE; // 难度系数
    private Integer isOutsource = 0; // 是否可外协
    private String effDate; // 生效日期
    private Integer status = 1; // 状态
    private String remark; // 备注
}

package com.chengtai.fashionmgmt.common;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 所有业务实体的公共审计字段。
 * 主键由各实体使用 @TableId 映射到真实字段名。
 */
@Data
public abstract class BaseEntity {
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;

    public abstract Long getId();

    public abstract void setId(Long id);
}

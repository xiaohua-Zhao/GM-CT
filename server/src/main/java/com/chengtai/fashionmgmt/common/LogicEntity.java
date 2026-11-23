package com.chengtai.fashionmgmt.common;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.Data;
import lombok.EqualsAndHashCode;

/** 带创建人和逻辑删除标记的标准业务实体基类。 */
@Data
@EqualsAndHashCode(callSuper = true)
public abstract class LogicEntity extends BaseEntity {
    @TableField("created_by")
    private Long createdBy;

    @TableField("is_del")
    @TableLogic(value = "0", delval = "1")
    private Integer isDel;
}

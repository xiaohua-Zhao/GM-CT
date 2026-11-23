package com.chengtai.fashionmgmt.module.ct.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.chengtai.fashionmgmt.common.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 权限管理实体，对应表 t_ct_permission。
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("ct_gm.t_ct_permission")
public class CtPermission extends BaseEntity {
    @TableId(value = "permission_id", type = IdType.AUTO)
    private Long id;

    private Integer subjectType = 1; // 主体类型
    private Long subjectId; // 主体ID
    private Long pageId; // 页面ID
    private String actionCode = "view"; // 动作码
    private Integer operState = 1; // 操作状态
    private Integer grantSource = 1; // 授权来源
    private String effTime; // 生效时间
    private String expireTime; // 失效时间
    private String remark; // 授权原因
}

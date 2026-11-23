package com.chengtai.fashionmgmt.module.ct.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.chengtai.fashionmgmt.common.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 页面管理实体，对应表 t_ct_page。
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("ct_gm.t_ct_page")
public class CtPage extends BaseEntity {
    @TableId(value = "page_id", type = IdType.AUTO)
    private Long id;
    private String pageCode; // 页面编码
    private String pageName; // 页面名称
    private String moduleCode; // 模块编码
    private String routePath; // 路由
    private Integer pageType = 1; // 页面类型
    private Long parentPageId; // 上级页面ID
    private Integer isMenu = 1; // 是否菜单
    private String icon; // 图标
    private Integer sortNo = 0; // 排序
    private Integer status = 1; // 状态
    private String remark; // 备注
}
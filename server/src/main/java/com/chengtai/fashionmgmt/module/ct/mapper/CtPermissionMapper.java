package com.chengtai.fashionmgmt.module.ct.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.chengtai.fashionmgmt.module.ct.entity.CtPermission;
import org.apache.ibatis.annotations.Mapper;

/**
 * 权限管理持久层，对应表 t_ct_permission。
 */
@Mapper
public interface CtPermissionMapper extends BaseMapper<CtPermission> {}

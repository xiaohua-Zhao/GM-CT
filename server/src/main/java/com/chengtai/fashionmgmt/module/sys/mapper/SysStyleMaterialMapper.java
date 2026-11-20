package com.chengtai.fashionmgmt.module.sys.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.chengtai.fashionmgmt.module.sys.entity.SysStyleMaterial;
import org.apache.ibatis.annotations.Mapper;

/**
 * 款式用料持久层，对应表 t_sys_style_material。
 */
@Mapper
public interface SysStyleMaterialMapper extends BaseMapper<SysStyleMaterial> {}

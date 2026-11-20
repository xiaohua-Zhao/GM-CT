package com.chengtai.fashionmgmt.module.sys.service.impl;

import com.chengtai.fashionmgmt.common.AbstractBaseService;
import com.chengtai.fashionmgmt.module.sys.entity.SysStyleMaterial;
import com.chengtai.fashionmgmt.module.sys.mapper.SysStyleMaterialMapper;
import com.chengtai.fashionmgmt.module.sys.service.SysStyleMaterialService;
import org.springframework.stereotype.Service;

/**
 * 款式用料服务实现，通过 Mapper 操作 t_sys_style_material。
 */
@Service
public class SysStyleMaterialServiceImpl extends AbstractBaseService<SysStyleMaterialMapper, SysStyleMaterial> implements SysStyleMaterialService {}

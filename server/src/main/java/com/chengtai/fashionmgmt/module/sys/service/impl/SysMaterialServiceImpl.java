package com.chengtai.fashionmgmt.module.sys.service.impl;

import com.chengtai.fashionmgmt.common.AbstractBaseService;
import com.chengtai.fashionmgmt.module.sys.entity.SysMaterial;
import com.chengtai.fashionmgmt.module.sys.mapper.SysMaterialMapper;
import com.chengtai.fashionmgmt.module.sys.service.SysMaterialService;
import org.springframework.stereotype.Service;

/** 物料管理服务实现，通过 Mapper 操作 t_sys_material。 */
@Service
public class SysMaterialServiceImpl extends AbstractBaseService<SysMaterialMapper, SysMaterial> implements SysMaterialService {}

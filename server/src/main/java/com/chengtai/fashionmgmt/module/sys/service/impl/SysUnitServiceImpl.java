package com.chengtai.fashionmgmt.module.sys.service.impl;

import com.chengtai.fashionmgmt.common.AbstractBaseService;
import com.chengtai.fashionmgmt.module.sys.entity.SysUnit;
import com.chengtai.fashionmgmt.module.sys.mapper.SysUnitMapper;
import com.chengtai.fashionmgmt.module.sys.service.SysUnitService;
import org.springframework.stereotype.Service;

/** 单位管理服务实现，通过 Mapper 操作 t_sys_unit。 */
@Service
public class SysUnitServiceImpl extends AbstractBaseService<SysUnitMapper, SysUnit> implements SysUnitService {}

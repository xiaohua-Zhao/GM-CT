package com.chengtai.fashionmgmt.module.sys.service.impl;

import com.chengtai.fashionmgmt.common.AbstractBaseService;
import com.chengtai.fashionmgmt.module.sys.entity.SysStyle;
import com.chengtai.fashionmgmt.module.sys.mapper.SysStyleMapper;
import com.chengtai.fashionmgmt.module.sys.service.SysStyleService;
import org.springframework.stereotype.Service;

/** 款式管理服务实现，通过 Mapper 操作 t_sys_style。 */
@Service
public class SysStyleServiceImpl extends AbstractBaseService<SysStyleMapper, SysStyle> implements SysStyleService {}

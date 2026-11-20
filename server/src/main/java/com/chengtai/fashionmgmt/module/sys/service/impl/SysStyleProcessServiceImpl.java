package com.chengtai.fashionmgmt.module.sys.service.impl;

import com.chengtai.fashionmgmt.common.AbstractBaseService;
import com.chengtai.fashionmgmt.module.sys.entity.SysStyleProcess;
import com.chengtai.fashionmgmt.module.sys.mapper.SysStyleProcessMapper;
import com.chengtai.fashionmgmt.module.sys.service.SysStyleProcessService;
import org.springframework.stereotype.Service;

/**
 * 款式工序服务实现，通过 Mapper 操作 t_sys_style_process。
 */
@Service
public class SysStyleProcessServiceImpl extends AbstractBaseService<SysStyleProcessMapper, SysStyleProcess> implements SysStyleProcessService {}

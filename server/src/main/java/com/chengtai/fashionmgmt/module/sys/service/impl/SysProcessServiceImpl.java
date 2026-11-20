package com.chengtai.fashionmgmt.module.sys.service.impl;

import com.chengtai.fashionmgmt.common.AbstractBaseService;
import com.chengtai.fashionmgmt.module.sys.entity.SysProcess;
import com.chengtai.fashionmgmt.module.sys.mapper.SysProcessMapper;
import com.chengtai.fashionmgmt.module.sys.service.SysProcessService;
import org.springframework.stereotype.Service;

/** 工序管理服务实现，通过 Mapper 操作 t_sys_process。 */
@Service
public class SysProcessServiceImpl extends AbstractBaseService<SysProcessMapper, SysProcess> implements SysProcessService {}

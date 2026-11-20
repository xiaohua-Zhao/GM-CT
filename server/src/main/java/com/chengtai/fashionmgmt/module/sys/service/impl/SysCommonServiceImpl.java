package com.chengtai.fashionmgmt.module.sys.service.impl;

import com.chengtai.fashionmgmt.common.AbstractBaseService;
import com.chengtai.fashionmgmt.module.sys.entity.SysCommon;
import com.chengtai.fashionmgmt.module.sys.mapper.SysCommonMapper;
import com.chengtai.fashionmgmt.module.sys.service.SysCommonService;
import org.springframework.stereotype.Service;

/** 公共信息服务实现，通过 Mapper 操作 t_sys_common。 */
@Service
public class SysCommonServiceImpl extends AbstractBaseService<SysCommonMapper, SysCommon> implements SysCommonService {}

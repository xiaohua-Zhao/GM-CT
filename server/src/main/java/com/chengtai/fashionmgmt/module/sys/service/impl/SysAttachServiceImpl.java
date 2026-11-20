package com.chengtai.fashionmgmt.module.sys.service.impl;

import com.chengtai.fashionmgmt.common.AbstractBaseService;
import com.chengtai.fashionmgmt.module.sys.entity.SysAttach;
import com.chengtai.fashionmgmt.module.sys.mapper.SysAttachMapper;
import com.chengtai.fashionmgmt.module.sys.service.SysAttachService;
import org.springframework.stereotype.Service;

/**
 * 附件管理服务实现，通过 Mapper 操作 t_sys_attach。
 */
@Service
public class SysAttachServiceImpl extends AbstractBaseService<SysAttachMapper, SysAttach> implements SysAttachService {}

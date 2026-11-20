package com.chengtai.fashionmgmt.module.ct.service.impl;

import com.chengtai.fashionmgmt.common.AbstractBaseService;
import com.chengtai.fashionmgmt.module.ct.entity.CtPermission;
import com.chengtai.fashionmgmt.module.ct.mapper.CtPermissionMapper;
import com.chengtai.fashionmgmt.module.ct.service.CtPermissionService;
import org.springframework.stereotype.Service;

/**
 * 权限管理服务实现，通过 Mapper 操作 t_ct_permission。
 */
@Service
public class CtPermissionServiceImpl extends AbstractBaseService<CtPermissionMapper, CtPermission> implements CtPermissionService {}

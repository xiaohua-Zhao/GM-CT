package com.chengtai.fashionmgmt.module.ct.service.impl;

import com.chengtai.fashionmgmt.common.AbstractBaseService;
import com.chengtai.fashionmgmt.module.ct.entity.CtRole;
import com.chengtai.fashionmgmt.module.ct.mapper.CtRoleMapper;
import com.chengtai.fashionmgmt.module.ct.service.CtRoleService;
import org.springframework.stereotype.Service;

/** 角色管理服务实现，通过 Mapper 操作 t_ct_role。 */
@Service
public class CtRoleServiceImpl extends AbstractBaseService<CtRoleMapper, CtRole> implements CtRoleService {}

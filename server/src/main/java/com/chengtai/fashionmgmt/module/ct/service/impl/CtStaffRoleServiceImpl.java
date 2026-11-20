package com.chengtai.fashionmgmt.module.ct.service.impl;

import com.chengtai.fashionmgmt.common.AbstractBaseService;
import com.chengtai.fashionmgmt.module.ct.entity.CtStaffRole;
import com.chengtai.fashionmgmt.module.ct.mapper.CtStaffRoleMapper;
import com.chengtai.fashionmgmt.module.ct.service.CtStaffRoleService;
import org.springframework.stereotype.Service;

/**
 * 人员角色关系服务实现，通过 Mapper 操作 t_ct_staff_role。
 */
@Service
public class CtStaffRoleServiceImpl extends AbstractBaseService<CtStaffRoleMapper, CtStaffRole> implements CtStaffRoleService {}

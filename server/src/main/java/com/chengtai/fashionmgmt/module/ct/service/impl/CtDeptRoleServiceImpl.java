package com.chengtai.fashionmgmt.module.ct.service.impl;

import com.chengtai.fashionmgmt.common.AbstractBaseService;
import com.chengtai.fashionmgmt.module.ct.entity.CtDeptRole;
import com.chengtai.fashionmgmt.module.ct.mapper.CtDeptRoleMapper;
import com.chengtai.fashionmgmt.module.ct.service.CtDeptRoleService;
import org.springframework.stereotype.Service;

/**
 * 部门角色关系服务实现，通过 Mapper 操作 t_ct_dept_role。
 */
@Service
public class CtDeptRoleServiceImpl extends AbstractBaseService<CtDeptRoleMapper, CtDeptRole> implements CtDeptRoleService {}

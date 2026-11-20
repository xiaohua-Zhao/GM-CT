package com.chengtai.fashionmgmt.module.ct.service.impl;

import com.chengtai.fashionmgmt.common.AbstractBaseService;
import com.chengtai.fashionmgmt.module.ct.entity.CtStaffDept;
import com.chengtai.fashionmgmt.module.ct.mapper.CtStaffDeptMapper;
import com.chengtai.fashionmgmt.module.ct.service.CtStaffDeptService;
import org.springframework.stereotype.Service;

/**
 * 人员部门关系服务实现，通过 Mapper 操作 t_ct_staff_dept。
 */
@Service
public class CtStaffDeptServiceImpl extends AbstractBaseService<CtStaffDeptMapper, CtStaffDept> implements CtStaffDeptService {}

package com.chengtai.fashionmgmt.module.ct.service.impl;

import com.chengtai.fashionmgmt.common.AbstractBaseService;
import com.chengtai.fashionmgmt.module.ct.entity.CtDept;
import com.chengtai.fashionmgmt.module.ct.mapper.CtDeptMapper;
import com.chengtai.fashionmgmt.module.ct.service.CtDeptService;
import org.springframework.stereotype.Service;

/** 部门管理服务实现，通过 Mapper 操作 t_ct_dept。 */
@Service
public class CtDeptServiceImpl extends AbstractBaseService<CtDeptMapper, CtDept> implements CtDeptService {}

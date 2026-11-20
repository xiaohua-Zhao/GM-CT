package com.chengtai.fashionmgmt.module.ct.service.impl;

import com.chengtai.fashionmgmt.common.AbstractBaseService;
import com.chengtai.fashionmgmt.module.ct.entity.CtStaff;
import com.chengtai.fashionmgmt.module.ct.mapper.CtStaffMapper;
import com.chengtai.fashionmgmt.module.ct.service.CtStaffService;
import org.springframework.stereotype.Service;

/** 人员管理服务实现，通过 Mapper 操作 t_ct_staff。 */
@Service
public class CtStaffServiceImpl extends AbstractBaseService<CtStaffMapper, CtStaff> implements CtStaffService {}

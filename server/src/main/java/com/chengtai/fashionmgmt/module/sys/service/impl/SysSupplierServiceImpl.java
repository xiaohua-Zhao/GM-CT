package com.chengtai.fashionmgmt.module.sys.service.impl;

import com.chengtai.fashionmgmt.common.AbstractBaseService;
import com.chengtai.fashionmgmt.module.sys.entity.SysSupplier;
import com.chengtai.fashionmgmt.module.sys.mapper.SysSupplierMapper;
import com.chengtai.fashionmgmt.module.sys.service.SysSupplierService;
import org.springframework.stereotype.Service;

/** 供应商管理服务实现，通过 Mapper 操作 t_sys_supplier。 */
@Service
public class SysSupplierServiceImpl extends AbstractBaseService<SysSupplierMapper, SysSupplier> implements SysSupplierService {}

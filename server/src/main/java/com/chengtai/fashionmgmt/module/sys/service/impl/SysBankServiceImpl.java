package com.chengtai.fashionmgmt.module.sys.service.impl;

import com.chengtai.fashionmgmt.common.AbstractBaseService;
import com.chengtai.fashionmgmt.module.sys.entity.SysBank;
import com.chengtai.fashionmgmt.module.sys.mapper.SysBankMapper;
import com.chengtai.fashionmgmt.module.sys.service.SysBankService;
import org.springframework.stereotype.Service;

/** 银行账户服务实现，通过 Mapper 操作 t_sys_bank。 */
@Service
public class SysBankServiceImpl extends AbstractBaseService<SysBankMapper, SysBank> implements SysBankService {}

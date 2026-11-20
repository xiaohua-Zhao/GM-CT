package com.chengtai.fashionmgmt.module.ct.service.impl;

import com.chengtai.fashionmgmt.common.AbstractBaseService;
import com.chengtai.fashionmgmt.module.ct.entity.CtPage;
import com.chengtai.fashionmgmt.module.ct.mapper.CtPageMapper;
import com.chengtai.fashionmgmt.module.ct.service.CtPageService;
import org.springframework.stereotype.Service;

/** 页面管理服务实现，通过 Mapper 操作 t_ct_page。 */
@Service
public class CtPageServiceImpl extends AbstractBaseService<CtPageMapper, CtPage> implements CtPageService {}

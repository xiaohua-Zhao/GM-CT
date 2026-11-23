package com.chengtai.fashionmgmt.module.st.service;

import com.chengtai.fashionmgmt.common.AbstractBaseService;
import com.chengtai.fashionmgmt.module.st.entity.StStock;
import com.chengtai.fashionmgmt.module.st.mapper.StStockMapper;
import org.springframework.stereotype.Service;

/** 库存管理业务服务。 */
@Service
public class StStockService extends AbstractBaseService<StStockMapper, StStock> {}

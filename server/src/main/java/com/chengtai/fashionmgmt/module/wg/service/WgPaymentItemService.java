package com.chengtai.fashionmgmt.module.wg.service;

import com.chengtai.fashionmgmt.common.AbstractBaseService;
import com.chengtai.fashionmgmt.module.wg.entity.WgPaymentItem;
import com.chengtai.fashionmgmt.module.wg.mapper.WgPaymentItemMapper;
import org.springframework.stereotype.Service;

/** 工资发放明细业务服务。 */
@Service
public class WgPaymentItemService extends AbstractBaseService<WgPaymentItemMapper, WgPaymentItem> {}

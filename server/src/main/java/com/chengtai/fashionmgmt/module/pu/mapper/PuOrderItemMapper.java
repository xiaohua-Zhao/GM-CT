package com.chengtai.fashionmgmt.module.pu.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.chengtai.fashionmgmt.module.pu.entity.PuOrderItem;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/** 采购明细持久层。 */
@Mapper
public interface PuOrderItemMapper extends BaseMapper<PuOrderItem> {
    int insertBusiness(@Param("entity") PuOrderItem entity, @Param("createdBy") Long createdBy);
    int updateBusiness(@Param("entity") PuOrderItem entity);
}

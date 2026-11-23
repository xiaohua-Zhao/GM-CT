package com.chengtai.fashionmgmt.module.pu.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.chengtai.fashionmgmt.module.pu.entity.PuOrderItem;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/** 采购明细持久层。 */
@Mapper
public interface PuOrderItemMapper extends BaseMapper<PuOrderItem> {
    /**
     * 执行采购明细自定义新增 SQL，并返回受影响行数。
     */
    int insertBusiness(@Param("entity") PuOrderItem entity, @Param("createdBy") Long createdBy);
    /**
     * 执行采购明细自定义修改 SQL，并返回受影响行数。
     */
    int updateBusiness(@Param("entity") PuOrderItem entity);
}

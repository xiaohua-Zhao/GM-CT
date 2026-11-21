package com.chengtai.fashionmgmt.module.od.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.chengtai.fashionmgmt.module.od.entity.OdOrderItem;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/** 订单明细持久层。 */
@Mapper
public interface OdOrderItemMapper extends BaseMapper<OdOrderItem> {
    int insertBusiness(@Param("entity") OdOrderItem entity, @Param("createdBy") Long createdBy);
    int updateBusiness(@Param("entity") OdOrderItem entity);
    int countByItemNo(@Param("orderId") Long orderId, @Param("itemNo") Integer itemNo, @Param("excludeId") Long excludeId);
}

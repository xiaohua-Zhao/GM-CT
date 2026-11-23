package com.chengtai.fashionmgmt.module.od.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.chengtai.fashionmgmt.module.od.entity.OdOrderItem;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/** 订单明细持久层。 */
@Mapper
public interface OdOrderItemMapper extends BaseMapper<OdOrderItem> {
    /**
     * 执行订单明细自定义新增 SQL，并返回受影响行数。
     */
    int insertBusiness(@Param("entity") OdOrderItem entity, @Param("createdBy") Long createdBy);
    /**
     * 执行订单明细自定义修改 SQL，并返回受影响行数。
     */
    int updateBusiness(@Param("entity") OdOrderItem entity);
    /**
     * 检查同一订单下订单明细序号是否重复。
     */
    int countByItemNo(@Param("orderId") Long orderId, @Param("itemNo") Integer itemNo, @Param("excludeId") Long excludeId);
}

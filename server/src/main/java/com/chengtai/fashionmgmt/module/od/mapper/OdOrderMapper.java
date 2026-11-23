package com.chengtai.fashionmgmt.module.od.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.chengtai.fashionmgmt.module.od.entity.OdOrder;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 销售订单持久层。
 */
@Mapper
public interface OdOrderMapper extends BaseMapper<OdOrder> {
    int insertBusiness(@Param("entity") OdOrder entity, @Param("createdBy") Long createdBy);

    int updateBusiness(@Param("entity") OdOrder entity);

    int countByOrderNo(@Param("orderNo") String orderNo, @Param("excludeId") Long excludeId);

    int recalculateTotals(@Param("orderId") Long orderId);
}

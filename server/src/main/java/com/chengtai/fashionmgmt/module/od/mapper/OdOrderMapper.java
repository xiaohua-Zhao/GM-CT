package com.chengtai.fashionmgmt.module.od.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.chengtai.fashionmgmt.module.od.entity.OdOrder;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 订单持久层。
 */
@Mapper
public interface OdOrderMapper extends BaseMapper<OdOrder> {
    /**
     * 执行订单自定义新增 SQL，并返回受影响行数。
     */
    int insertBusiness(@Param("entity") OdOrder entity, @Param("createdBy") Long createdBy);

    /**
     * 执行订单自定义修改 SQL，并返回受影响行数。
     */
    int updateBusiness(@Param("entity") OdOrder entity);

    /**
     * 检查订单号是否重复，可排除当前记录自身。
     */
    int countByOrderNo(@Param("orderNo") String orderNo, @Param("excludeId") Long excludeId);

    /**
     * 根据订单明细汇总数量、金额和价税合计，并回写主单。
     */
    int recalculateTotals(@Param("orderId") Long orderId);
}

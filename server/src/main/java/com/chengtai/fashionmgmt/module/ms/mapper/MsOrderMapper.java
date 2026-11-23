package com.chengtai.fashionmgmt.module.ms.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.chengtai.fashionmgmt.module.ms.entity.MsOrder;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/** 其他订单持久层。 */
@Mapper
public interface MsOrderMapper extends BaseMapper<MsOrder> {
    /**
     * 执行其他订单自定义新增 SQL。
     */
    int insertBusiness(@Param("entity") MsOrder entity, @Param("createdBy") Long createdBy);

    /**
     * 执行其他订单自定义修改 SQL。
     */
    int updateBusiness(@Param("entity") MsOrder entity);

    /**
     * 检查其他单号是否重复。
     */
    int countByMiscNo(@Param("miscNo") String miscNo, @Param("excludeId") Long excludeId);

    /**
     * 根据其他订单明细汇总不含税金额和价税合计。
     */
    int recalculateTotals(@Param("msOrderId") Long msOrderId);

    /**
     * 逻辑删除其他订单。
     */
    int logicalDelete(@Param("msOrderId") Long msOrderId);
}

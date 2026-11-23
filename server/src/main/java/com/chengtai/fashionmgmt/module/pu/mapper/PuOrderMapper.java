package com.chengtai.fashionmgmt.module.pu.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.chengtai.fashionmgmt.module.pu.entity.PuOrder;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/** 采购订单持久层。 */
@Mapper
public interface PuOrderMapper extends BaseMapper<PuOrder> {
    /**
     * 执行采购订单自定义新增 SQL，并返回受影响行数。
     */
    int insertBusiness(@Param("entity") PuOrder entity, @Param("createdBy") Long createdBy);
    /**
     * 执行采购订单自定义修改 SQL，并返回受影响行数。
     */
    int updateBusiness(@Param("entity") PuOrder entity);
    /**
     * 检查采购单号是否重复，可排除当前记录自身。
     */
    int countByPoNo(@Param("poNo") String poNo, @Param("excludeId") Long excludeId);
    /**
     * 根据采购明细汇总数量、金额和价税合计，并回写主单。
     */
    int recalculateTotals(@Param("poId") Long poId);
}

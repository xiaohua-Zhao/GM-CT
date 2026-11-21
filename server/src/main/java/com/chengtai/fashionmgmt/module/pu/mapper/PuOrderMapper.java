package com.chengtai.fashionmgmt.module.pu.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.chengtai.fashionmgmt.module.pu.entity.PuOrder;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/** 采购订单持久层。 */
@Mapper
public interface PuOrderMapper extends BaseMapper<PuOrder> {
    int insertBusiness(@Param("entity") PuOrder entity, @Param("createdBy") Long createdBy);
    int updateBusiness(@Param("entity") PuOrder entity);
    int countByPoNo(@Param("poNo") String poNo, @Param("excludeId") Long excludeId);
    int recalculateTotals(@Param("poId") Long poId);
}

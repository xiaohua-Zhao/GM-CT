package com.chengtai.fashionmgmt.module.pl.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.chengtai.fashionmgmt.module.pl.entity.PlProdPlan;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/** 生产计划持久层。 */
@Mapper
public interface PlProdPlanMapper extends BaseMapper<PlProdPlan> {
    /**
     * 执行生产计划自定义新增 SQL。
     */
    int insertBusiness(@Param("entity") PlProdPlan entity, @Param("createdBy") Long createdBy);

    /**
     * 执行生产计划自定义修改 SQL。
     */
    int updateBusiness(@Param("entity") PlProdPlan entity);

    /**
     * 检查生产计划单号是否重复。
     */
    int countByPlanNo(@Param("prodPlanNo") String prodPlanNo, @Param("excludeId") Long excludeId);

    /**
     * 检查订单是否已经存在有效生产计划。
     */
    int countByOrderId(@Param("orderId") Long orderId, @Param("excludeId") Long excludeId);

    /**
     * 逻辑删除生产计划。
     */
    int logicalDelete(@Param("prodPlanId") Long prodPlanId);
}

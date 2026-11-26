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
    /** 为当前订单明细行号临时偏移，避免重排行触发唯一键冲突。 */
    int offsetItemNos(@Param("orderId") Long orderId);

    /** 逻辑删除订单明细。 */
    int logicalDelete(@Param("itemId") Long itemId);
    /** 按订单汇总款式 BOM 物料成本。 */
    java.math.BigDecimal selectMaterialCostByOrderId(@Param("orderId") Long orderId);

    /** 按订单汇总款式工序用工成本。 */
    java.math.BigDecimal selectLaborCostByOrderId(@Param("orderId") Long orderId);

    /** 按款式汇总单件 BOM 物料成本。 */
    java.math.BigDecimal selectMaterialCostByStyleId(@Param("styleId") Long styleId);

    /** 按款式汇总单件工序用工成本。 */
    java.math.BigDecimal selectLaborCostByStyleId(@Param("styleId") Long styleId);
}

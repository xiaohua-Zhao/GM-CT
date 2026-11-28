package com.chengtai.fashionmgmt.module.pl.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.chengtai.fashionmgmt.module.od.entity.OdOrder;
import com.chengtai.fashionmgmt.module.pl.entity.PlProdPlan;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/** 生产计划持久层。 */
@Mapper
public interface PlProdPlanMapper extends BaseMapper<PlProdPlan> {
    /**
     * 分页查询可创建生产计划的订单。
     * SQL 见 PlProdPlanMapper.xml，筛选条件为：未删除、评审通过、样衣已放行、
     * 待生产、未关闭且尚未创建有效生产计划。
     *
     * @param page              分页参数，由 MyBatis-Plus 分页插件拦截并改写 SQL
     * @param keyword           关键字，模糊匹配订单号/合同号/客户单位名称，可为空
     * @param unitId            客户单位 ID，可为空
     * @param orderDateStart    下单日期起始（含），可为空
     * @param orderDateEnd      下单日期结束（含），可为空
     * @param deliveryDateStart 预计交期起始（含），可为空
     * @param deliveryDateEnd   预计交期结束（含），可为空
     * @return 可排产订单分页结果
     */
    IPage<OdOrder> pageEligibleOrders(IPage<OdOrder> page,
                                      @Param("keyword") String keyword,
                                      @Param("unitId") Long unitId,
                                      @Param("orderDateStart") String orderDateStart,
                                      @Param("orderDateEnd") String orderDateEnd,
                                      @Param("deliveryDateStart") String deliveryDateStart,
                                      @Param("deliveryDateEnd") String deliveryDateEnd);

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

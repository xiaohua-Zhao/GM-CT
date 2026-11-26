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
    /** 按主键查询未删除订单。 */
    OdOrder selectActiveById(@Param("orderId") Long orderId);

    /** 写入评审通过结果。 */
    int updateReviewPassed(@Param("orderId") Long orderId, @Param("reviewBy") Long reviewBy, @Param("reviewTime") String reviewTime);

    /** 写入评审不通过及关闭留痕。 */
    int updateReviewRejected(@Param("orderId") Long orderId, @Param("reviewBy") Long reviewBy, @Param("reviewTime") String reviewTime, @Param("closeReasonType") Integer closeReasonType, @Param("closeReason") String closeReason);

    /** 写入审核通过结果。 */
    int updateAuditPassed(@Param("orderId") Long orderId, @Param("reviewBy") Long reviewBy, @Param("reviewTime") String reviewTime, @Param("auditRemark") String auditRemark);

    /** 写入审核不通过及待复议结果。 */
    int updateAuditRejected(@Param("orderId") Long orderId, @Param("reviewBy") Long reviewBy, @Param("reviewTime") String reviewTime, @Param("auditRemark") String auditRemark);

    /** 写入订单关闭结果。 */
    int updateClose(@Param("orderId") Long orderId, @Param("closeReasonType") Integer closeReasonType, @Param("closeReason") String closeReason, @Param("closeBy") Long closeBy, @Param("closeTime") String closeTime);

    /** 更新订单状态。 */
    int updateStatus(@Param("orderId") Long orderId, @Param("status") Integer status);

    /** 写入订单状态历史。 */
    int insertStatusLog(@Param("orderId") Long orderId, @Param("deptId") Long deptId, @Param("fromStatus") Integer fromStatus, @Param("toStatus") Integer toStatus, @Param("operatorId") Long operatorId, @Param("remark") String remark);

    /** 查询最近一次非暂停状态。 */
    Integer selectPreviousStatus(@Param("orderId") Long orderId);

    /** 统计订单下有效生产计划数量。 */
    int countActiveProductionPlans(@Param("orderId") Long orderId);

    /** 样衣确认通过后放行订单生产。 */
    int releaseForProduction(@Param("orderId") Long orderId, @Param("releaseTime") String releaseTime);
}

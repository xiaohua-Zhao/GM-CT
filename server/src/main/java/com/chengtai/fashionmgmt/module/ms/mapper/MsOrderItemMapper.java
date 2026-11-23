package com.chengtai.fashionmgmt.module.ms.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.chengtai.fashionmgmt.module.ms.entity.MsOrderItem;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/** 其他订单明细持久层。 */
@Mapper
public interface MsOrderItemMapper extends BaseMapper<MsOrderItem> {
    /**
     * 执行其他订单明细自定义新增 SQL。
     */
    int insertBusiness(@Param("entity") MsOrderItem entity, @Param("createdBy") Long createdBy);

    /**
     * 执行其他订单明细自定义修改 SQL。
     */
    int updateBusiness(@Param("entity") MsOrderItem entity);

    /**
     * 检查同一其他订单内的行号是否重复。
     */
    int countBySeqNo(@Param("msOrderId") Long msOrderId, @Param("seqNo") Integer seqNo,
                     @Param("excludeId") Long excludeId);

    /**
     * 获取同一其他订单内的下一个行号。
     */
    int nextSeqNo(@Param("msOrderId") Long msOrderId);

    /**
     * 逻辑删除单条其他订单明细。
     */
    int logicalDelete(@Param("itemId") Long itemId);

    /**
     * 逻辑删除指定其他订单的全部明细。
     */
    int logicalDeleteByMsOrderId(@Param("msOrderId") Long msOrderId);
}

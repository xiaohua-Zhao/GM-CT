package com.chengtai.fashionmgmt.module.fin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.chengtai.fashionmgmt.module.fin.entity.FinPlan;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/** 收款计划持久层。 */
@Mapper
public interface FinPlanMapper extends BaseMapper<FinPlan> {
    int insertBusiness(@Param("entity") FinPlan entity, @Param("createdBy") Long createdBy);
    int updateBusiness(@Param("entity") FinPlan entity);
    int countByPlanNo(@Param("planNo") String planNo, @Param("excludeId") Long excludeId);
    int countBySourceSeq(@Param("sourceType") Integer sourceType, @Param("sourceId") Long sourceId, @Param("seqNo") Integer seqNo, @Param("excludeId") Long excludeId);
}

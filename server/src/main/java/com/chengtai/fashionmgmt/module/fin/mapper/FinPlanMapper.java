package com.chengtai.fashionmgmt.module.fin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.chengtai.fashionmgmt.module.fin.entity.FinPlan;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/** 收款计划持久层。 */
@Mapper
public interface FinPlanMapper extends BaseMapper<FinPlan> {
    /**
     * 执行收付款计划自定义新增 SQL，并返回受影响行数。
     */
    int insertBusiness(@Param("entity") FinPlan entity, @Param("createdBy") Long createdBy);
    /**
     * 执行收付款计划自定义修改 SQL，并返回受影响行数。
     */
    int updateBusiness(@Param("entity") FinPlan entity);
    /**
     * 检查收付款计划单号是否重复，可排除当前记录自身。
     */
    int countByPlanNo(@Param("planNo") String planNo, @Param("excludeId") Long excludeId);
    /**
     * 检查同一来源单据下是否存在相同节点序号。
     */
    int countBySourceSeq(@Param("sourceType") Integer sourceType, @Param("sourceId") Long sourceId, @Param("seqNo") Integer seqNo, @Param("excludeId") Long excludeId);
}

package com.chengtai.fashionmgmt.module.pl.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.chengtai.fashionmgmt.module.pl.entity.PlTask;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/** 生产工序任务持久层。 */
@Mapper
public interface PlTaskMapper extends BaseMapper<PlTask> {
    /**
     * 执行工序任务自定义新增 SQL。
     */
    int insertBusiness(@Param("entity") PlTask entity, @Param("createdBy") Long createdBy);

    /**
     * 执行工序任务自定义修改 SQL。
     */
    int updateBusiness(@Param("entity") PlTask entity);

    /**
     * 检查工序任务号是否重复。
     */
    int countByTaskNo(@Param("taskNo") String taskNo, @Param("excludeId") Long excludeId);

    /**
     * 检查同一计划、工序、执行组下的拆分序号是否重复。
     */
    int countByPlanStageDeptSeq(@Param("prodPlanId") Long prodPlanId,
                                @Param("processStage") Integer processStage,
                                @Param("deptId") Long deptId,
                                @Param("seqNo") Integer seqNo,
                                @Param("excludeId") Long excludeId);

    /**
     * 获取同一计划、工序和执行组下的下一个拆分序号。
     */
    int nextSeqNo(@Param("prodPlanId") Long prodPlanId,
                  @Param("processStage") Integer processStage,
                  @Param("deptId") Long deptId);

    /**
     * 统计生产计划下的有效工序任务数量。
     */
    int countByPlanId(@Param("prodPlanId") Long prodPlanId);

    /**
     * 逻辑删除工序任务。
     */
    int logicalDelete(@Param("taskId") Long taskId);
}

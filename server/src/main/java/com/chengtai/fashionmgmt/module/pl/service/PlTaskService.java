package com.chengtai.fashionmgmt.module.pl.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.chengtai.fashionmgmt.common.BaseService;
import com.chengtai.fashionmgmt.module.pl.entity.PlTask;

import java.util.List;

/** 生产工序任务服务接口。 */
public interface PlTaskService extends BaseService<PlTask> {
    /**
     * 按工序阶段分页查询生产任务；阶段为空时查询全部任务。
     */
    IPage<PlTask> pageByStage(long current, long size, String keyword, String keywordColumn, Integer processStage);

    /**
     * 按生产计划 ID 查询全部有效工序任务。
     */
    List<PlTask> listByPlanId(Long prodPlanId);
}

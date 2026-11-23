package com.chengtai.fashionmgmt.module.pl.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.chengtai.fashionmgmt.common.BaseCrudController;
import com.chengtai.fashionmgmt.common.BaseService;
import com.chengtai.fashionmgmt.common.Result;
import com.chengtai.fashionmgmt.module.pl.entity.PlTask;
import com.chengtai.fashionmgmt.module.pl.service.PlTaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/** 生产工序任务接口，按工序阶段提供裁剪、缝制、剪线、整烫和包装任务 CRUD。 */
@Tag(name = "生产工序任务")
@RestController
@RequestMapping("/api/pl/tasks")
@RequiredArgsConstructor
public class PlTaskController extends BaseCrudController<PlTask> {
    private final PlTaskService service;

    /**
     * 返回工序任务模块的业务 Service。
     */
    @Override
    protected BaseService<PlTask> service() {
        return service;
    }

    /**
     * 返回工序任务列表模糊查询使用的字段。
     */
    @Override
    protected String keywordField() {
        return "task_no";
    }

    /**
     * 返回工序任务模块名称，用于接口日志和 Swagger。
     */
    @Override
    protected String moduleName() {
        return "工序任务";
    }

    /**
     * 按工序阶段 stage 执行分页查询；stage 为空时查询全部工序任务。
     */
    @Override
    protected IPage<PlTask> pageData(long current, long size, String keyword, Integer stage) {
        return service.pageByStage(current, size, keyword, keywordField(), stage);
    }

    /**
     * 按生产计划 ID 查询全部工序任务。
     */
    @Operation(summary = "按生产计划查询工序任务")
    @GetMapping("/by-plan/{prodPlanId}")
    public Result<List<PlTask>> byPlan(@PathVariable Long prodPlanId) {
        return Result.success(service.listByPlanId(prodPlanId));
    }
}

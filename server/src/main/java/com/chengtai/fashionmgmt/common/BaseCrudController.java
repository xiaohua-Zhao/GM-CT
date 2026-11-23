package com.chengtai.fashionmgmt.common;

import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 通用 CRUD Controller。
 * 所有接口均记录 Controller 级入参、出参和业务上下文日志，
 * 便于生产环境按模块名称和操作类型检索。
 */
@Slf4j
public abstract class BaseCrudController<T extends BaseEntity> {
    @Autowired
    private BusinessLogJson logJson;

    /** 当前模块对应的业务 Service。 */
    protected abstract BaseService<T> service();

    /** 当前模块支持模糊查询的数据库字段。 */
    protected abstract String keywordField();

    /** 当前模块中文名称，用于日志和接口文档。 */
    protected abstract String moduleName();

    /**
     * 分页查询，记录查询条件、分页参数和返回结果摘要。
     */
    @Operation(summary = "分页查询", description = "按当前模块和关键字分页查询数据")
    @GetMapping("/page")
    public Result<IPage<T>> page(
            @Parameter(description = "当前页码") @RequestParam(defaultValue = "1") long current,
            @Parameter(description = "每页条数") @RequestParam(defaultValue = "10") long size,
            @Parameter(description = "关键字") @RequestParam(required = false) String keyword,
            @Parameter(description = "可选的业务阶段筛选") @RequestParam(required = false) Integer stage) {
        log.info("[CONTROLLER][{}][PAGE][IN] class={} method={} current={} size={} keyword={} stage={}",
                moduleName(), controllerClassName(), "page", current, size, keyword, stage);
        IPage<T> result = pageData(current, size, keyword, stage);
        log.info("[CONTROLLER][{}][PAGE][OUT] class={} method={} total={} pages={} result={}",
                moduleName(), controllerClassName(), "page", result.getTotal(), result.getPages(), logJson.toJson(result));
        return Result.success(result);
    }

    /**
     * 执行具体模块的分页查询；子类可覆盖该方法处理阶段等业务筛选条件。
     */
    protected IPage<T> pageData(long current, long size, String keyword, Integer stage) {
        return service().page(current, size, keyword, keywordField());
    }

    /**
     * 查询单条详情，记录主键和返回实体。
     */
    @Operation(summary = "查询详情", description = "按主键查询当前模块的一条数据")
    @GetMapping("/{id}")
    public Result<T> get(@Parameter(description = "主键ID") @PathVariable Long id) {
        log.info("[CONTROLLER][{}][GET][IN] class={} method={} id={}",
                moduleName(), controllerClassName(), "get", id);
        T entity = service().findById(id);
        if (entity == null) {
            log.warn("[CONTROLLER][{}][GET][NOT_FOUND] class={} method={} id={}",
                    moduleName(), controllerClassName(), "get", id);
            return Result.fail(404, "数据不存在");
        }
        log.info("[CONTROLLER][{}][GET][OUT] class={} method={} id={} result={}",
                moduleName(), controllerClassName(), "get", id, logJson.toJson(entity));
        return Result.success(entity);
    }

    /**
     * 新增数据，记录 Controller 请求参数和业务 Service 返回结果。
     */
    @Operation(summary = "新增", description = "新增一条当前模块数据")
    @PostMapping
    public Result<T> create(@RequestBody T entity) {
        log.info("[CONTROLLER][{}][CREATE][IN] class={} method={} request={}",
                moduleName(), controllerClassName(), "create", logJson.toJson(entity));
        entity.setId(null);
        boolean success = service().create(entity);
        if (!success) {
            log.error("[CONTROLLER][{}][CREATE][FAILED] class={} method={} request={}",
                    moduleName(), controllerClassName(), "create", logJson.toJson(entity));
            return Result.fail(500, "新增失败");
        }
        log.info("[CONTROLLER][{}][CREATE][OUT] class={} method={} id={} result={}",
                moduleName(), controllerClassName(), "create", entity.getId(), logJson.toJson(entity));
        return Result.success("新增成功", entity);
    }

    /**
     * 修改数据，记录主键、请求参数和修改后的业务结果。
     */
    @Operation(summary = "修改", description = "按主键修改当前模块数据")
    @PutMapping("/{id}")
    public Result<T> update(
            @Parameter(description = "主键ID") @PathVariable Long id,
            @RequestBody T entity) {
        log.info("[CONTROLLER][{}][UPDATE][IN] class={} method={} id={} request={}",
                moduleName(), controllerClassName(), "update", id, logJson.toJson(entity));
        entity.setId(id);
        boolean success = service().update(entity);
        if (!success) {
            log.error("[CONTROLLER][{}][UPDATE][FAILED] class={} method={} id={} request={}",
                    moduleName(), controllerClassName(), "update", id, logJson.toJson(entity));
            return Result.fail(500, "修改失败");
        }
        T result = service().findById(id);
        log.info("[CONTROLLER][{}][UPDATE][OUT] class={} method={} id={} result={}",
                moduleName(), controllerClassName(), "update", id, logJson.toJson(result));
        return Result.success("修改成功", result);
    }

    /**
     * 删除数据，记录主键和业务执行结果。
     */
    @Operation(summary = "删除", description = "按主键删除当前模块数据")
    @DeleteMapping("/{id}")
    public Result<Void> delete(@Parameter(description = "主键ID") @PathVariable Long id) {
        log.info("[CONTROLLER][{}][DELETE][IN] class={} method={} id={}",
                moduleName(), controllerClassName(), "delete", id);
        boolean success = service().delete(id);
        if (!success) {
            log.error("[CONTROLLER][{}][DELETE][FAILED] class={} method={} id={}",
                    moduleName(), controllerClassName(), "delete", id);
            return Result.fail(500, "删除失败");
        }
        log.info("[CONTROLLER][{}][DELETE][OUT] class={} method={} id={} result=success",
                moduleName(), controllerClassName(), "delete", id);
        return Result.success("删除成功", null);
    }

    /** 获取实际 Controller 类名，避免输出 CGLIB 代理类名。 */
    private String controllerClassName() {
        Class<?> type = getClass();
        while (type.getName().contains("$$")) type = type.getSuperclass();
        return type.getSimpleName();
    }
}

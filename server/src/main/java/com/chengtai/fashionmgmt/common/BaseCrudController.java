package com.chengtai.fashionmgmt.common;

import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 通用增删改查 Controller。
 * <p>
 * 所有基础模块共用这一套接口，具体模块只需要提供 Service 和关键字字段。
 * 调用链：Controller -> Service -> Mapper -> 数据库。
 */
@Slf4j
public abstract class BaseCrudController<T extends BaseEntity> {
    /**
     * 当前模块对应的业务 Service。
     */
    protected abstract BaseService<T> service();

    /**
     * 当前模块支持模糊查询的数据库字段。
     */
    protected abstract String keywordField();

    /**
     * 当前模块中文名称，用于日志和接口文档。
     */
    protected abstract String moduleName();

    /**
     * 分页查询。
     */
    @Operation(summary = "分页查询", description = "按当前模块和关键字分页查询数据")
    @GetMapping("/page")
    public Result<IPage<T>> page(
            @Parameter(description = "当前页码") @RequestParam(defaultValue = "1") long current,
            @Parameter(description = "每页条数") @RequestParam(defaultValue = "10") long size,
            @Parameter(description = "关键字") @RequestParam(required = false) String keyword) {
        log.info("[{}] 分页查询 current={}, size={}, keyword={}", moduleName(), current, size, keyword);
        return Result.success(service().page(current, size, keyword, keywordField()));
    }

    /**
     * 查询单条详情。
     */
    @Operation(summary = "查询详情", description = "按主键查询当前模块的一条数据")
    @GetMapping("/{id}")
    public Result<T> get(@Parameter(description = "主键ID") @PathVariable Long id) {
        log.info("[{}] 查询详情 id={}", moduleName(), id);
        T entity = service().findById(id);
        return entity == null ? Result.fail(404, "数据不存在") : Result.success(entity);
    }

    /**
     * 新增数据。
     */
    @Operation(summary = "新增", description = "新增一条当前模块数据")
    @PostMapping
    public Result<T> create(@RequestBody T entity) {
        log.info("[{}] 新增数据 data={}", moduleName(), entity);
        entity.setId(null);
        service().create(entity);
        return Result.success("新增成功", entity);
    }

    /**
     * 修改数据。
     */
    @Operation(summary = "修改", description = "按主键修改当前模块数据")
    @PutMapping("/{id}")
    public Result<T> update(
            @Parameter(description = "主键ID") @PathVariable Long id,
            @RequestBody T entity) {
        log.info("[{}] 修改数据 id={}, data={}", moduleName(), id, entity);
        entity.setId(id);
        service().update(entity);
        return Result.success("修改成功", service().findById(id));
    }

    /**
     * 删除数据。
     */
    @Operation(summary = "删除", description = "按主键删除当前模块数据")
    @DeleteMapping("/{id}")
    public Result<Void> delete(@Parameter(description = "主键ID") @PathVariable Long id) {
        log.info("[{}] 删除数据 id={}", moduleName(), id);
        service().delete(id);
        return Result.success("删除成功", null);
    }
}

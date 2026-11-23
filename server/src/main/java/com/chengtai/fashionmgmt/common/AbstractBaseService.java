package com.chengtai.fashionmgmt.common;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.util.StringUtils;

public abstract class AbstractBaseService<M extends BaseMapper<T>, T extends BaseEntity>
        extends ServiceImpl<M, T> implements BaseService<T> {

    /**
     * 按页码、每页条数和可选关键字分页查询，并按创建时间倒序返回。
     */
    @Override
    public IPage<T> page(long current, long size, String keyword, String keywordColumn) {
        QueryWrapper<T> query = new QueryWrapper<>();
        if (StringUtils.hasText(keyword)) {
            query.like(keywordColumn, keyword.trim());
        }
        query.orderByDesc("created_at");
        return page(new Page<>(current, size), query);
    }

    /**
     * 根据主键查询一条实体，供详情接口和内部业务复用。
     */
    @Override
    public T findById(Long id) {
        return getById(id);
    }

    /**
     * 使用 MyBatis-Plus 通用能力保存一条实体。
     */
    @Override
    public boolean create(T entity) {
        return save(entity);
    }

    /**
     * 使用 MyBatis-Plus 通用能力按主键更新实体。
     */
    @Override
    public boolean update(T entity) {
        return updateById(entity);
    }

    /**
     * 使用 MyBatis-Plus 通用能力按主键删除实体。
     */
    @Override
    public boolean delete(Long id) {
        return removeById(id);
    }
}

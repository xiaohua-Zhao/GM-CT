package com.chengtai.fashionmgmt.common;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.util.StringUtils;

public abstract class AbstractBaseService<M extends BaseMapper<T>, T extends BaseEntity>
        extends ServiceImpl<M, T> implements BaseService<T> {

    @Override
    public IPage<T> page(long current, long size, String keyword, String keywordColumn) {
        QueryWrapper<T> query = new QueryWrapper<>();
        if (StringUtils.hasText(keyword)) {
            query.like(keywordColumn, keyword.trim());
        }
        query.orderByDesc("created_at");
        return page(new Page<>(current, size), query);
    }

    @Override
    public T findById(Long id) {
        return getById(id);
    }

    @Override
    public boolean create(T entity) {
        return save(entity);
    }

    @Override
    public boolean update(T entity) {
        return updateById(entity);
    }

    @Override
    public boolean delete(Long id) {
        return removeById(id);
    }
}

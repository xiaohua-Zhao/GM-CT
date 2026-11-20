package com.chengtai.fashionmgmt.common;

import com.baomidou.mybatisplus.core.metadata.IPage;

public interface BaseService<T extends BaseEntity> {
    IPage<T> page(long current, long size, String keyword, String keywordColumn);

    T findById(Long id);

    boolean create(T entity);

    boolean update(T entity);

    boolean delete(Long id);
}

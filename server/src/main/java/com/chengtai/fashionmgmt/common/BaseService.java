package com.chengtai.fashionmgmt.common;

import com.baomidou.mybatisplus.core.metadata.IPage;

public interface BaseService<T extends BaseEntity> {
    /**
     * 按页码、每页条数和可选关键字分页查询，并按创建时间倒序返回。
     */
    IPage<T> page(long current, long size, String keyword, String keywordColumn);

    /**
     * 根据主键查询一条实体，供详情接口和内部业务复用。
     */
    T findById(Long id);

    /**
     * 新增Base：完成默认值处理、业务校验和唯一性检查后写入数据库。
     */
    boolean create(T entity);

    /**
     * 修改Base：读取原记录、合并非空字段并完成业务校验后更新数据库。
     */
    boolean update(T entity);

    /**
     * 删除Base记录，并返回删除是否成功。
     */
    boolean delete(Long id);
}

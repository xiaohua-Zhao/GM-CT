package com.chengtai.fashionmgmt.module.fin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.chengtai.fashionmgmt.module.fin.entity.FinInvoice;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/** 发票信息持久层。 */
@Mapper
public interface FinInvoiceMapper extends BaseMapper<FinInvoice> {
    /**
     * 执行发票自定义新增 SQL，并返回受影响行数。
     */
    int insertBusiness(@Param("entity") FinInvoice entity, @Param("createdBy") Long createdBy);
    /**
     * 执行发票自定义修改 SQL，并返回受影响行数。
     */
    int updateBusiness(@Param("entity") FinInvoice entity);
    /**
     * 检查发票号码是否重复，可排除当前记录自身。
     */
    int countByInvoiceNo(@Param("invoiceNo") String invoiceNo, @Param("excludeId") Long excludeId);
}

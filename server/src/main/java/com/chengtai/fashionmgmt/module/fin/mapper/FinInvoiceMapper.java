package com.chengtai.fashionmgmt.module.fin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.chengtai.fashionmgmt.module.fin.entity.FinInvoice;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/** 发票信息持久层。 */
@Mapper
public interface FinInvoiceMapper extends BaseMapper<FinInvoice> {
    int insertBusiness(@Param("entity") FinInvoice entity, @Param("createdBy") Long createdBy);
    int updateBusiness(@Param("entity") FinInvoice entity);
    int countByInvoiceNo(@Param("invoiceNo") String invoiceNo, @Param("excludeId") Long excludeId);
}

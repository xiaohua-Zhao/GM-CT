package com.chengtai.fashionmgmt.module.sys.service;

import com.chengtai.fashionmgmt.module.sys.dto.CodeGenerateResponse;
import com.chengtai.fashionmgmt.module.sys.dto.CodeResetRequest;

/** 通用编码生成服务。 */
public interface CodeGeneratorService {
    /**
     * 预览指定业务类型的下一个可用编码，不修改序列值。
     */
    CodeGenerateResponse preview(String bizType);
    /**
     * 在数据库行锁保护下生成并占用一个可用编码。
     */
    CodeGenerateResponse generate(String bizType);
    /**
     * 按指定下一序号重置编码序列，并记录操作原因。
     */
    CodeGenerateResponse reset(Long ruleId, CodeResetRequest request);
}

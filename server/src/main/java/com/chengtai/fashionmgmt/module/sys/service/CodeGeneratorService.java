package com.chengtai.fashionmgmt.module.sys.service;

import com.chengtai.fashionmgmt.module.sys.dto.CodeGenerateResponse;
import com.chengtai.fashionmgmt.module.sys.dto.CodeResetRequest;

/** 通用编码生成服务。 */
public interface CodeGeneratorService {
    CodeGenerateResponse preview(String bizType);
    CodeGenerateResponse generate(String bizType);
    CodeGenerateResponse reset(Long ruleId, CodeResetRequest request);
}

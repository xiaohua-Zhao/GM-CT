package com.chengtai.fashionmgmt.module.sys.dto;

import lombok.Builder;
import lombok.Data;

/** 编码预览或生成结果。 */
@Data
@Builder
public class CodeGenerateResponse {
    private Long ruleId;
    private String bizType;
    private String ruleName;
    private String periodKey;
    private Long currentValue;
    private Long nextValue;
    private String previewCode;
}

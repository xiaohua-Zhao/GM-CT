package com.chengtai.fashionmgmt.module.sys.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/** 编码生成请求。 */
@Data
public class CodeGenerateRequest {
    @NotBlank(message = "业务类型不能为空")
    private String bizType;
}

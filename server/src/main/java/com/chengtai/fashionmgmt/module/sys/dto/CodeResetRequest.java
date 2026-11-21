package com.chengtai.fashionmgmt.module.sys.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/** 手工重置编码序列请求。 */
@Data
public class CodeResetRequest {
    @NotNull(message = "下一次序号不能为空")
    @Min(value = 1, message = "下一次序号必须大于0")
    private Long nextValue;
    private String reason;
}

package com.chengtai.fashionmgmt.module.auth.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 登录请求参数。
 */
@Data
public class LoginRequest {
    @NotBlank(message = "请输入登录账号")
    private String loginName;

    @NotBlank(message = "请输入登录密码")
    private String password;
}

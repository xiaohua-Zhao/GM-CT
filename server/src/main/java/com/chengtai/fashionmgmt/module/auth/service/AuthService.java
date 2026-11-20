package com.chengtai.fashionmgmt.module.auth.service;

import com.chengtai.fashionmgmt.module.auth.dto.LoginRequest;
import com.chengtai.fashionmgmt.module.auth.dto.LoginResponse;
import com.chengtai.fashionmgmt.module.auth.dto.LoginUserInfo;
import jakarta.servlet.http.HttpServletRequest;

/**
 * 登录认证服务。
 */
public interface AuthService {
    LoginResponse login(LoginRequest request, HttpServletRequest httpRequest);

    LoginUserInfo getUserInfo(Long staffId);
}

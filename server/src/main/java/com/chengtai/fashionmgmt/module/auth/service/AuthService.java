package com.chengtai.fashionmgmt.module.auth.service;

import com.chengtai.fashionmgmt.module.auth.dto.LoginRequest;
import com.chengtai.fashionmgmt.module.auth.dto.LoginResponse;
import com.chengtai.fashionmgmt.module.auth.dto.LoginUserInfo;
import jakarta.servlet.http.HttpServletRequest;

/**
 * 登录认证服务。
 */
public interface AuthService {
    /**
     * 校验账号、密码和员工状态，成功后生成 JWT 并记录登录日志。
     */
    LoginResponse login(LoginRequest request, HttpServletRequest httpRequest);

    /**
     * 根据员工 ID 加载部门、角色和权限，组装登录用户信息。
     */
    LoginUserInfo getUserInfo(Long staffId);
}

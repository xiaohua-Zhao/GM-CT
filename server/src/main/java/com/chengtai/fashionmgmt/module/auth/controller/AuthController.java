package com.chengtai.fashionmgmt.module.auth.controller;

import com.chengtai.fashionmgmt.common.Result;
import com.chengtai.fashionmgmt.module.auth.context.LoginUserContext;
import com.chengtai.fashionmgmt.module.auth.dto.LoginRequest;
import com.chengtai.fashionmgmt.module.auth.dto.LoginResponse;
import com.chengtai.fashionmgmt.module.auth.dto.LoginUserInfo;
import com.chengtai.fashionmgmt.module.auth.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 登录认证接口。
 */
@Slf4j
@Tag(name = "登录认证")
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @Operation(summary = "登录", description = "校验员工账号密码并签发 JWT")
    @PostMapping("/login")
    public Result<LoginResponse> login(@Valid @RequestBody LoginRequest request, HttpServletRequest httpRequest) {
        return Result.success("登录成功", authService.login(request, httpRequest));
    }

    @Operation(summary = "当前用户", description = "根据 JWT 查询当前登录员工资料")
    @GetMapping("/me")
    public Result<LoginUserInfo> me() {
        LoginUserContext.LoginUser current = LoginUserContext.get();
        return Result.success(authService.getUserInfo(current.id()));
    }

    @Operation(summary = "退出登录", description = "记录退出操作，客户端同时删除本地 Token")
    @PostMapping("/logout")
    public Result<Void> logout(HttpServletRequest request) {
        LoginUserContext.LoginUser current = LoginUserContext.get();
        log.info("[AUTH][LOGOUT] staffId={} loginName={} ip={} userAgent={}",
                current.id(), current.loginName(), clientIp(request), request.getHeader("User-Agent"));
        return Result.success("退出成功", null);
    }

    private String clientIp(HttpServletRequest request) {
        String forwarded = request.getHeader("X-Forwarded-For");
        if (StringUtils.hasText(forwarded)) {
            return forwarded.split(",")[0].trim();
        }
        String realIp = request.getHeader("X-Real-IP");
        return StringUtils.hasText(realIp) ? realIp : request.getRemoteAddr();
    }
}

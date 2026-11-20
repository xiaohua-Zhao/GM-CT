package com.chengtai.fashionmgmt.module.auth.jwt;

import com.chengtai.fashionmgmt.common.Result;
import com.chengtai.fashionmgmt.module.auth.config.AuthLogProperties;
import com.chengtai.fashionmgmt.module.auth.config.JwtProperties;
import com.chengtai.fashionmgmt.module.auth.context.LoginUserContext;
import com.chengtai.fashionmgmt.module.ct.entity.CtStaff;
import com.chengtai.fashionmgmt.module.ct.mapper.CtStaffMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

/**
 * JWT 请求拦截器，并记录认证失败和可选访问日志。
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthInterceptor implements HandlerInterceptor {
    private static final String ACCESS_START_ATTR = JwtAuthInterceptor.class.getName() + ".start";

    private final JwtService jwtService;
    private final CtStaffMapper staffMapper;
    private final ObjectMapper objectMapper;
    private final JwtProperties jwtProperties;
    private final AuthLogProperties authLogProperties;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (HttpMethod.OPTIONS.matches(request.getMethod())) {
            return true;
        }
        request.setAttribute(ACCESS_START_ATTR, System.nanoTime());
        String authorization = request.getHeader(jwtProperties.getHeaderName());
        String prefix = jwtProperties.getTokenPrefix();
        if (!StringUtils.hasText(authorization) || !authorization.startsWith(prefix)) {
            log.warn("[AUTH][TOKEN_MISSING] uri={} method={} ip={}",
                    request.getRequestURI(), request.getMethod(), clientIp(request));
            writeUnauthorized(response, "请先登录", "AUTH_TOKEN_MISSING");
            return false;
        }

        String token = authorization.substring(prefix.length()).trim();
        try {
            Claims claims = jwtService.parseToken(token);
            Long staffId = Long.valueOf(claims.getSubject());
            CtStaff staff = staffMapper.selectById(staffId);
            if (staff == null || (!Objects.equals(staff.getStatus(), 1) && !Objects.equals(staff.getStatus(), 2))) {
                log.warn("[AUTH][ACCOUNT_DISABLED_AFTER_LOGIN] staffId={} staffCode={} uri={} method={} ip={}",
                        staffId, staff == null ? null : staff.getStaffCode(),
                        request.getRequestURI(), request.getMethod(), clientIp(request));
                writeUnauthorized(response, "账号已停用或不存在", "AUTH_ACCOUNT_DISABLED");
                return false;
            }
            LoginUserContext.set(new LoginUserContext.LoginUser(
                    staff.getId(), staff.getLoginName(), staff.getStaffName()));
            return true;
        } catch (ExpiredJwtException ex) {
            log.warn("[AUTH][TOKEN_EXPIRED] uri={} method={} ip={} expiredAt={}",
                    request.getRequestURI(), request.getMethod(), clientIp(request), ex.getClaims().getExpiration());
            writeUnauthorized(response, "登录已过期，请重新登录", "AUTH_TOKEN_EXPIRED");
            return false;
        } catch (JwtException | IllegalArgumentException ex) {
            if (authLogProperties.isTokenDebugEnabled()) {
                log.warn("[AUTH][TOKEN_INVALID] uri={} method={} ip={} errorType={} error={}",
                        request.getRequestURI(), request.getMethod(), clientIp(request),
                        ex.getClass().getSimpleName(), ex.getMessage());
            } else {
                log.warn("[AUTH][TOKEN_INVALID] uri={} method={} ip={}",
                        request.getRequestURI(), request.getMethod(), clientIp(request));
            }
            writeUnauthorized(response, "登录状态无效，请重新登录", "AUTH_TOKEN_INVALID");
            return false;
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        try {
            LoginUserContext.LoginUser user = LoginUserContext.get();
            if (authLogProperties.isAccessLogEnabled() && user != null && response.getStatus() < 400) {
                Object start = request.getAttribute(ACCESS_START_ATTR);
                long costMs = start instanceof Long ? (System.nanoTime() - (Long) start) / 1_000_000 : -1;
                log.info("[AUTH][ACCESS] staffId={} loginName={} method={} uri={} status={} costMs={} ip={}",
                        user.id(), user.loginName(), request.getMethod(), request.getRequestURI(),
                        response.getStatus(), costMs, clientIp(request));
            }
        } finally {
            LoginUserContext.clear();
        }
    }

    private void writeUnauthorized(HttpServletResponse response, String message, String failureCode) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.setContentType("application/json;charset=UTF-8");
        response.setHeader("X-Auth-Failure-Code", failureCode);
        objectMapper.writeValue(response.getWriter(), Result.fail(401, message));
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

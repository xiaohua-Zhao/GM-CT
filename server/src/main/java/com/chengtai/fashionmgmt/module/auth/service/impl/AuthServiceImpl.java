package com.chengtai.fashionmgmt.module.auth.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.chengtai.fashionmgmt.module.auth.config.AuthLogProperties;
import com.chengtai.fashionmgmt.module.auth.config.JwtProperties;
import com.chengtai.fashionmgmt.module.auth.dto.LoginRequest;
import com.chengtai.fashionmgmt.module.auth.dto.LoginResponse;
import com.chengtai.fashionmgmt.module.auth.dto.LoginUserInfo;
import com.chengtai.fashionmgmt.module.auth.exception.AuthException;
import com.chengtai.fashionmgmt.module.auth.filter.AuthTraceFilter;
import com.chengtai.fashionmgmt.module.auth.jwt.JwtService;
import com.chengtai.fashionmgmt.module.auth.service.AuthService;
import com.chengtai.fashionmgmt.module.ct.entity.CtDept;
import com.chengtai.fashionmgmt.module.ct.entity.CtPage;
import com.chengtai.fashionmgmt.module.ct.entity.CtPermission;
import com.chengtai.fashionmgmt.module.ct.entity.CtRole;
import com.chengtai.fashionmgmt.module.ct.entity.CtStaff;
import com.chengtai.fashionmgmt.module.ct.entity.CtStaffRole;
import com.chengtai.fashionmgmt.module.ct.mapper.CtDeptMapper;
import com.chengtai.fashionmgmt.module.ct.mapper.CtPageMapper;
import com.chengtai.fashionmgmt.module.ct.mapper.CtPermissionMapper;
import com.chengtai.fashionmgmt.module.ct.mapper.CtRoleMapper;
import com.chengtai.fashionmgmt.module.ct.mapper.CtStaffMapper;
import com.chengtai.fashionmgmt.module.ct.mapper.CtStaffRoleMapper;
import com.chengtai.fashionmgmt.module.sys.entity.SysLoginLog;
import com.chengtai.fashionmgmt.module.sys.mapper.SysLoginLogMapper;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 登录认证实现。
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private final CtStaffMapper staffMapper;
    private final CtDeptMapper deptMapper;
    private final CtStaffRoleMapper staffRoleMapper;
    private final CtRoleMapper roleMapper;
    private final CtPermissionMapper permissionMapper;
    private final CtPageMapper pageMapper;
    private final SysLoginLogMapper loginLogMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final JwtProperties jwtProperties;
    private final AuthLogProperties authLogProperties;

    @Override
    public LoginResponse login(LoginRequest request, HttpServletRequest httpRequest) {
        long startNanos = System.nanoTime();
        String loginName = request.getLoginName() == null ? "" : request.getLoginName().trim();

        CtStaff staff = staffMapper.selectOne(new LambdaQueryWrapper<CtStaff>()
                .eq(CtStaff::getLoginName, loginName)
                .last("LIMIT 1"));

        if (staff == null) {
            failLogin(null, null, loginName, "AUTH_ACCOUNT_NOT_FOUND", "登录账号不存在", httpRequest, startNanos);
            throw new AuthException("账号或密码错误");
        }
        if (!isActive(staff.getStatus())) {
            failLogin(staff.getId(), staff.getStaffCode(), loginName, "AUTH_ACCOUNT_DISABLED", "员工状态不可登录", httpRequest, startNanos);
            throw new AuthException("当前账号已被停用或离职");
        }
        if (!StringUtils.hasText(staff.getPasswordHash())) {
            failLogin(staff.getId(), staff.getStaffCode(), loginName, "AUTH_PASSWORD_NOT_SET", "尚未设置登录密码", httpRequest, startNanos);
            throw new AuthException("当前账号尚未设置登录密码，请联系管理员");
        }

        boolean passwordMatched;
        try {
            passwordMatched = passwordEncoder.matches(request.getPassword(), staff.getPasswordHash());
        } catch (IllegalArgumentException ex) {
            failLogin(staff.getId(), staff.getStaffCode(), loginName, "AUTH_PASSWORD_HASH_INVALID", "密码密文格式异常", httpRequest, startNanos);
            throw new AuthException("账号或密码错误");
        }
        if (!passwordMatched) {
            failLogin(staff.getId(), staff.getStaffCode(), loginName, "AUTH_PASSWORD_MISMATCH", "登录密码错误", httpRequest, startNanos);
            throw new AuthException("账号或密码错误");
        }

        List<CtRole> roles = loadRoles(staff.getId());
        LoginUserInfo user = buildUserInfo(staff, roles);
        List<String> permissions = buildPermissions(staff, roles);
        String token = jwtService.generateToken(staff);
        LocalDateTime tokenExpireAt = LocalDateTime.now().plusSeconds(jwtProperties.getExpirationSeconds());

        updateLastLogin(staff.getId());
        long costMs = costMs(startNanos);
        writeLoginLog(staff.getId(), staff.getStaffCode(), loginName, 1, "登录成功",
                null, httpRequest, costMs, tokenExpireAt);
        log.info("[AUTH][LOGIN_SUCCESS] staffId={} staffCode={} loginName={} ip={} clientType={} costMs={} tokenExpireAt={}",
                staff.getId(), staff.getStaffCode(), loginName, clientIp(httpRequest), clientType(httpRequest),
                costMs, tokenExpireAt.format(DATE_TIME_FORMATTER));

        return LoginResponse.builder()
                .accessToken(token)
                .tokenType("Bearer")
                .expiresIn(jwtProperties.getExpirationSeconds())
                .user(user)
                .permissions(permissions)
                .build();
    }

    @Override
    public LoginUserInfo getUserInfo(Long staffId) {
        CtStaff staff = staffMapper.selectById(staffId);
        if (staff == null || !isActive(staff.getStatus())) {
            throw new AuthException("登录状态已失效");
        }
        return buildUserInfo(staff, loadRoles(staffId));
    }

    private void failLogin(Long staffId, String staffCode, String loginName, String failureCode,
                           String message, HttpServletRequest request, long startNanos) {
        long costMs = costMs(startNanos);
        writeLoginLog(staffId, staffCode, loginName, 0, message, failureCode, request, costMs, null);
        log.warn("[AUTH][LOGIN_FAILED] loginName={} failureCode={} ip={} clientType={} costMs={}",
                loginName, failureCode, clientIp(request), clientType(request), costMs);
    }

    private boolean isActive(Integer status) {
        return Objects.equals(status, 1) || Objects.equals(status, 2);
    }

    private List<CtRole> loadRoles(Long staffId) {
        List<CtStaffRole> relations = staffRoleMapper.selectList(new LambdaQueryWrapper<CtStaffRole>()
                .eq(CtStaffRole::getStaffId, staffId)
                .eq(CtStaffRole::getStatus, 1));
        if (relations.isEmpty()) {
            return Collections.emptyList();
        }
        List<Long> roleIds = relations.stream().map(CtStaffRole::getRoleId).distinct().toList();
        return roleMapper.selectBatchIds(roleIds).stream()
                .filter(role -> Objects.equals(role.getStatus(), 1))
                .toList();
    }

    private LoginUserInfo buildUserInfo(CtStaff staff, List<CtRole> roles) {
        String deptName = null;
        if (staff.getDeptId() != null) {
            CtDept dept = deptMapper.selectById(staff.getDeptId());
            deptName = dept == null ? null : dept.getDeptName();
        }
        return LoginUserInfo.builder()
                .id(staff.getId())
                .staffCode(staff.getStaffCode())
                .staffName(staff.getStaffName())
                .loginName(staff.getLoginName())
                .deptId(staff.getDeptId())
                .deptName(deptName)
                .post(staff.getPost())
                .roles(roles.stream().map(CtRole::getRoleCode).toList())
                .roleNames(roles.stream().map(CtRole::getRoleName).toList())
                .build();
    }

    private List<String> buildPermissions(CtStaff staff, List<CtRole> roles) {
        if (roles.stream().anyMatch(role -> "ADMIN".equals(role.getRoleCode()))) {
            return List.of("*");
        }
        Set<Long> roleIds = roles.stream().map(CtRole::getId).collect(Collectors.toSet());
        List<CtPermission> permissions = permissionMapper.selectList(new LambdaQueryWrapper<CtPermission>()
                .eq(CtPermission::getOperState, 1));
        if (permissions.isEmpty()) {
            return Collections.emptyList();
        }

        Map<Long, CtPage> pageMap = pageMapper.selectList(new LambdaQueryWrapper<CtPage>()
                        .eq(CtPage::getStatus, 1))
                .stream()
                .collect(Collectors.toMap(CtPage::getId, Function.identity(), (left, right) -> left));

        LinkedHashSet<String> result = new LinkedHashSet<>();
        for (CtPermission permission : permissions) {
            boolean matched =
                    (Objects.equals(permission.getSubjectType(), 1) && roleIds.contains(permission.getSubjectId()))
                    || (Objects.equals(permission.getSubjectType(), 2) && Objects.equals(permission.getSubjectId(), staff.getDeptId()))
                    || (Objects.equals(permission.getSubjectType(), 3) && Objects.equals(permission.getSubjectId(), staff.getId()));
            if (!matched) {
                continue;
            }
            CtPage page = pageMap.get(permission.getPageId());
            if (page != null) {
                result.add(page.getRoutePath() + ":" + permission.getActionCode());
            }
        }
        return new ArrayList<>(result);
    }

    private void updateLastLogin(Long staffId) {
        CtStaff update = new CtStaff();
        update.setId(staffId);
        update.setLastLoginAt(LocalDateTime.now().format(DATE_TIME_FORMATTER));
        staffMapper.updateById(update);
    }

    private void writeLoginLog(Long staffId, String staffCode, String loginName, int status, String message,
                               String failureCode, HttpServletRequest request, long costMs, LocalDateTime tokenExpireAt) {
        if (!authLogProperties.isLoginLogEnabled()) {
            return;
        }
        try {
            SysLoginLog loginLog = new SysLoginLog();
            loginLog.setStaffId(staffId);
            loginLog.setTraceId(truncate(MDC.get(AuthTraceFilter.TRACE_MDC_KEY), 40));
            loginLog.setStaffCode(truncate(staffCode, 20));
            loginLog.setLoginName(truncate(loginName, 50));
            loginLog.setLoginStatus(status);
            loginLog.setIpAddress(truncate(clientIp(request), 64));
            loginLog.setClientType(truncate(clientType(request), 20));
            loginLog.setUserAgent(truncate(request.getHeader("User-Agent"), 500));
            loginLog.setMessage(truncate(message, 200));
            loginLog.setFailureCode(truncate(failureCode, 30));
            loginLog.setCostMs((int) Math.min(costMs, Integer.MAX_VALUE));
            loginLog.setTokenExpireAt(tokenExpireAt);
            loginLog.setCreatedAt(LocalDateTime.now());
            loginLogMapper.insert(loginLog);
        } catch (Exception ex) {
            log.warn("[AUTH][LOGIN_LOG_WRITE_FAILED] error={}", ex.getMessage());
        }
    }

    private long costMs(long startNanos) {
        return (System.nanoTime() - startNanos) / 1_000_000;
    }

    private String clientType(HttpServletRequest request) {
        String ua = request.getHeader("User-Agent");
        if (ua == null) {
            return "UNKNOWN";
        }
        String lower = ua.toLowerCase();
        if (lower.contains("micromessenger")) {
            return "WECHAT_MINI";
        }
        if (lower.contains("okhttp") || lower.contains("android") || lower.contains("iphone")) {
            return "APP";
        }
        if (lower.contains("postman")) {
            return "API_CLIENT";
        }
        return "PC_WEB";
    }

    private String clientIp(HttpServletRequest request) {
        String forwarded = request.getHeader("X-Forwarded-For");
        if (StringUtils.hasText(forwarded)) {
            return forwarded.split(",")[0].trim();
        }
        String realIp = request.getHeader("X-Real-IP");
        return StringUtils.hasText(realIp) ? realIp : request.getRemoteAddr();
    }

    private String truncate(String value, int maxLength) {
        if (value == null) {
            return null;
        }
        return value.length() <= maxLength ? value : value.substring(0, maxLength);
    }
}

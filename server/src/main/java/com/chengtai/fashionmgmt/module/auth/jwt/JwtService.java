package com.chengtai.fashionmgmt.module.auth.jwt;

import com.chengtai.fashionmgmt.module.auth.config.JwtProperties;
import com.chengtai.fashionmgmt.module.ct.entity.CtStaff;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;

/**
 * JWT 签发和解析服务。
 */
@Service
@RequiredArgsConstructor
public class JwtService {
    private final JwtProperties properties;

    /**
     * 为登录员工签发 Token。
     */
    public String generateToken(CtStaff staff) {
        Instant now = Instant.now();
        Instant expiresAt = now.plusSeconds(properties.getExpirationSeconds());
        return Jwts.builder()
                .issuer(properties.getIssuer())
                .subject(String.valueOf(staff.getId()))
                .claim("staffCode", staff.getStaffCode())
                .claim("staffName", staff.getStaffName())
                .claim("loginName", staff.getLoginName())
                .issuedAt(Date.from(now))
                .expiration(Date.from(expiresAt))
                .signWith(signingKey())
                .compact();
    }

    /**
     * 验证并解析 Token，签名、有效期或签发方不合法时抛出 JWT 异常。
     */
    public Claims parseToken(String token) {
        return Jwts.parser()
                .verifyWith(signingKey())
                .requireIssuer(properties.getIssuer())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    /**
     * 根据配置密钥创建 HMAC 签名密钥。
     */
    private SecretKey signingKey() {
        byte[] bytes = properties.getSecret().getBytes(StandardCharsets.UTF_8);
        if (bytes.length < 32) {
            throw new IllegalStateException("JWT secret 至少需要 32 个字节");
        }
        return Keys.hmacShaKeyFor(bytes);
    }
}

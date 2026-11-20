package com.chengtai.fashionmgmt.module.auth.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * JWT 与认证请求配置。
 */
@Data
@Component
@ConfigurationProperties(prefix = "app.jwt")
public class JwtProperties {
    private String secret;
    private long expirationSeconds = 28800;
    private String issuer = "chengtai-fashion-mgmt";
    private String headerName = "Authorization";
    private String tokenPrefix = "Bearer ";
}

package com.chengtai.fashionmgmt.module.auth.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 认证日志和审计配置。
 */
@Data
@Component
@ConfigurationProperties(prefix = "app.auth")
public class AuthLogProperties {
    private boolean loginLogEnabled = true;
    private boolean accessLogEnabled = false;
    private boolean tokenDebugEnabled = true;
    private int logRetentionDays = 90;
    private String logCleanupCron = "0 0 3 * * ?";
}

package com.chengtai.fashionmgmt.common.web;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * 接口入参和出参日志配置。
 */
@Data
@Component
@ConfigurationProperties(prefix = "app.api-log")
public class ApiLogProperties {
    private boolean enabled = true;
    private boolean includeResponse = true;
    private int maxPayloadLength = 8000;
    private long slowThresholdMs = 1000;
    private List<String> excludedPaths = new ArrayList<>(List.of("/api/health", "/actuator"));
    private List<String> sensitiveKeys = new ArrayList<>(List.of(
            "password", "passwd", "pwd", "passwordhash", "oldpassword", "newpassword",
            "confirmpassword", "token", "accesstoken", "refreshtoken", "authorization",
            "secret", "jwtsecret", "privatekey", "filecontent", "base64",
            "phone", "mobile", "idcard", "bankaccount", "accountno", "cardno"
    ));
}

package com.chengtai.fashionmgmt.module.system.controller;

import com.chengtai.fashionmgmt.common.Result;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class HealthController {
    private final JdbcTemplate jdbcTemplate;

    @Value("${spring.application.name}")
    private String applicationName;

    @Value("${file.attachment.root:./attachments}")
    private String attachmentRoot;

    @GetMapping("/health")
    public Result<Map<String, Object>> health() {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("application", applicationName);
        data.put("time", LocalDateTime.now().toString());
        data.put("attachmentRoot", attachmentRoot);
        data.put("logFile", "logs/chengtai-fashion-mgmt.log");
        try {
            Integer value = jdbcTemplate.queryForObject("SELECT 1", Integer.class);
            data.put("database", value != null && value == 1 ? "UP" : "DOWN");
            data.put("databaseName", "user");
            log.info("健康检查成功，数据库连接正常");
        } catch (Exception ex) {
            data.put("database", "DOWN");
            data.put("databaseError", ex.getMessage());
            log.error("健康检查失败，数据库连接异常", ex);
        }
        return Result.success("系统运行正常", data);
    }
}

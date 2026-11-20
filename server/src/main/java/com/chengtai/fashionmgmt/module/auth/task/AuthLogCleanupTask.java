package com.chengtai.fashionmgmt.module.auth.task;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.chengtai.fashionmgmt.module.auth.config.AuthLogProperties;
import com.chengtai.fashionmgmt.module.sys.entity.SysLoginLog;
import com.chengtai.fashionmgmt.module.sys.mapper.SysLoginLogMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * 按配置天数清理历史登录日志。
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class AuthLogCleanupTask {
    private final AuthLogProperties properties;
    private final SysLoginLogMapper loginLogMapper;

    @Scheduled(cron = "${app.auth.log-cleanup-cron:0 0 3 * * ?}")
    public void cleanupExpiredLoginLogs() {
        int days = Math.max(properties.getLogRetentionDays(), 1);
        LocalDateTime cutoff = LocalDateTime.now().minusDays(days);
        int deleted = loginLogMapper.delete(new LambdaQueryWrapper<SysLoginLog>()
                .lt(SysLoginLog::getCreatedAt, cutoff));
        log.info("[AUTH][LOG_CLEANUP] retentionDays={} cutoff={} deleted={}", days, cutoff, deleted);
    }
}

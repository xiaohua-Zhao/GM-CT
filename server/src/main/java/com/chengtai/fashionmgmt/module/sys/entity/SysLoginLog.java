package com.chengtai.fashionmgmt.module.sys.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 登录日志实体，对应表 t_sys_login_log。
 */
@Data
@TableName("ct_gm.t_sys_login_log")
public class SysLoginLog {
    @TableId(value = "login_log_id", type = IdType.AUTO)
    private Long id;
    private Long staffId;
    private String traceId;
    private String staffCode;
    private String loginName;
    private Integer loginStatus;
    private String ipAddress;
    private String clientType;
    private String userAgent;
    private String message;
    private String failureCode;
    private Integer costMs;
    private LocalDateTime tokenExpireAt;
    private LocalDateTime createdAt;
}

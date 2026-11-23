package com.chengtai.fashionmgmt.module.auth.exception;

/**
 * 登录认证异常。
 */
public class AuthException extends RuntimeException {
    /**
     * 创建带错误提示的AuthException。
     */
    public AuthException(String message) {
        super(message);
    }
}

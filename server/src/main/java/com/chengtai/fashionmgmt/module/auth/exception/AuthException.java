package com.chengtai.fashionmgmt.module.auth.exception;

/**
 * 登录认证异常。
 */
public class AuthException extends RuntimeException {
    public AuthException(String message) {
        super(message);
    }
}

package com.chengtai.fashionmgmt.common;

/** 业务校验异常。 */
public class BusinessValidationException extends RuntimeException {
    /**
     * 创建带错误提示的BusinessValidationException。
     */
    public BusinessValidationException(String message) { super(message); }
}

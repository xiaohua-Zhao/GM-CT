package com.chengtai.fashionmgmt.common;

/** 业务校验异常。 */
public class BusinessValidationException extends RuntimeException {
    public BusinessValidationException(String message) { super(message); }
}

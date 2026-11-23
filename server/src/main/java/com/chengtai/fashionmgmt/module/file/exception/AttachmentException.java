package com.chengtai.fashionmgmt.module.file.exception;

public class AttachmentException extends RuntimeException {
    /**
     * 创建带错误提示的AttachmentException。
     */
    public AttachmentException(String message) {
        super(message);
    }

    /**
     * 创建带错误提示的AttachmentException。
     */
    public AttachmentException(String message, Throwable cause) {
        super(message, cause);
    }
}

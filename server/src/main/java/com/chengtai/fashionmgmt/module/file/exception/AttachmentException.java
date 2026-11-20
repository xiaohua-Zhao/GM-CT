package com.chengtai.fashionmgmt.module.file.exception;

public class AttachmentException extends RuntimeException {
    public AttachmentException(String message) {
        super(message);
    }

    public AttachmentException(String message, Throwable cause) {
        super(message, cause);
    }
}

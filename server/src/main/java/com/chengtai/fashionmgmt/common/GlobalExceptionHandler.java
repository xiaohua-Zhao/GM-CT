package com.chengtai.fashionmgmt.common;

import com.chengtai.fashionmgmt.module.file.exception.AttachmentException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(AttachmentException.class)
    public Result<Void> handleAttachmentException(AttachmentException ex) {
        log.warn("附件处理失败: {}", ex.getMessage());
        return Result.fail(400, ex.getMessage());
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public Result<Void> handleUploadSize(MaxUploadSizeExceededException ex) {
        log.warn("上传文件超过大小限制", ex);
        return Result.fail(413, "上传文件超过大小限制");
    }

    @ExceptionHandler(Exception.class)
    public Result<Void> handleException(Exception ex) {
        log.error("系统异常", ex);
        return Result.fail(500, "系统异常，请联系管理员");
    }
}

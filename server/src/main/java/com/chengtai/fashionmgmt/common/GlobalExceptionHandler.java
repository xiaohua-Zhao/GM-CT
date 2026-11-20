package com.chengtai.fashionmgmt.common;

import com.chengtai.fashionmgmt.module.auth.exception.AuthException;
import com.chengtai.fashionmgmt.module.file.exception.AttachmentException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(AuthException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public Result<Void> handleAuthException(AuthException ex) {
        log.warn("登录认证失败: {}", ex.getMessage());
        return Result.fail(401, ex.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result<Void> handleValidationException(MethodArgumentNotValidException ex) {
        String message = ex.getBindingResult().getFieldErrors().stream()
                .findFirst()
                .map(error -> error.getDefaultMessage())
                .orElse("请求参数不合法");
        return Result.fail(400, message);
    }

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

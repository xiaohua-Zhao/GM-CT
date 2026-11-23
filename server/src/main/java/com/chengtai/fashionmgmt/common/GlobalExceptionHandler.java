package com.chengtai.fashionmgmt.common;

import com.chengtai.fashionmgmt.module.auth.exception.AuthException;
import com.chengtai.fashionmgmt.common.BusinessValidationException;
import com.chengtai.fashionmgmt.module.file.exception.AttachmentException;
import com.chengtai.fashionmgmt.module.sys.code.CodeGenerationException;
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

    /**
     * 将认证异常转换为统一的未授权响应。
     */
    @ExceptionHandler(AuthException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public Result<Void> handleAuthException(AuthException ex) {
        log.warn("登录认证失败: {}", ex.getMessage());
        return Result.fail(401, ex.getMessage());
    }

    /**
     * 汇总参数校验错误并返回 400 响应。
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result<Void> handleValidationException(MethodArgumentNotValidException ex) {
        String message = ex.getBindingResult().getFieldErrors().stream()
                .findFirst()
                .map(error -> error.getDefaultMessage())
                .orElse("请求参数不合法");
        return Result.fail(400, message);
    }

    /**
     * 将业务校验异常转换为用户可读的业务错误响应。
     */
    @ExceptionHandler(BusinessValidationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result<Void> handleBusinessValidation(BusinessValidationException ex) {
        log.warn("业务校验失败: {}", ex.getMessage());
        return Result.fail(400, ex.getMessage());
    }

    /**
     * 将编码生成异常转换为可操作的业务提示。
     */
    @ExceptionHandler(CodeGenerationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result<Void> handleCodeGenerationException(CodeGenerationException ex) {
        log.warn("编码生成失败: {}", ex.getMessage());
        return Result.fail(400, ex.getMessage());
    }

    /**
     * 将附件上传和存储异常转换为统一错误响应。
     */
    @ExceptionHandler(AttachmentException.class)
    public Result<Void> handleAttachmentException(AttachmentException ex) {
        log.warn("附件处理失败: {}", ex.getMessage());
        return Result.fail(400, ex.getMessage());
    }

    /**
     * 处理上传文件超过限制时的大小异常。
     */
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public Result<Void> handleUploadSize(MaxUploadSizeExceededException ex) {
        log.warn("上传文件超过大小限制", ex);
        return Result.fail(413, "上传文件超过大小限制");
    }

    /**
     * 兜底处理未预期异常，记录日志并返回统一 500 响应。
     */
    @ExceptionHandler(Exception.class)
    public Result<Void> handleException(Exception ex) {
        log.error("系统异常", ex);
        return Result.fail(500, "系统异常，请联系管理员");
    }
}

package com.chengtai.fashionmgmt.module.sys.code;

/** 编码生成业务异常。 */
public class CodeGenerationException extends RuntimeException {
    /**
     * 创建带错误提示的CodeGenerationException。
     */
    public CodeGenerationException(String message) {
        super(message);
    }
}

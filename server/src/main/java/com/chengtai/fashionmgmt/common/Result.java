package com.chengtai.fashionmgmt.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Result<T> {
    private int code;
    private String message;
    private T data;
    private LocalDateTime timestamp;

    public static <T> Result<T> success(T data) {
        return new Result<>(0, "success", data, LocalDateTime.now());
    }

    public static <T> Result<T> success(String message, T data) {
        return new Result<>(0, message, data, LocalDateTime.now());
    }

    public static <T> Result<T> fail(int code, String message) {
        return new Result<>(code, message, null, LocalDateTime.now());
    }
}

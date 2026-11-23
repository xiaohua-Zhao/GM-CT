package com.chengtai.fashionmgmt.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/** 业务日志 JSON 序列化工具，限制单条日志最大长度。 */
@Component
@RequiredArgsConstructor
public class BusinessLogJson {
    private static final int MAX_LENGTH = 12000;
    private final ObjectMapper objectMapper;

    /**
     * 将业务对象序列化为日志 JSON；失败时返回可读降级信息。
     */
    public String toJson(Object value) {
        if (value == null) return "null";
        try {
            String json = objectMapper.writeValueAsString(value);
            return json.length() <= MAX_LENGTH ? json : json.substring(0, MAX_LENGTH) + "...<truncated>";
        } catch (Exception ex) {
            return "<json-serialize-error " + ex.getClass().getSimpleName() + ">";
        }
    }
}

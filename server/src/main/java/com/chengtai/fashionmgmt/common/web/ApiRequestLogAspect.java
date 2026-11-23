package com.chengtai.fashionmgmt.common.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Array;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * 统一记录项目 Controller 的入参和出参。
 * <p>
 * 敏感字段会被脱敏，文件只记录摘要，超长内容按配置截断。
 */
@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class ApiRequestLogAspect {
    private final ApiLogProperties properties;
    private final ObjectMapper objectMapper;

    /**
     * 环绕拦截 API 请求，记录 URL、入参、出参、耗时和异常。
     */
    @Around("within(@org.springframework.web.bind.annotation.RestController *) || execution(public * com.chengtai.fashionmgmt.common.BaseCrudController.*(..))")
    public Object logApi(ProceedingJoinPoint joinPoint) throws Throwable {
        HttpServletRequest request = currentRequest();
        if (!properties.isEnabled() || isExcluded(request)) {
            return joinPoint.proceed();
        }

        long startNanos = System.nanoTime();
        String method = request == null ? "UNKNOWN" : request.getMethod();
        String uri = request == null ? "UNKNOWN" : request.getRequestURI();
        String handler = joinPoint.getSignature().toShortString();
        String arguments = serializeArguments(joinPoint);

        log.info("[API][REQUEST] method={} uri={} handler={} args={}", method, uri, handler, arguments);

        try {
            Object result = joinPoint.proceed();
            long costMs = (System.nanoTime() - startNanos) / 1_000_000;
            if (properties.isIncludeResponse()) {
                log.info("[API][RESPONSE] method={} uri={} handler={} costMs={} result={}",
                        method, uri, handler, costMs, serialize(result));
            } else {
                log.info("[API][RESPONSE] method={} uri={} handler={} costMs={} result=<response logging disabled>",
                        method, uri, handler, costMs);
            }
            if (costMs >= properties.getSlowThresholdMs()) {
                log.warn("[API][SLOW] method={} uri={} handler={} costMs={} thresholdMs={}",
                        method, uri, handler, costMs, properties.getSlowThresholdMs());
            }
            return result;
        } catch (Throwable ex) {
            long costMs = (System.nanoTime() - startNanos) / 1_000_000;
            log.error("[API][ERROR] method={} uri={} handler={} costMs={} errorType={} error={} args={}",
                    method, uri, handler, costMs, ex.getClass().getSimpleName(), ex.getMessage(), arguments);
            throw ex;
        }
    }

    /**
     * 从 Spring 请求上下文中获取当前 HTTP 请求。
     */
    private HttpServletRequest currentRequest() {
        if (RequestContextHolder.getRequestAttributes() instanceof ServletRequestAttributes attributes) {
            return attributes.getRequest();
        }
        return null;
    }

    /**
     * 判断当前请求路径是否属于日志排除范围。
     */
    private boolean isExcluded(HttpServletRequest request) {
        if (request == null || properties.getExcludedPaths() == null) {
            return false;
        }
        return properties.getExcludedPaths().stream()
                .filter(path -> path != null && !path.isBlank())
                .anyMatch(path -> request.getRequestURI().startsWith(path));
    }

    /**
     * 提取并脱敏 Controller 方法参数，生成可检索的请求日志。
     */
    private String serializeArguments(ProceedingJoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        if (args == null || args.length == 0) {
            return "{}";
        }
        String[] parameterNames = joinPoint.getSignature() instanceof MethodSignature signature
                ? signature.getParameterNames()
                : null;
        Map<String, Object> namedArgs = new LinkedHashMap<>();
        for (int i = 0; i < args.length; i++) {
            String name = parameterNames != null && i < parameterNames.length && parameterNames[i] != null
                    ? parameterNames[i]
                    : "arg" + i;
            namedArgs.put(name, isSensitive(name) ? "***" : sanitize(args[i], 0));
        }
        return serialize(namedArgs);
    }

    /**
     * 将对象安全序列化为受长度限制的 JSON 字符串。
     */
    private String serialize(Object value) {
        try {
            String json = objectMapper.writeValueAsString(sanitize(value, 0));
            return truncate(json);
        } catch (Exception ex) {
            return "<serialize failed: " + ex.getClass().getSimpleName() + ">";
        }
    }

    /**
     * 递归脱敏密码、Token、密钥等敏感字段。
     */
    private Object sanitize(Object value, int depth) {
        if (value == null) {
            return null;
        }
        if (depth >= 6) {
            return "<max-depth>";
        }
        if (value instanceof MultipartFile file) {
            return fileSummary(file);
        }
        if (value instanceof MultipartFile[] files) {
            List<Map<String, Object>> list = new ArrayList<>();
            for (MultipartFile file : files) {
                list.add(fileSummary(file));
            }
            return list;
        }
        if (value instanceof byte[] bytes) {
            return "<byte-array size=" + bytes.length + ">";
        }
        if (value instanceof ServletRequest || value instanceof ServletResponse
                || value instanceof BindingResult || value instanceof Principal
                || value instanceof InputStream || value instanceof OutputStream) {
            return "<ignored " + value.getClass().getSimpleName() + ">";
        }
        if (value instanceof CharSequence || value instanceof Number || value instanceof Boolean
                || value instanceof Enum<?>) {
            return value;
        }
        if (value instanceof Map<?, ?> map) {
            Map<String, Object> result = new LinkedHashMap<>();
            map.forEach((key, mapValue) -> {
                String name = String.valueOf(key);
                result.put(name, isSensitive(name) ? "***" : sanitize(mapValue, depth + 1));
            });
            return result;
        }
        if (value instanceof Collection<?> collection) {
            List<Object> result = new ArrayList<>();
            int index = 0;
            for (Object item : collection) {
                if (index++ >= 200) {
                    result.add("<collection truncated>");
                    break;
                }
                result.add(sanitize(item, depth + 1));
            }
            return result;
        }
        if (value.getClass().isArray()) {
            int length = Array.getLength(value);
            List<Object> result = new ArrayList<>();
            for (int i = 0; i < length; i++) {
                result.add(sanitize(Array.get(value, i), depth + 1));
            }
            return result;
        }
        try {
            Map<?, ?> converted = objectMapper.convertValue(value, Map.class);
            return sanitize(converted, depth + 1);
        } catch (Exception ex) {
            return "<" + value.getClass().getSimpleName() + ">";
        }
    }

    /**
     * 把上传文件转换为文件名、大小和类型摘要，避免日志输出文件内容。
     */
    private Map<String, Object> fileSummary(MultipartFile file) {
        Map<String, Object> summary = new LinkedHashMap<>();
        summary.put("name", file.getOriginalFilename());
        summary.put("size", file.getSize());
        summary.put("contentType", file.getContentType());
        return summary;
    }

    /**
     * 判断字段名是否属于需要脱敏的敏感字段。
     */
    private boolean isSensitive(String key) {
        String normalized = key.replace("_", "").toLowerCase(Locale.ROOT);
        return properties.getSensitiveKeys().stream()
                .map(item -> item.replace("_", "").toLowerCase(Locale.ROOT))
                .anyMatch(normalized::contains);
    }

    /**
     * 将字符串截断到指定长度，避免写入日志表时超长。
     */
    private String truncate(String value) {
        if (value == null || value.length() <= properties.getMaxPayloadLength()) {
            return value;
        }
        return value.substring(0, properties.getMaxPayloadLength())
                + "...<truncated totalLength=" + value.length() + ">";
    }
}

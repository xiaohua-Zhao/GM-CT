package com.chengtai.fashionmgmt.module.auth.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.MDC;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;

/**
 * 为每个请求建立 TraceId，便于从日志和数据库定位一次完整请求。
 */
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class AuthTraceFilter extends OncePerRequestFilter {
    public static final String TRACE_HEADER = "X-Request-Id";
    public static final String TRACE_MDC_KEY = "traceId";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String traceId = normalize(request.getHeader(TRACE_HEADER));
        MDC.put(TRACE_MDC_KEY, traceId);
        response.setHeader(TRACE_HEADER, traceId);
        try {
            filterChain.doFilter(request, response);
        } finally {
            MDC.remove(TRACE_MDC_KEY);
        }
    }

    private String normalize(String value) {
        if (StringUtils.hasText(value) && value.length() <= 40 && value.matches("[A-Za-z0-9._-]+")) {
            return value;
        }
        return UUID.randomUUID().toString().replace("-", "");
    }
}

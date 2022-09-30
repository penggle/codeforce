package com.penglecode.codeforce.common.web.servlet.support;

import com.penglecode.codeforce.common.consts.GlobalConstants;
import org.slf4j.MDC;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;

/**
 * 基于SLF4j的MDC机制的HTTP接口请求链路追踪过滤器
 * 其中需要在logback|log4j2的日志行PATTERN中加入traceId参数
 *
 * @author pengpeng
 * @version 1.0.0
 */
public class HttpApiTraceFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        try {
            String traceId = UUID.randomUUID().toString().replace("-", "");
            MDC.put(GlobalConstants.DEFAULT_MDC_TRACE_ID_KEY, traceId);
            chain.doFilter(request, response);
        } finally {
            MDC.clear();
        }
    }

}

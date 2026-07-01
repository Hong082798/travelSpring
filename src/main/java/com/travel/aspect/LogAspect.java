package com.travel.aspect;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import java.util.LinkedHashMap;
import java.util.Map;

@Aspect
@Component
public class LogAspect {

    private static final Logger log = LoggerFactory.getLogger(LogAspect.class);

    private final ObjectMapper objectMapper;

    public LogAspect(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Around("execution(* com.travel.controller..*(..))")
    public Object log(ProceedingJoinPoint pjp) throws Throwable {
        long start = System.currentTimeMillis();

        ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attrs != null ? attrs.getRequest() : null;

        MethodSignature signature = (MethodSignature) pjp.getSignature();
        String[] paramNames = signature.getParameterNames();
        Object[] args = pjp.getArgs();

        Map<String, Object> params = new LinkedHashMap<>();
        if (paramNames != null) {
            for (int i = 0; i < paramNames.length; i++) {
                if (!isSkippable(args[i])) {
                    params.put(paramNames[i], args[i]);
                }
            }
        }

        String httpMethod = request != null ? request.getMethod() : "-";
        String uri = request != null ? request.getRequestURI() : "-";
        String ip = resolveClientIp(request);

        log.debug("┌─────────────────────────────────────────────────");
        log.debug("│ {} {} (来自 {})", httpMethod, uri, ip);
        log.debug("│ 接口方法: {}.{}", pjp.getTarget().getClass().getSimpleName(), signature.getName());
        log.debug("│ 请求参数: {}", toJson(params));

        try {
            Object result = pjp.proceed();
            log.debug("│ 响应结果: {}", toJson(result));
            log.debug("└─────────────────────────── 耗时 {}ms", System.currentTimeMillis() - start);
            return result;
        } catch (Throwable ex) {
            log.debug("│ 抛出异常: {} - {}", ex.getClass().getSimpleName(), ex.getMessage());
            log.debug("└─────────────────────────── 耗时 {}ms", System.currentTimeMillis() - start);
            throw ex;
        }
    }

    private boolean isSkippable(Object arg) {
        return arg instanceof HttpServletRequest
                || arg instanceof HttpServletResponse
                || arg instanceof MultipartFile;
    }

    private String toJson(Object obj) {
        if (obj == null) return "null";
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (Exception e) {
            return obj.toString();
        }
    }

    private String resolveClientIp(HttpServletRequest request) {
        if (request == null) return "-";
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isBlank() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }
}

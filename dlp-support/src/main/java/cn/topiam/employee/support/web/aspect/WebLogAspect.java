/*
 * ULP - United Login Platform
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.topiam.employee.support.web.aspect;

import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import cn.topiam.employee.support.context.ServletContextService;
import cn.topiam.employee.support.security.util.SecurityUtils;
import cn.topiam.employee.support.trace.TraceUtils;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Aspect
@Component
public class WebLogAspect {

    private static final Logger log = LoggerFactory.getLogger(WebLogAspect.class);

    @Pointcut("execution(public * cn.topiam.employee..controller..*.*(..))")
    public void webLog() {
    }

    @Around("webLog()")
    public Object doAround(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        Object result = null;
        Throwable throwable = null;
        try {
            result = proceedingJoinPoint.proceed();
            return result;
        } catch (Throwable e) {
            throwable = e;
            throw e;
        } finally {
            this.handleLog(proceedingJoinPoint, startTime, result, throwable);
        }
    }

    private void handleLog(ProceedingJoinPoint proceedingJoinPoint, long startTime, Object result,
                           Throwable throwable) {
        try {
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder
                .getRequestAttributes();
            if (Objects.isNull(attributes)) {
                return;
            }

            HttpServletRequest request = attributes.getRequest();
            MethodSignature signature = (MethodSignature) proceedingJoinPoint.getSignature();
            Method method = signature.getMethod();
            String methodStr = request.getMethod() + " " + request.getRequestURI();
            String params = this.getRequestParams(request, proceedingJoinPoint);
            long executeTime = System.currentTimeMillis() - startTime;
            String userId = SecurityUtils.getCurrentUserId();
            String clientIP = this.getClientIP(request);
            String userAgent = request.getHeader("User-Agent");
            String traceId = TraceUtils.get();
            Map<String, Object> logMap = new HashMap<>();
            logMap.put("method", methodStr);
            logMap.put("params", params);
            logMap.put("executeTime", executeTime);
            logMap.put("userId", userId);
            logMap.put("clientIP", clientIP);
            logMap.put("userAgent", userAgent);
            logMap.put("traceId", traceId);
            logMap.put("timestamp", LocalDateTime.now());
            logMap.put("result", result);
            logMap.put("error", throwable);
            logMap.put("methodName", method.getName());
            if (throwable != null) {
                log.error("WebLog error: {}", logMap, throwable);
            } else {
                log.info("WebLog: {}", logMap);
            }
        } catch (Exception e) {
            log.error("WebLogAspect handleLog error", e);
        }
    }

    private String getRequestParams(HttpServletRequest request,
                                    ProceedingJoinPoint proceedingJoinPoint) {
        Object[] args = proceedingJoinPoint.getArgs();
        if (args.length == 0) {
            return "";
        }
        Object[] params = Arrays.stream(args)
            .filter(arg -> !(arg instanceof HttpServletRequest)
                           && !(arg instanceof HttpServletResponse)
                           && !(arg instanceof MultipartFile))
            .toArray();
        return params.length == 0 ? ""
            : ServletContextService.getParameterMap(request).toString() + " "
              + Arrays.toString(params);
    }

    private String getClientIP(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (StringUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (StringUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (StringUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }
}

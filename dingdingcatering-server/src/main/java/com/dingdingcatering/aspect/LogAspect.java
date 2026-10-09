package com.dingdingcatering.aspect;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Aspect
@Component
@Slf4j
public class LogAspect {

    private final ObjectMapper objectMapper;

    public LogAspect(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Around("execution(* com.dingdingcatering.controller..*.*(..))")
    public Object recordLog(ProceedingJoinPoint joinPoint) throws Throwable {
        // 1. 获取请求信息
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes != null ? attributes.getRequest() : null;
        String uri = request != null ? request.getRequestURI() : "未知URI";
        String method = request != null ? request.getMethod() : "未知方法";
        String ip = request != null ? request.getRemoteAddr() : "未知IP";
        // 2. 获取方法参数（过滤掉 request/response 等无法序列化的对象）
        Object[] args = joinPoint.getArgs();
        Object[] loggableArgs = new Object[args.length];
        for (int i = 0; i < args.length; i++) {
            if (args[i] instanceof HttpServletRequest || args[i] instanceof HttpServletResponse) {
                loggableArgs[i] = "[Filtered]";
            } else {
                loggableArgs[i] = args[i];
            }
        }
        String params;
        try {
            params = objectMapper.writeValueAsString(loggableArgs);
        } catch (Exception e) {
            params = "[序列化失败，参数包含无法序列化的对象，已过滤]";
        }
        // 3. 记录请求入口
        log.info(">>> 请求开始 | {} {} | IP: {} | 参数: {}", method, uri, ip, params);
        // 4. 执行目标方法并计时
        long startTime = System.currentTimeMillis();
        Object result;
        try {
            result = joinPoint.proceed();
            long costTime = System.currentTimeMillis() - startTime;
            // 5. 记录请求成功出口
            log.info("<<< 请求结束 | {} {} | 耗时: {}ms", method, uri, costTime);
            return result;
        } catch (Throwable e) {
            long costTime = System.currentTimeMillis() - startTime;
            // 7. 记录异常
            log.error("!!! 请求异常 | {} {} | 耗时: {}ms | 异常: {}", method, uri, costTime, e.getMessage(), e);
            throw e;
        }
    }
}

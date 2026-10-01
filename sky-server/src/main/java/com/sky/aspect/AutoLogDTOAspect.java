package com.sky.aspect;

import com.sky.annotation.AutoLogDTO;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;

import java.util.Arrays;

@Aspect
@Component
@Slf4j
public class AutoLogDTOAspect {
    @Pointcut("execution(* com.sky.controller.*.*(..)) && @annotation(com.sky.annotation.AutoLogDTO)")
    public void pointcut() {
    }

    @Before("pointcut()")
    public void autoLogDTOBefore(JoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        String methodName = signature.getMethod().getName();
        String className = signature.getDeclaringType().getSimpleName();

        AutoLogDTO autoLogDTO = signature.getMethod().getAnnotation(AutoLogDTO.class);
        String customMessage = autoLogDTO.value();

        Object[] args = joinPoint.getArgs();

        StringBuilder logMessage = new StringBuilder();
        logMessage.append("【").append(className).append(".").append(methodName).append("】");

        if (!customMessage.isEmpty()) {
            logMessage.append(" ").append(customMessage);
        }

        if (args == null || args.length == 0) {
            log.info("{} 无参数", logMessage);
        } else if (args.length == 1) {
            log.info("{} 参数: {}", logMessage, args[0]);
        } else {
            log.info("{} 参数: {}", logMessage, Arrays.toString(args));
        }
    }
}

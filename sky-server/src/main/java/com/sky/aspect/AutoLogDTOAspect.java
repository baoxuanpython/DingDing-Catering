package com.sky.aspect;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.JSONWriter;
import com.sky.annotation.AutoLogDTO;
import com.sky.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;

import java.util.Arrays;

@Aspect
@Component
@Slf4j
public class AutoLogDTOAspect {
    @Pointcut("execution(* com.sky.controller..*(..)) && @annotation(com.sky.annotation.AutoLogDTO)")
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
        } else {
            log.info("{} 参数: {}", logMessage, Arrays.toString(args));
        }
    }

    @AfterReturning(pointcut = "pointcut()", returning = "result")
    public void autoLogDTOAfterReturning(JoinPoint joinPoint, Object result) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        AutoLogDTO autoLogDTO = signature.getMethod().getAnnotation(AutoLogDTO.class);

        if (autoLogDTO.returnResult() && result instanceof Result<?> resultObj) {
            String methodName = signature.getMethod().getName();
            String className = signature.getDeclaringType().getSimpleName();
            StringBuilder logMessage = new StringBuilder();
            logMessage.append("【").append(className).append(".").append(methodName).append("】");
            if (!autoLogDTO.value().isEmpty()) {
                logMessage.append(" ").append(autoLogDTO.value());
            }

            JSONObject jsonResult = new JSONObject();
            jsonResult.put("code", resultObj.getCode());
            jsonResult.put("msg", resultObj.getMsg());

            if (resultObj.getData() != null) {
                String dataJson = JSON.toJSONString(resultObj.getData(), JSONWriter.Feature.PrettyFormat);
                Object parsedData = JSON.parse(dataJson);
                jsonResult.put("data", parsedData);
            } else {
                jsonResult.put("data", null);
            }

            String formattedJson = JSON.toJSONString(jsonResult, JSONWriter.Feature.PrettyFormat);

            log.info("╔══════════════════════════════════════════════════════════════╗");
            log.info("║ {} 返回值详情", logMessage);
            log.info("╚══════════════════════════════════════════════════════════════╝");
            log.info("{}", formattedJson);
            log.info("");
        }
    }
}

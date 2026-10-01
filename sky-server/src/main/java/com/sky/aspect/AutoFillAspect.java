package com.sky.aspect;

import com.sky.annotation.AutoFill;
import com.sky.enumeration.OperationType;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.LocalDateTime;

import static com.sky.context.BaseContext.getCurrentId;

/**
 * 自动填充切面
 */
@Aspect
@Component
@Slf4j
public class AutoFillAspect {
    /**
     * 自动填充点切面
     */
    @Pointcut("execution(* com.sky.mapper.*.*(..)) && @annotation( com.sky.annotation.AutoFill)")
    public void autoFillPointCut() {
    }

    @Before("autoFillPointCut()")
    public void autoFillBefore(JoinPoint joinPoint) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();//获取方法签名
        AutoFill autoFill = signature.getMethod().getAnnotation(AutoFill.class);//获取注解
        OperationType operationType = autoFill.value();//获取操作类型
        Object[] args = joinPoint.getArgs();//获取参数
        if (args == null || args.length == 0) {
            return;
        }
        Long currentId = getCurrentId();
        boolean foundAutoFillEntity = false;
        for (Object arg : args) {
            if (arg != null && arg.getClass().isAnnotationPresent(AutoFill.class)) {
                foundAutoFillEntity = true;
                if (operationType == OperationType.INSERT) {
                    Method setCreateTime = arg.getClass().getMethod("setCreateTime", LocalDateTime.class);
                    setCreateTime.invoke(arg, LocalDateTime.now());
                    Method setUpdateTime = arg.getClass().getMethod("setUpdateTime", LocalDateTime.class);
                    setUpdateTime.invoke(arg, LocalDateTime.now());
                    Method setCreate = arg.getClass().getMethod("setCreateUser", Long.class);
                    setCreate.invoke(arg, currentId);
                    Method setUpdateUser = arg.getClass().getMethod("setUpdateUser", Long.class);
                    setUpdateUser.invoke(arg, currentId);
                } else if (operationType == OperationType.UPDATE) {
                    Method setUpdateTime = arg.getClass().getMethod("setUpdateTime", LocalDateTime.class);
                    setUpdateTime.invoke(arg, LocalDateTime.now());
                    Method setUpdateUser = arg.getClass().getMethod("setUpdateUser", Long.class);
                    setUpdateUser.invoke(arg, currentId);
                }
            }
        }
        if (!foundAutoFillEntity) {
            throw new RuntimeException("方法 " + signature.getMethod().getName() + " 上有@AutoFill注解，但参数中没有带有@AutoFill注解的实体类对象");
        }
    }
}

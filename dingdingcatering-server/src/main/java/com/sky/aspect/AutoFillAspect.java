package com.dingdingcatering.aspect;

import com.dingdingcatering.annotation.AutoFill;
import com.dingdingcatering.enumeration.OperationType;
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

import static com.dingdingcatering.context.BaseContext.getCurrentId;

/**
 * 自动填充切面（增强版�?
 * <p>
 * 支持两种模式�?
 * 1. 标准模式：方法参数中包含�?@AutoFill 注解的实体类 �?自动填充公共字段
 * 2. 简洁模式：方法参数中没有实体类（如简单状态更新）�?优雅跳过，不报错
 *
 * @author baoxuanpython
 * @version 2.0
 */
@Aspect
@Component
@Slf4j
public class AutoFillAspect {

    /**
     * 切入点：拦截所有带�?@AutoFill 注解�?Mapper 方法
     */
    @Pointcut("execution(* com.dingdingcatering.mapper..*(..)) && @annotation(com.dingdingcatering.annotation.AutoFill)")
    public void autoFillPointCut() {
    }

    /**
     * 前置通知：在目标方法执行前自动填充公共字�?
     * <p>
     * 处理流程�?
     * 1. 获取方法�?@AutoFill 注解，确定操作类型（INSERT/UPDATE�?
     * 2. 遍历方法参数，查找带�?@AutoFill 注解的实体类
     * 3. 对找到的实体类自动填充公共字段（createTime/updateTime/createUser/updateUser�?
     * 4. 如果未找到实体类，记录日志并优雅跳过（不抛异常）
     *
     * @param joinPoint 连接点对象，包含目标方法的信�?
     * @throws IllegalAccessException    无法访问 setter 方法
     * @throws InvocationTargetException 调用 setter 方法时发生异�?
     */
    @Before("autoFillPointCut()")
    public void autoFillBefore(JoinPoint joinPoint) throws InvocationTargetException, IllegalAccessException {
        log.debug("AutoFill 切面触发 - 方法: {}", joinPoint.getSignature().getName());

        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        AutoFill autoFill = signature.getMethod().getAnnotation(AutoFill.class);
        OperationType operationType = autoFill.value();

        Object[] args = joinPoint.getArgs();
        if (args == null || args.length == 0) {
            log.debug("方法 {} 无参数，跳过自动填充", signature.getName());
            return;
        }

        Long currentId = getCurrentId();
        boolean foundEntity = false;

        for (Object arg : args) {
            if (arg != null && isAutoFillableEntity(arg)) {
                foundEntity = true;
                fillEntityFields(arg, operationType, currentId);
                log.info("�?已为 [{}] 自动填充公共字段 - 操作类型: {}",
                        arg.getClass().getSimpleName(), operationType);
            }
        }

        if (!foundEntity) {
            handleMissingEntity(signature, operationType);
        }
    }

    /**
     * 判断是否为可自动填充的实体类
     * <p>
     * 条件：对象的类上必须标注�?@AutoFill 注解
     *
     * @param arg 方法参数对象
     * @return 是否为可自动填充的实体类
     */
    private boolean isAutoFillableEntity(Object arg) {
        return arg.getClass().isAnnotationPresent(AutoFill.class);
    }

    /**
     * 填充实体类的公共字段
     * <p>
     * 根据操作类型填充不同的字段：
     * - INSERT 操作：填�?createTime, updateTime, createUser, updateUser
     * - UPDATE 操作：只填充 updateTime, updateUser
     *
     * @param entity        待填充的实体类对�?
     * @param operationType 操作类型（INSERT/UPDATE�?
     * @param currentId     当前登录用户ID
     */
    private void fillEntityFields(Object entity, OperationType operationType, Long currentId)
            throws InvocationTargetException, IllegalAccessException {

        Class<?> clazz = entity.getClass();

        if (operationType == OperationType.INSERT) {
            invokeSetter(clazz, entity, "setCreateTime", LocalDateTime.class, LocalDateTime.now());
            invokeSetter(clazz, entity, "setUpdateTime", LocalDateTime.class, LocalDateTime.now());
            invokeSetter(clazz, entity, "setCreateUser", Long.class, currentId);
            invokeSetter(clazz, entity, "setUpdateUser", Long.class, currentId);

        } else if (operationType == OperationType.UPDATE) {
            invokeSetter(clazz, entity, "setUpdateTime", LocalDateTime.class, LocalDateTime.now());
            invokeSetter(clazz, entity, "setUpdateUser", Long.class, currentId);
        }
    }

    /**
     * 安全地调用实体类�?setter 方法
     * <p>
     * 如果实体类缺少指定的 setter 方法，不会抛出异常，
     * 而是记录警告日志并跳过该字段的填�?
     *
     * @param clazz      实体类的 Class 对象
     * @param entity     实体类实�?
     * @param methodName setter 方法名（�?"setCreateTime"�?
     * @param paramType  参数类型（如 LocalDateTime.class�?
     * @param value      要设置的�?
     */
    private void invokeSetter(Class<?> clazz, Object entity, String methodName,
                              Class<?> paramType, Object value)
            throws InvocationTargetException, IllegalAccessException {
        try {
            Method method = clazz.getMethod(methodName, paramType);
            method.invoke(entity, value);
        } catch (NoSuchMethodException e) {
            log.warn("实体�?[{}] 缺少方法 [{}]，跳过该字段填充",
                    clazz.getSimpleName(), methodName);
        }
    }

    /**
     * 处理未找到可填充实体类的情况
     * <p>
     * 不再抛出 RuntimeException，而是记录详细的提示信息�?
     * 这种情况通常出现在以下场景：
     * 1. 简单的更新操作（如只更新状态字段），时间字段由 SQL NOW() 函数处理
     * 2. 公共字段已在其他地方手动处理
     * 3. 该方法确实不需要自动填充功�?
     *
     * @param signature     方法签名
     * @param operationType 操作类型
     */
    private void handleMissingEntity(MethodSignature signature, OperationType operationType) {
        String methodName = signature.getMethod().getName();

        log.info("""
                    ℹ️ 方法 [{}] 带有 @AutoFill({}) 注解，但参数中没有可填充的实体类对象�?
                       可能原因�?
                       1. 这是一个简单更新操作（如状态切换），时间字段由 SQL NOW() 处理
                       2. 该方法的公共字段已在其他地方处理
                       3. 此注解仅用于标记或未来扩�?
                       已自动跳过填充，不影响业务逻辑�?"",
                methodName, operationType);
    }
}

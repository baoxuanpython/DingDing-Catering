package com.dingdingcatering.aspect;

import com.dingdingcatering.annotation.AutoClearCache;
import com.dingdingcatering.enumeration.CacheType;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Component;

/**
 * 自动清除缓存的切面类
 * <p>
 * 处理流程：
 * 1. 拦截 service 包下所有标注了 @AutoClearCache 注解的方法
 * 2. 在方法成功返回后，获取注解中指定的缓存类型
 * 3. 遍历缓存类型，逐一清除对应的缓存
 * 4. 记录缓存清除操作的日志信息
 * <p>
 * 设计说明：
 * - 使用 @AfterReturning 确保只在方法成功返回时才清除缓存，避免失败时误删缓存
 * - 缓存不存在时仅记录警告日志，不抛出异常，保证业务流程不受影响
 *
 * @see AutoClearCache
 * @see CacheType
 */
@Aspect
@Component
@Slf4j
public class AutoClearCacheAspect {

    private final CacheManager cacheManager;

    /**
     * 构造方法，注入缓存管理器
     *
     * @param cacheManager Spring 缓存管理器，用于获取和操作缓存实例
     */
    public AutoClearCacheAspect(CacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }

    /**
     * 定义切点，匹配 service 包下所有标注了 @AutoClearCache 注解的方法
     */
    @Pointcut("execution(* com.dingdingcatering.service..*(..)) && @annotation(com.dingdingcatering.annotation.AutoClearCache)")
    public void pointcut() {
    }

    /**
     * 在目标方法成功返回后执行缓存清除操作
     * <p>
     * 处理流程：
     * 1. 从方法签名中获取 @AutoClearCache 注解
     * 2. 提取注解中声明的缓存类型数组
     * 3. 遍历缓存类型，通过 CacheManager 获取对应缓存实例
     * 4. 若缓存存在则执行清除操作并记录日志
     * 5. 若缓存不存在则记录警告日志并跳过
     *
     * @param joinPoint 连接点对象，包含目标方法的签名和上下文信息
     */
    @AfterReturning("pointcut()")
    public void afterReturning(JoinPoint joinPoint) {
        log.debug("AutoClearCache 切面触发 - 方法：{}", joinPoint.getSignature().getName());
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        AutoClearCache autoClearCache = signature.getMethod().getAnnotation(AutoClearCache.class);
        CacheType[] cacheTypes = autoClearCache.value();
        for (CacheType cacheType : cacheTypes) {
            String cacheName = cacheType.cacheName();
            Cache cache = cacheManager.getCache(cacheName);
            if (cache != null) {
                cache.clear();
                log.info("已清除缓存 [{}] - 操作类型: {}", cacheName, cacheType);
            } else {
                log.warn("缓存 [{}] 不存在，跳过清除", cacheName);
            }
        }
    }
}

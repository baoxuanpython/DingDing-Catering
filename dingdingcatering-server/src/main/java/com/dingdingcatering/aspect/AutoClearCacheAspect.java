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

@Aspect
@Component
@Slf4j
public class AutoClearCacheAspect {

    private final CacheManager cacheManager;

    public AutoClearCacheAspect(CacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }

    @Pointcut("execution(* com.dingdingcatering.service..*(..)) && @annotation(com.dingdingcatering.annotation.AutoClearCache)")
    public void pointcut() {
    }

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

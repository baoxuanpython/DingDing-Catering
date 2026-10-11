package com.dingdingcatering.annotation;

import com.dingdingcatering.enumeration.CacheType;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 自动清除缓存注解
 * <p>
 * 用于标注需要在方法执行成功后自动清除指定缓存的 service 层方法。
 * 配合 AutoClearCacheAspect 切面使用，方法返回后自动清除注解中声明的缓存类型。
 * <p>
 * 使用示例：
 * &#064;AutoClearCache({CacheType.USER, CacheType.ORDER})
 * public void updateUser(User user) { ... }
 *
 * @see CacheType
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface AutoClearCache {
    /**
     * 需要清除的缓存类型数组
     *
     * @return 缓存类型数组，指定方法执行成功后需要清除的缓存
     */
    CacheType[] value();
}

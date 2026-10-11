package com.dingdingcatering.annotation;

import com.dingdingcatering.enumeration.OperationType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 自动填充注解
 * <p>
 * 用于标注需要在数据操作时自动填充公共字段（如创建时间、更新时间、创建人等）的实体类或方法。
 * 配合相应的切面使用，根据操作类型自动填充对应的审计字段。
 * <p>
 * 使用示例：
 * &#064;AutoFill(OperationType.INSERT)
 * public void saveUser(User user) { ... }
 *
 * @see OperationType
 */
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface AutoFill {
    /**
     * 指定自动填充的操作类型
     *
     * @return 操作类型，默认为 INSERT
     */
    OperationType value() default OperationType.INSERT;
}

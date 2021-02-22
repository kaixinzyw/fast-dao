package com.fast.condition.many;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * 多对多
 *
 * @author zyw
 * @date 2021/01/14
 */
@Target(FIELD)
@Retention(RUNTIME)
public @interface FastOneToMany {

    /**
     * 关联表实体
     */
    Class joinEntity();

    /**
     * 关联字段名
     */
    String joinMappedBy() default "";
}

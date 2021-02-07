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
public @interface FastManyToMany {

    /**
     * 中间表实体
     */
    Class joinEntity();

    /**
     * 中间表关联列名
     */
    String joinMappedBy() default "";

    /**
     * 关系表实体
     */
    Class relationalEntity();

    /**
     * 关系表列名
     */
    String relationalMappedBy() default "";
}

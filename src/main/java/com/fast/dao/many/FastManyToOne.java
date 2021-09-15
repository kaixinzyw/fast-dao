package com.fast.dao.many;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * 多对多
 *
 * @author zyw
 */
@Target(FIELD)
@Retention(RUNTIME)
public @interface FastManyToOne {

    /**
     * 关联表实体
     * @return 关联表实体
     */
    Class joinEntity();

    /**
     * 关联字段名
     * @return 关联字段名

     */
    String joinMappedBy() default "";
}

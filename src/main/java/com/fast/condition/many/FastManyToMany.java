package com.fast.condition.many;

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
public @interface FastManyToMany {

    /**
     * 中间表实体
     * @return 中间表实体
     */
    Class joinEntity();

    /**
     * 中间表关联字段名
     * @return 中间表关联字段名
     */
    String joinMappedBy() default "";

    /**
     * 关系表实体
     * @return 关系表实体
     */
    Class relationalEntity();

    /**
     * 关系表字段名
     * @return 关系表字段名
     */
    String relationalMappedBy() default "";
}

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
public @interface FastJoinQuery {


    /**
     * 映射表别名
     * @return 表别名
     */
    String thisTableAlias() default "";

    /**
     * 映射列名
     * @return 映射列名
     */
    String thisColumnName() default "";

    /**
     * 连接表别名
     *
     * @return {@link String}
     */
    String joinTableAlias() default "";

    /**
     * 连接列的名字
     *
     * @return {@link String}
     */
    String joinColumnName() default "";

    /**
     * 连接表别名
     *
     * @return {@link String}
     */
    String value() default "";


}

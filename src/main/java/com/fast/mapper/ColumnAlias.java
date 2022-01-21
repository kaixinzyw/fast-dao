package com.fast.mapper;

import java.lang.annotation.Retention;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * 列别名
 *
 * @author 张亚伟
 */
@Retention(RUNTIME)
public @interface ColumnAlias {
    String value() default "";
}

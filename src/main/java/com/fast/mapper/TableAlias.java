package com.fast.mapper;

import java.lang.annotation.Retention;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * 表别名
 *
 * @author 张亚伟
 */
@Retention(RUNTIME)
public @interface TableAlias {
    String value() default "";
}

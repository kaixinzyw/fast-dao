package com.fast.mapper;

import java.lang.annotation.Retention;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * 忽略字段查询
 *
 * @author 张亚伟
 */
@Retention(RUNTIME)
public @interface NotQuery {
}

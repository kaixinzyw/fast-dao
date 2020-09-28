package com.fast.dao;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * @author 自动事务注解
 */
@Target(ElementType.METHOD)
@Retention(RUNTIME)
public @interface FastTransaction {
}

package com.fast.db.template.cache;

import java.lang.annotation.*;
import java.util.concurrent.TimeUnit;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
/**
 * @author 张亚伟 398850094@qq.com
 */
@Target(TYPE)
@Retention(RUNTIME)
public @interface FastLocalCache {

    long value() default 0L;
    long cacheTime() default 0L;
    TimeUnit cacheTimeType() default TimeUnit.SECONDS;



}

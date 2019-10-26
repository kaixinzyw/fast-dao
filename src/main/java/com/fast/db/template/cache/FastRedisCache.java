package com.fast.db.template.cache;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Redis缓存,当开启缓存并操作对象配置此注解时,会将查询到的数据缓存到redis中
 * 当进行使用此框架模板进行操作新增,更新,删除操作时,会自动刷新Redis缓存中的数据
 * 此方法使用了StringRedisTemplate,需要存在于Spring容器中
 * 默认参数为框架设置的缓存时间和类型
 * 缓存可选参数
 * 1:(Long 秒) 如@FastRedisCache(60L) 缓存60秒
 * 2:(cacheTime = 时间,cacheTimeType = TimeUnit) 如@FastRedisCache(cacheTime =1L,cacheTimeType = TimeUnit.HOURS) 缓存1小时
 * @author 张亚伟 https://github.com/kaixinzyw/fast-db-template
 */
@Target(TYPE)
@Retention(RUNTIME)
public @interface FastRedisCache {

    long value() default 0L;

    long cacheTime() default 0L;

    TimeUnit cacheTimeType() default TimeUnit.SECONDS;


}

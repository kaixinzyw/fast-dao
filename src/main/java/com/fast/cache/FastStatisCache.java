package com.fast.cache;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * 纯本地内存缓存,当集群项目部署,不会进行其他服务器的缓存刷新,使用场景需要注意,缓存的数据一般不会变,比如项目存储在数据库中的配置信息等
 * 当开启缓存并操作对象配置此注解时,会将查询到的数据缓存到本地中
 * 当进行使用此框架模板进行操作新增,更新,删除操作时,会自动刷新内存中缓存的数据
 * 默认参数为框架设置的缓存时间和类型
 * 缓存可选参数
 * 1:(Long 秒) 如@FastStatisCache(60L) 缓存60秒
 * 2:(cacheTime = 时间,cacheTimeType = TimeUnit) 如@FastStatisCache(cacheTime =1L,cacheTimeType = TimeUnit.HOURS) 缓存1小时
 * @author 张亚伟 https://github.com/kaixinzyw
 */
@Target(TYPE)
@Retention(RUNTIME)
public @interface FastStatisCache {

    long value() default 0L;

    long cacheTime() default 0L;

    TimeUnit cacheTimeType() default TimeUnit.SECONDS;


}

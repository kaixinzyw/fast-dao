package com.fast.cache;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Redis和本地内存结合的缓存,在特殊场景使用,数据库中需要实时进行集群同步,数据量大并取用频繁,并且数据修改不频繁的场景,如商品的品牌或类目信息
 * Redis只会存储版本号,本地存储具体数据内容
 * 当开启缓存并操作对象配置此注解时,会将查询到的数据缓存到本地中,同时在Redis中获取数据版本号
 * 当进行使用此框架模板进行操作新增,更新,删除操作时,会自动刷新Redis缓存中的数据数据版本号
 * 此方法使用了RedisConnectionFactory,需要存在于Spring容器中
 * 默认参数为框架设置的缓存时间和类型
 * 缓存可选参数
 * 1:(Long 秒) 如@FastRedisLocalCache(60L) 缓存60秒
 * 2:(cacheTime = 时间,cacheTimeType = TimeUnit) 如@FastRedisLocalCache(cacheTime =1L,cacheTimeType = TimeUnit.HOURS) 缓存1小时
 * @author 张亚伟 https://github.com/kaixinzyw
 */
@Target(TYPE)
@Retention(RUNTIME)
public @interface FastRedisLocalCache {

    long value() default 0L;
    long cacheTime() default 0L;
    TimeUnit cacheTimeType() default TimeUnit.SECONDS;



}

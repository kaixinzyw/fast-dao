package com.fast.utils;

import cn.hutool.log.StaticLog;
import com.fast.config.FastDaoAttributes;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.concurrent.TimeUnit;

/**
 * 分布式锁
 */
public class FastRedisLock {

    /**
     * 前缀
     */
    private static final String KEY_PRE = "fast_redis_lock.";
    /**
     * REDIS 默认值
     */
    private static final String KEY_DEF_VALUE = "1";

    private FastRedisLock() {
    }

    /**
     * 阻塞一定时间,可以提前是否锁
     *
     * @param keyStr   锁名
     * @param time     最大阻塞时间
     * @param timeUnit 时间单位
     */
    public static void lock(String keyStr, long time, TimeUnit timeUnit) {
        StringRedisTemplate lock = new StringRedisTemplate(FastDaoAttributes.getRedisConnectionFactory());
        String key = KEY_PRE + keyStr;
        while (!lock.opsForValue().setIfAbsent(key, KEY_DEF_VALUE, time, timeUnit)) {
            try {
                TimeUnit.MILLISECONDS.sleep(100);
            } catch (InterruptedException ignore) {
            }
        }
    }

    /**
     * 阻塞一定时间,在阻塞时间内无释放锁,异常抛出,
     *
     * @param keyStr   锁名
     * @param time     最大阻塞时间 超过锁最大时间抛出异常
     * @param timeUnit 时间单位
     * @return 是否阻塞成功
     */
    public static boolean throwLock(String keyStr, long time, TimeUnit timeUnit) {
        StringRedisTemplate lock = new StringRedisTemplate(FastDaoAttributes.getRedisConnectionFactory());
        String key = KEY_PRE + keyStr;
        long lockTime = System.currentTimeMillis() + timeUnit.toMillis(time);
        while (!lock.opsForValue().setIfAbsent(key, KEY_DEF_VALUE, time * 10, timeUnit)) {
            //超时处理，获取锁失败。
            if (System.currentTimeMillis() > lockTime) {
                StaticLog.error("获取锁 {} 失败", keyStr);
                throw new RuntimeException("获取Redos锁<" + keyStr + ">失败");
            }
            try {
                TimeUnit.MILLISECONDS.sleep(100);
            } catch (InterruptedException ignore) {
            }
        }
        return true;
    }

    /**
     * 尝试在一定时间内获取锁,默认10秒
     *
     * @param keyStr 锁名
     */
    public static void lock(String keyStr) {
        lock(keyStr, 10, TimeUnit.SECONDS);
    }
    public static void throwLock(String keyStr) {
        throwLock(keyStr, 10, TimeUnit.SECONDS);
    }

    /**
     * 尝试在一定时间内获取锁
     *
     * @param keyStr 锁名
     * @param time   最大阻塞时间,默认秒
     */
    public static void lock(String keyStr, Integer time) {
        lock(keyStr, time, TimeUnit.SECONDS);
    }
    public static void throwLock(String keyStr, Integer time) {
        throwLock(keyStr, time, TimeUnit.SECONDS);
    }

    /**
     * 解锁
     *
     * @param keyStr key
     */
    public static void lockRelease(String keyStr) {
        StringRedisTemplate lock = new StringRedisTemplate(FastDaoAttributes.getRedisConnectionFactory());
        lock.delete(KEY_PRE + keyStr);
    }

}

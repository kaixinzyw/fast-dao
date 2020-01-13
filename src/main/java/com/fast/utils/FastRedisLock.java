package com.fast.utils;

import com.fast.config.FastDaoAttributes;
import io.netty.util.concurrent.FastThreadLocal;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.util.concurrent.TimeUnit;

/**
 * 分布式锁
 */
public class FastRedisLock {

    private static final FastThreadLocal<StringRedisTemplate> redisLockThreadLocal = new FastThreadLocal<>();


    /**
     * 前缀
     */
    private static final String KEY_PRE = "fast_redis_lock.";
    /**
     * REDIS 默认值
     */
    private static final String KEY_DEF_VALUE = "lock";

    private FastRedisLock() {
    }


    /**
     * 对指定Key存储,Key存在时如果有相同Key进行请求,则进行阻塞,可使用lockRelease(KEY) 进行提前解锁
     * 每50毫秒对key进行重复检测,如果不存在则取消阻塞
     *
     * @param keyStr   锁名
     * @param time     最大阻塞时间
     * @param timeUnit 时间单位
     * @throws RedisLockTimeOutException 如果阻塞时间超出还未进行解锁操作,则抛出此异常信息
     */
    public static void lockTime(String keyStr, long time, TimeUnit timeUnit) throws RedisLockTimeOutException {
        ValueOperations<String, String> valueOperations = init().opsForValue();
        String key = KEY_PRE + keyStr;
        long lockDurationTime = timeUnit.toMillis(time);
        long lockExpiredTime = System.currentTimeMillis() + lockDurationTime;
        long redisKeyExpiredTime = time * 2;
        while (!valueOperations.setIfAbsent(key, KEY_DEF_VALUE, redisKeyExpiredTime, timeUnit)) {
            if (System.currentTimeMillis() > lockExpiredTime) {
                lockRelease(key);
                throw new RedisLockTimeOutException("Redis Lock Time Out Key: " + key);
            }
            try {
                if (lockDurationTime > 50) {
                    TimeUnit.MILLISECONDS.sleep(50);
                } else {
                    TimeUnit.MILLISECONDS.sleep(lockDurationTime);
                }
            } catch (InterruptedException ignore) {
                lockRelease(key);
                ignore.printStackTrace();
                throw new RedisLockTimeOutException("Redis Lock Time Error Key: " + key);
            }
        }
    }

    /**
     * 对指定Key存储,Key存在时如果有相同Key进行请求,则进行阻塞,可使用lockRelease(KEY) 进行提前解锁
     *
     * @param keyStr  锁名
     * @param seconds 最大阻塞时间,秒
     * @throws RedisLockTimeOutException 如果阻塞时间超出还未进行解锁操作,则抛出此异常信息
     */
    public static void lockTime(String keyStr, Integer seconds) throws RedisLockTimeOutException {
        lockTime(keyStr, seconds, TimeUnit.SECONDS);
    }

    /**
     * 对指定Key存储10秒,Key存在时如果有相同Key进行请求,则进行阻塞,可使用lockRelease(KEY) 进行提前解锁
     *
     * @param keyStr 锁名
     * @throws RedisLockTimeOutException 如果阻塞时间超出还未进行解锁操作,则抛出此异常信息
     */
    public static void lockTime(String keyStr) throws RedisLockTimeOutException {
        lockTime(keyStr, 10L, TimeUnit.SECONDS);
    }


    /**
     * 对指定key进行存储,可使用lockRelease(KEY) 进行提前删除
     *
     * @param keyStr   锁名
     * @param time     最大锁定时间
     * @param timeUnit 时间单位
     * @return key存在期间有相同的key进行访问, 返回false
     */
    public static Boolean lock(String keyStr, long time, TimeUnit timeUnit) {
        return init().opsForValue().setIfAbsent(KEY_PRE + keyStr, KEY_DEF_VALUE, time, timeUnit);
    }


    /**
     * 对指定key进行存储,可使用lockRelease(KEY) 进行提前删除
     *
     * @param keyStr  锁名
     * @param seconds key最大生效时间,秒
     * @return key存在期间有相同的key进行访问, 返回false
     */
    public static Boolean lock(String keyStr, Integer seconds) {
        return lock(keyStr, seconds, TimeUnit.SECONDS);
    }

    /**
     * 对指定key进行存储10秒,可使用lockRelease(KEY) 进行提前删除
     *
     * @param keyStr 锁名
     * @return key存在期间有相同的key进行访问, 返回false
     */
    public static Boolean lock(String keyStr) {
        return lock(keyStr, 10, TimeUnit.SECONDS);
    }

    /**
     * 解锁,删除Redis中指定key
     *
     * @param keyStr key
     */
    public static void lockRelease(String keyStr) {
        StringRedisTemplate lock = init();
        lock.delete(KEY_PRE + keyStr);
    }

    /**
     * 初始化Redis
     * @return Redis模板对象
     */
    private static StringRedisTemplate init() {
        StringRedisTemplate lock = redisLockThreadLocal.get();
        if (lock == null) {
            lock = new StringRedisTemplate(FastDaoAttributes.getRedisConnectionFactory());
            redisLockThreadLocal.set(lock);
            return lock;
        }
        return lock;
    }

}

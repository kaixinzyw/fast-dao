package com.fast.utils.lock;

import cn.hutool.core.util.BooleanUtil;
import com.fast.config.FastDaoAttributes;
import com.google.common.collect.Interner;
import com.google.common.collect.Interners;
import io.netty.util.concurrent.FastThreadLocal;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.util.concurrent.TimeUnit;

/**
 * 分布式锁
 *
 * @author 张亚伟 https://github.com/kaixinzyw
 */
public class FastRedisLock {

    private static final FastThreadLocal<StringRedisTemplate> redisLockThreadLocal = new FastThreadLocal<>();


    /**
     * 前缀
     */
    private static final String KEY_PRE = "fast_redis_lock.";


    private FastRedisLock() {
    }


    /**
     * 阻塞锁
     * 对指定Key存储,Key存在时如果有相同Key进行请求,则进行阻塞,可使用lockRelease(KEY) 进行提前解锁
     * 每50毫秒对key进行重复检测,如果不存在则取消阻塞
     *
     * @param lockKey  锁名
     * @param time     最大阻塞时间
     * @param timeUnit 时间单位
     * @throws BlockingLockTimeOutException 如果阻塞时间超出还未进行解锁操作,则抛出此异常信息
     * @return 锁
     */
    public static BlockingLock blockingLock(String lockKey, long time, TimeUnit timeUnit) throws BlockingLockTimeOutException {
        ValueOperations<String, String> valueOperations = init().opsForValue();
        String key = KEY_PRE + lockKey;
        Interner<String> pool = Interners.newWeakInterner();
        synchronized (pool.intern(key)){
            long lockDurationTime = timeUnit.toMillis(time);
            long lockExpiredTime = System.currentTimeMillis() + lockDurationTime;
            long redisKeyExpiredTime = lockDurationTime + 1000L;
            while (!BooleanUtil.isTrue(valueOperations.setIfAbsent(key, String.valueOf(Thread.currentThread().getId()), redisKeyExpiredTime, TimeUnit.MILLISECONDS))) {
                if (System.currentTimeMillis() > lockExpiredTime) {
                    lockRelease(key);
                    throw new BlockingLockTimeOutException("Redis Lock Time Out Key: " + lockKey);
                }
                try {
                    TimeUnit.MILLISECONDS.sleep(50);
                } catch (InterruptedException ignore) {
                    lockRelease(key);
                    ignore.printStackTrace();
                    throw new BlockingLockTimeOutException("Redis Lock Time Error Key: " + lockKey);
                }
            }
        }
        return new BlockingLock(lockKey);

    }

    /**
     * 阻塞锁
     * 对指定Key存储,Key存在时如果有相同Key进行请求,则进行阻塞,可使用lockRelease(KEY) 进行提前解锁
     *
     * @param lockKey 锁名
     * @param seconds 最大阻塞时间,秒
     * @throws BlockingLockTimeOutException 如果阻塞时间超出还未进行解锁操作,则抛出此异常信息
     * @return 锁
     */
    public static BlockingLock blockingLock(String lockKey, Integer seconds) throws BlockingLockTimeOutException {
        return blockingLock(lockKey, seconds, TimeUnit.SECONDS);
    }

    /**
     * 阻塞锁
     * 对指定Key存储10秒,Key存在时如果有相同Key进行请求,则进行阻塞,可使用lockRelease(KEY) 进行提前解锁
     *
     * @param lockKey 锁名
     * @throws BlockingLockTimeOutException 如果阻塞时间超出还未进行解锁操作,则抛出此异常信息
     * @return 锁
     */
    public static BlockingLock blockingLock(String lockKey) throws BlockingLockTimeOutException {
        return blockingLock(lockKey, 10L, TimeUnit.SECONDS);
    }


    /**
     * 状态锁
     * 对指定key进行存储,可使用lockRelease(KEY) 进行提前删除
     *
     * @param lockKey  锁名
     * @param time     最大锁定时间
     * @param timeUnit 时间单位
     * @return key存在期间有相同的key进行访问, 返回false
     */
    public static StatusLock statusLock(String lockKey, long time, TimeUnit timeUnit) {
        return new StatusLock(lockKey, !BooleanUtil.isTrue(init().opsForValue().setIfAbsent(KEY_PRE + lockKey, String.valueOf(Thread.currentThread().getId()), time, timeUnit)));
    }


    /**
     * 状态锁
     * 对指定key进行存储,可使用lockRelease(KEY) 进行提前删除
     *
     * @param lockKey 锁名
     * @param seconds key最大生效时间,秒
     * @return key存在期间有相同的key进行访问, 返回false
     */
    public static StatusLock statusLock(String lockKey, Integer seconds) {
        return statusLock(lockKey, seconds, TimeUnit.SECONDS);
    }

    /**
     * 状态锁
     * 对指定key进行存储10秒,可使用lockRelease(KEY) 进行提前删除
     *
     * @param lockKey 锁名
     * @return key存在期间有相同的key进行访问, 返回false
     */
    public static StatusLock statusLock(String lockKey) {
        return statusLock(lockKey, 10, TimeUnit.SECONDS);
    }

    /**
     * 释放锁,释放Redis中指定key
     *
     * @param lockKey key
     */
    public static void lockRelease(String lockKey) {
        StringRedisTemplate lock = init();
        lock.delete(KEY_PRE + lockKey);
    }

    public static Boolean lockIsExist(String lockKey) {
        return init().opsForValue().get(KEY_PRE + lockKey) == null ? Boolean.FALSE : Boolean.TRUE;
    }

    /**
     * 初始化Redis
     *
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

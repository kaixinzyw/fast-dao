package com.fast.utils.lock;

import cn.hutool.core.util.StrUtil;
import com.fast.config.FastDaoAttributes;
import io.netty.util.concurrent.FastThreadLocal;
import org.springframework.data.redis.core.StringRedisTemplate;

/**
 * 锁基类
 *
 * @author 张亚伟 https://github.com/kaixinzyw
 */
public class BaseLock {

    private static final FastThreadLocal<StringRedisTemplate> redisLockThreadLocal = new FastThreadLocal<>();

    /**
     * 前缀
     */
    private static final String KEY_PRE = "fast_redis_lock_.";

    /**
     * 锁key
     */
    protected final String lockKey;

    /**
     * 锁是否被占用
     */
    protected Boolean isLock = Boolean.FALSE;

    protected StringRedisTemplate redisTemplate;

    protected String threadId;

    public BaseLock(String lockKey) {
        this.lockKey = KEY_PRE + lockKey;
        redisTemplate = init();
        threadId = String.valueOf(Thread.currentThread().getId());
    }

    /**
     * 释放锁,释放Redis中指定key
     */
    public void unlock() {
        if (isLock && StrUtil.equals(threadId, redisTemplate.opsForValue().get(lockKey))) {
            redisTemplate.delete(lockKey);
        }
    }

    /**
     * @return 检测锁是否存在, 此查询为实时查询
     */
    public Boolean lockIsExist() {
        return redisTemplate.opsForValue().get(lockKey) == null ? Boolean.FALSE : Boolean.TRUE;
    }


    /**
     * 初始化Redis
     *
     * @return Redis模板对象
     */
    private StringRedisTemplate init() {
        StringRedisTemplate lock = redisLockThreadLocal.get();
        if (lock == null) {
            lock = new StringRedisTemplate(FastDaoAttributes.getRedisConnectionFactory());
            redisLockThreadLocal.set(lock);
            return lock;
        }
        return lock;
    }
}

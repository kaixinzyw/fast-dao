package com.fast.utils.lock;

import cn.hutool.core.util.StrUtil;
import com.fast.config.FastDaoAttributes;
import org.springframework.data.redis.core.StringRedisTemplate;

/**
 * 锁基类
 *
 * @author 张亚伟 https://github.com/kaixinzyw
 */
public class BaseLock {


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
        redisTemplate = FastDaoAttributes.getStringRedisTemplate();
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

}

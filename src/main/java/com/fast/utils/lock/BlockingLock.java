package com.fast.utils.lock;

import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.BooleanUtil;
import com.google.common.collect.Interner;
import com.google.common.collect.Interners;
import org.springframework.data.redis.core.ValueOperations;

import java.util.concurrent.TimeUnit;

/**
 * 阻塞锁封装
 *
 * @author 张亚伟 https://github.com/kaixinzyw
 */
public class BlockingLock extends BaseLock {

    public BlockingLock(String lockKey) {
        super(lockKey);
    }

    /**
     * 阻塞锁
     * 对指定Key存储,Key存在时如果有相同Key进行请求,则进行阻塞,可使用 unlock() 进行解锁
     *
     * @param keyLockTime Key锁定时间(毫秒)
     * @param cycle       锁检测周期(毫秒)
     * @return 锁竞争失败返回false
     */
    public Boolean lock(long keyLockTime, long cycle) {
        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        Interner<String> pool = Interners.newWeakInterner();
        synchronized (pool.intern(lockKey)) {
            try {
                long lockExpiredTime = System.currentTimeMillis() + keyLockTime;
                long redisKeyExpiredTime = keyLockTime * 2 + cycle;
                while (!BooleanUtil.isTrue(valueOperations.setIfAbsent(lockKey,
                        threadId, redisKeyExpiredTime, TimeUnit.MILLISECONDS))) {
                    if (System.currentTimeMillis() >= lockExpiredTime) {
                        return isLock;
                    }
                    ThreadUtil.sleep(cycle);
                }
            } catch (Exception e) {
                return isLock;
            }
        }
        return isLock = Boolean.TRUE;

    }

    /**
     * 阻塞锁
     * 对指定Key存储,Key存在时如果有相同Key进行请求,则进行阻塞,可使用 unlock() 进行解锁
     *
     * @param keyLockTimeSeconds 最大阻塞时间,秒
     * @return 锁竞争失败返回false
     */
    public Boolean lock(Integer keyLockTimeSeconds) {
        return lock(TimeUnit.MILLISECONDS.convert(keyLockTimeSeconds, TimeUnit.SECONDS), 10L);
    }

    /**
     * 阻塞锁
     * 对指定Key存储10秒,Key存在时如果有相同Key进行请求,则进行阻塞,可使用 unlock() 进行解锁
     *
     * @return 锁竞争失败返回false
     */
    public Boolean lock() {
        return lock(10000L, 10L);
    }

}

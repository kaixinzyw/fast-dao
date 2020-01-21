package com.fast.utils.lock;

/**
 * 锁基类
 *
 * @author 张亚伟 https://github.com/kaixinzyw
 */
public class BaseLock {

    /**
     * 锁key
     */
    private String lockKey;

    public BaseLock(String lockKey) {
        this.lockKey = lockKey;
    }

    /**
     * 释放锁
     */
    public void release() {
        FastRedisLock.lockRelease(lockKey);
    }
}

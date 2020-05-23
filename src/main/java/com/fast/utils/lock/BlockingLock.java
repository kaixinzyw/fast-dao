package com.fast.utils.lock;

/**
 * 阻塞锁封装
 *
 * @author 张亚伟 https://github.com/kaixinzyw
 */
public class BlockingLock extends BaseLock {

    public BlockingLock(String lockKey) {
        super(lockKey);
    }

    @Override
    public void release() {
        FastRedisLock.lockRelease(super.lockKey);
    }

}

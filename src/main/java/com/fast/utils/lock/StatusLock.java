package com.fast.utils.lock;

import cn.hutool.core.util.BooleanUtil;

import java.util.concurrent.TimeUnit;

/**
 * 状态锁封装
 *
 * @author 张亚伟 https://github.com/kaixinzyw
 */
public class StatusLock extends BaseLock {

    public StatusLock(String lockKey) {
        super(lockKey);
    }

    /**
     * 状态锁
     * 对指定key进行存储,unlock() 进行解锁
     * @param keyLockTime     Key锁定时间(毫秒)
     * @return key存在期间有相同的key进行访问, 返回false
     */
    public Boolean lock(long keyLockTime) {
        isLock = BooleanUtil.isTrue(redisTemplate.opsForValue()
                .setIfAbsent(lockKey, threadId, keyLockTime, TimeUnit.MILLISECONDS));
        return isLock;
    }


    /**
     * 状态锁
     * 对指定key进行存储10秒,unlock() 进行解锁
     * @return key存在期间有相同的key进行访问, 返回false
     */
    public Boolean lock() {
        return lock(10000);
    }

}

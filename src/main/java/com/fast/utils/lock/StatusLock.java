package com.fast.utils.lock;

/**
 * 状态锁封装
 *
 * @author 张亚伟 https://github.com/kaixinzyw
 */
public class StatusLock extends BaseLock {

    /**
     * 锁是否可用
     */
    private Boolean lockStatus;

    public StatusLock(String lockKey, Boolean lockStatus) {
        super(lockKey);
        this.lockStatus = lockStatus;
    }

    /**
     * @return 获取锁是否被占用
     */
    public Boolean isLock() {
        return !lockStatus;
    }

}

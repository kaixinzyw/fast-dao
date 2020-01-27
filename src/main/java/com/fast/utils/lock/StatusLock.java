package com.fast.utils.lock;

/**
 * 状态锁封装
 *
 * @author 张亚伟 https://github.com/kaixinzyw
 */
public class StatusLock extends BaseLock {

    /**
     * 锁是否被占用
     */
    private final Boolean isLock;

    public StatusLock(String lockKey, Boolean isLock) {
        super(lockKey);
        this.isLock = isLock;
    }

    /**
     * @return 锁是否被占用,此状态为调用状态锁时的结果,后续修改不会改变,请注意使用
     */
    public Boolean isLock() {
        return isLock;
    }

}

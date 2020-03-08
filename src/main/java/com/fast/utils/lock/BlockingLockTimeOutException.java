package com.fast.utils.lock;

/**
 * 阻塞锁超出锁定时间后未释放锁会抛出此异常
 */
public class BlockingLockTimeOutException extends  Exception {

    public BlockingLockTimeOutException(String message) {
        super(message);
    }
}

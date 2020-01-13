package com.fast.utils;

public class RedisLockTimeOutException extends  Exception {

    public RedisLockTimeOutException(String message) {
        super(message);
    }
}

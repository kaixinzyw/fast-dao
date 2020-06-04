package com.fast.utils.lock;

/**
 * 分布式锁
 *
 * @author 张亚伟 https://github.com/kaixinzyw
 */
public class FastRedisLock {

    private FastRedisLock() {}

    /**
     * 状态锁
     * @param key 锁key
     * @return 锁对象
     */
    public static StatusLock createStatusLock(String key){
        return new StatusLock(key);
    }

    /**
     * 阻塞锁
     * @param key 锁key
     * @return 锁对象
     */
    public static BlockingLock createBlockingLock(String key){
        return new BlockingLock(key);
    }

}

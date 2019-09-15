package com.fast.db.template.utils;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * @author 张亚伟 398850094@qq.com
 */
public class JedisUtils {

    public static JedisPool jedisPool;

    private Jedis getJedis() {
        return jedisPool.getResource();
    }

    private void closeJedis(Jedis jedis) {
        if (jedis != null) {
            jedis.close();
        }
    }

    private Long add(String key) {
        Jedis jedis = getJedis();
        Long incr = jedis.incr(key);
        closeJedis(jedis);
        return incr;
    }

    private Long get(String key) {
        Jedis jedis = getJedis();
        String s = jedis.get(key);
        closeJedis(jedis);
        if (s == null) {
            return 0L;
        }
        return Long.parseLong(s);
    }


    public static Long incr(String key) {
        return new JedisUtils().add(key);
    }

    public static Long getIncr(String key) {
        return new JedisUtils().get(key);
    }

}
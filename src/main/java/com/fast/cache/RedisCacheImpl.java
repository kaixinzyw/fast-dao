package com.fast.cache;

import cn.hutool.core.collection.CollUtil;
import com.alibaba.fastjson.JSONObject;
import com.fast.config.FastDaoAttributes;
import com.fast.mapper.TableMapper;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.List;
import java.util.Set;
public class RedisCacheImpl {

    private final static String WILDCARD = ":*";

    private static StringRedisTemplate redisTemplate() {
        return FastDaoAttributes.getStringRedisTemplate();
    }

    public static <T> List<T> get(Class<T> resultClass, String keyName) {
        StringRedisTemplate redisTemplate = redisTemplate();
        String ostr = redisTemplate.opsForValue().get(keyName);
        if (ostr == null) {
            return null;
        }
        return JSONObject.parseArray(ostr, resultClass);
    }

    public static <T> void set(List<T> ts, TableMapper tableMapper, String keyName) {
        StringRedisTemplate redisTemplate = redisTemplate();
        redisTemplate.opsForValue().set(keyName, JSONObject.toJSONString(ts), tableMapper.getCacheTime(), tableMapper.getCacheTimeType());
    }

    public static void update(TableMapper tableMapper) {
        StringRedisTemplate redisTemplate = redisTemplate();
        Set keys = redisTemplate.keys(DataCache.PREFIX_NAME + tableMapper.getTableName() + WILDCARD);
        if (CollUtil.isNotEmpty(keys)) {
            redisTemplate.delete(keys);
        }
    }


}

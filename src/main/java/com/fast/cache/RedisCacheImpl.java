package com.fast.cache;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.fast.config.FastDaoAttributes;
import com.fast.mapper.TableMapper;
import io.netty.util.concurrent.FastThreadLocal;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.List;
import java.util.Set;

public class RedisCacheImpl {

    private static final FastThreadLocal<StringRedisTemplate> redisTemplateThreadLocal = new FastThreadLocal<>();

    private static StringRedisTemplate redisTemplate() {
        StringRedisTemplate redisTemplate = redisTemplateThreadLocal.get();
        if (redisTemplate == null) {
            redisTemplate = new StringRedisTemplate(FastDaoAttributes.getRedisConnectionFactory());
            redisTemplateThreadLocal.set(redisTemplate);
        }
        return redisTemplate;
    }

    public static <T> List<T> get(TableMapper tableMapper, String keyName) {
        StringRedisTemplate redisTemplate = redisTemplate();
        String ostr = redisTemplate.opsForValue().get(keyName);
        if (ostr == null) {
            return null;
        }
        return JSONObject.parseArray(ostr, tableMapper.getObjClass());
    }

    public static  <T> void set(List<T> ts, TableMapper tableMapper, String keyName) {
        StringRedisTemplate redisTemplate = redisTemplate();
        redisTemplate.opsForValue().set(keyName, JSONObject.toJSONString(ts), tableMapper.getCacheTime(), tableMapper.getCacheTimeType());
    }

    public static void update(TableMapper tableMapper) {
        StringRedisTemplate redisTemplate = redisTemplate();
        Set keys = redisTemplate.keys("fast_cache_" + tableMapper.getTableName() + ":*");
        if (CollUtil.isNotEmpty(keys)) {
            redisTemplate.delete(keys);
        }
    }


}

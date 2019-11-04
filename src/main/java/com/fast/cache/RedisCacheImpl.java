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

    /**
     * 从缓存中回去列表数据
     *
     * @return 查询结果
     */
    public static <T> List<T> get(TableMapper tableMapper, String keyName) {
        StringRedisTemplate redisTemplate = redisTemplate();
        String ostr = redisTemplate.opsForValue().get(keyName);
        if (ostr == null) {
            return null;
        }
        return JSONObject.parseArray(ostr, tableMapper.getObjClass());
    }

    /**
     * 设置缓存
     *
     * @param ts 设置的参数
     */
    public static  <T> void set(List<T> ts, TableMapper tableMapper, String keyName) {
        StringRedisTemplate redisTemplate = redisTemplate();
        redisTemplate.opsForValue().set(keyName, JSONObject.toJSONString(ts), tableMapper.getCacheTime(), tableMapper.getCacheTimeType());
    }

    /**
     * 更新缓存,对象的缓存信息
     * @param tableMapper 数据操作对象映射信息
     * @return 更新条数
     */
    public static void update(TableMapper tableMapper) {
        StringRedisTemplate redisTemplate = redisTemplate();
        Set keys = redisTemplate.keys("fast_cache_" + tableMapper.getTableName() + ":*");
        if (CollUtil.isNotEmpty(keys)) {
            redisTemplate.delete(keys);
        }
    }


}

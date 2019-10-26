package com.fast.db.template.cache;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSONObject;
import com.fast.db.template.mapper.FastMapperUtil;
import com.fast.db.template.mapper.TableMapper;
import com.fast.db.template.template.FastExample;
import com.fast.db.template.utils.SpringUtil;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.support.atomic.RedisAtomicLong;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 缓存实现,目前支持三种缓存
 * 在需要进行缓存的Bean中加入注解
 * 1:@FastRedisCache Redis缓存
 * 2:@FastStatisCache 本地缓存
 * 3:@FastRedisLocalCache 本地存数据,Redis存数据缓存版本号,当Redis版本更新时进行本地缓存刷新
 * 默认参数为框架设置的缓存时间和类型
 * 缓存可选参数
 * 1:(Long 秒) 如@FastRedisCache(60L) 缓存60秒
 * 2:(cacheTime = 时间,cacheTimeType = TimeUnit) 如@FastRedisCache(cacheTime =1L,cacheTimeType = TimeUnit.HOURS) 缓存1小时
 * <p>
 * 缓存在进行新增,更新,删除是会自动刷新
 *
 * @param <T> 缓存操作对象泛型
 * @author 张亚伟 https://github.com/kaixinzyw/fast-db-template
 */
public class DataCache<T> {

    /**
     * 本地缓存的单条查询数据
     * 适用于@FastStatisCache和@FastRedisLocalCache
     */
    public static Map<String, Cache> cacheOneMap = new HashMap<>();
    /**
     * 本地缓存的单条列表数据
     * 适用于@FastStatisCache和@FastRedisLocalCache
     */
    public static Map<String, Cache> cacheListMap = new HashMap<>();
    /**
     * 本地缓存的单条列表数据
     * 适用于@FastRedisLocalCache
     */
    public static Map<String, Long> localCacheVersion = new HashMap<>();

    /**
     * 缓存键名
     */
    private String keyName;
    /**
     * 缓存的表明
     */
    private String tableName;
    /**
     * 表映射关系
     */
    private TableMapper<T> tableMapper;
    /**
     * @FastRedisLocalCache 中缓存数据的版本号
     */
    private String redisVersionKey;

    /**
     * 初始化缓存
     * @param <T> 缓存操作对象泛型
     * @return 缓存类初始化
     */
    public static <T> DataCache<T> init() {
        FastMapperUtil<T> dataUtil = FastMapperUtil.get();
        DataCache<T> dataCache = dataUtil.getDataCache();
        FastExample<T> fastExample = dataUtil.getFastExample();
        TableMapper<T> tableMapper = dataUtil.getTableMapper();

        dataCache.tableMapper = dataUtil.getTableMapper();
        dataCache.tableName = dataUtil.getTableMapper().getTableName();

        if (fastExample.conditionPackages() == null) {
            dataCache.keyName = "fast_cache_" + tableMapper.getTableName() + ": null";
        } else {
            dataCache.keyName = "fast_cache_" + tableMapper.getTableName() + ": " + JSONUtil.toJsonStr(fastExample.conditionPackages());
        }
        dataCache.redisVersionKey = "fast_cache_version:" + tableMapper.getTableName();
        if (tableMapper.getCacheType().equals(DataCacheType.RedisLocalCache)) {
            dataCache.localCacheVersionInitialize();
        }

        return dataCache;
    }

    /**
     * 当操作对象使用@FastRedisLocalCache会使用此方法进行缓存版本的加载
     */
    private void localCacheVersionInitialize() {
        RedisConnectionFactory connectionFactory = SpringUtil.getBean(RedisConnectionFactory.class);
        if (connectionFactory == null) {
            return;
        }
        if (localCacheVersion.get(tableName) == null) {
            synchronized (("FastCacheTemplate:initialize:" + tableName).intern()) {
                if (localCacheVersion.get(tableName) != null) {
                    return;
                }
                RedisAtomicLong redisAtomicLong = new RedisAtomicLong(redisVersionKey, connectionFactory);
                long version = redisAtomicLong.get();
                localCacheVersion.put(tableName, version);
                return;
            }
        }
    }

    /**
     * 当操作对象使用@FastStatisCache或@FastRedisLocalCache会使用此方法进行本地缓存的创建
     * @return 使用google的guava缓存工具Cache
     */
    private Cache<String, T> createCache() {
        Cache<String, T> cache = CacheBuilder.newBuilder()
                .expireAfterWrite(tableMapper.getCacheTime(), tableMapper.getCacheTimeType())
//                        .concurrencyLevel(10)
                .recordStats()
                .build();
        return cache;
    }

    /**
     * 当操作对象使用@FastStatisCache或@FastRedisLocalCache 并进行单条查询操作,会使用此方法进行本地缓存的创建
     * @return 使用google的guava缓存工具Cache
     */
    public Cache<String, T> createOne() {
        Cache<String, T> cache = cacheOneMap.get(tableName);
        if (cache == null) {
            synchronized (("createOne:" + tableName).intern()) {
                if (cacheOneMap.get(tableName) != null) {
                    return cacheOneMap.get(tableName);
                }
                cacheOneMap.put(tableName, createCache());
            }
            cache = cacheOneMap.get(tableName);
        }
        return cache;
    }

    /**
     * 当操作对象使用@FastStatisCache或@FastRedisLocalCache 并进行列表查询操作,会使用此方法进行本地缓存的创建
     * @return 使用google的guava缓存工具Cache
     */
    public Cache<String, List<T>> createList() {
        Cache<String, List<T>> cache = cacheListMap.get(tableName);
        if (cache == null) {
            synchronized (("createList:" + tableName).intern()) {
                if (cacheListMap.get(tableName) != null) {
                    return cacheListMap.get(tableName);
                }
                cacheListMap.put(tableName, createCache());
            }
            cache = cacheListMap.get(tableName);
        }
        return cache;
    }

    /**
     * 从缓存中获取单条数据
     * @return 查询结果
     */
    public T getOne() {
        T t = null;
        if (tableMapper.getCacheType().equals(DataCacheType.RedisLocalCache) || tableMapper.getCacheType().equals(DataCacheType.StatisCache)) {
            if (tableMapper.getCacheType().equals(DataCacheType.RedisLocalCache)) {
                if (isLocalCacheVersionExpired()) {
                    return null;
                }
            }

            Cache<String, T> one = createOne();
            if (one.size() == 0) {
                return null;
            }
            t = one.getIfPresent(keyName);
        } else if (tableMapper.getCacheType().equals(DataCacheType.RedisCache)) {
            StringRedisTemplate redisTemplate = SpringUtil.getBean(StringRedisTemplate.class);
            String ostr = redisTemplate.opsForValue().get(keyName);
            if (StrUtil.isNotEmpty(ostr)) {
                t = JSONObject.parseObject(ostr, tableMapper.getObjClass());
            }
        }
        return t;

    }

    /**
     * 设置缓存
     * @param t 设置的参数
     */
    public void setOne(T t) {
        if (tableMapper.getCacheType().equals(DataCacheType.RedisLocalCache) || tableMapper.getCacheType().equals(DataCacheType.StatisCache)) {
            Cache<String, T> one = createOne();
            one.put(keyName, t);
        } else if (tableMapper.getCacheType().equals(DataCacheType.RedisCache)) {
            StringRedisTemplate redisTemplate = SpringUtil.getBean(StringRedisTemplate.class);
            redisTemplate.opsForValue().set(keyName, JSONObject.toJSONString(t), tableMapper.getCacheTime(), tableMapper.getCacheTimeType());
        }
    }

    /**
     * 从缓存中获取存储的数据,一般会使用此方法存储count查询结果
     * @return 查询结果
     */
    public T getCount() {
        keyName = keyName + "_count";
        return getOne();

    }

    /**
     * 在缓存中设置存储的数据,一般会使用此方法存储count查询结果
     * @param t 设置的参数
     */
    public void setCount(T t) {
        keyName = keyName + "_count";
        setOne(t);
    }

    /**
     * 从缓存中回去列表数据
     * @return 查询结果
     */
    public List<T> getList() {
        List<T> ts = null;
        if (tableMapper.getCacheType().equals(DataCacheType.RedisLocalCache) || tableMapper.getCacheType().equals(DataCacheType.StatisCache)) {
            if (tableMapper.getCacheType().equals(DataCacheType.RedisLocalCache)) {
                if (isLocalCacheVersionExpired()) {
                    return null;
                }
            }
            Cache<String, List<T>> cache = createList();
            if (cache.size() == 0) {
                return null;
            }
            ts = cache.getIfPresent(keyName);
        } else if (tableMapper.getCacheType().equals(DataCacheType.RedisCache)) {
            StringRedisTemplate redisTemplate = SpringUtil.getBean(StringRedisTemplate.class);
            String ostr = redisTemplate.opsForValue().get(keyName);
            if (StrUtil.isNotEmpty(ostr)) {
                ts = JSONObject.parseArray(ostr, tableMapper.getObjClass());
            }
        }
        return ts;

    }

    /**
     * 设置缓存
     * @param ts 设置的参数
     */
    public void setList(List<T> ts) {
        if (tableMapper.getCacheType().equals(DataCacheType.RedisLocalCache) || tableMapper.getCacheType().equals(DataCacheType.StatisCache)) {
            Cache<String, List<T>> list = createList();
            list.put(keyName, ts);
        } else if (tableMapper.getCacheType().equals(DataCacheType.RedisCache)) {
            StringRedisTemplate redisTemplate = SpringUtil.getBean(StringRedisTemplate.class);
            redisTemplate.opsForValue().set(keyName, JSONObject.toJSONString(ts), tableMapper.getCacheTime(), tableMapper.getCacheTimeType());
        }
    }

    /**
     * 判断是否使用了@FastRedisLocalCache缓存,如果使用,设置Redis数据版本号
     * @return 是否使用@FastRedisLocalCache缓存
     */
    private boolean isLocalCacheVersionExpired() {
        RedisConnectionFactory connectionFactory = SpringUtil.getBean(RedisConnectionFactory.class);
        if (connectionFactory == null) {
            return false;
        }
        RedisAtomicLong redisAtomicLong = new RedisAtomicLong(redisVersionKey, connectionFactory);
        long version = redisAtomicLong.get();
        if (!localCacheVersion.get(tableName).equals(version)) {
            cacheOneMap.remove(tableName);
            cacheListMap.remove(tableName);
            localCacheVersion.put(tableName, version);
            return true;
        }
        return false;
    }

    /**
     * 更细缓存数据,调用时候刷新数据操作对象的缓存信息
     * @param tableMapper 数据操作对象映射信息
     */
    public static void upCacheVersion(TableMapper tableMapper) {
        if (tableMapper.getCacheType() == null) {
            return;
        }
        if (tableMapper.getCacheType().equals(DataCacheType.RedisLocalCache) || tableMapper.getCacheType().equals(DataCacheType.StatisCache)) {
            cacheOneMap.remove(tableMapper.getTableName());
            cacheListMap.remove(tableMapper.getTableName());
            if (tableMapper.getCacheType().equals(DataCacheType.RedisLocalCache)) {
                localCacheVersionController(tableMapper.getTableName());
            }
        } else if (tableMapper.getCacheType().equals(DataCacheType.RedisCache)) {
            StringRedisTemplate redisTemplate = SpringUtil.getBean(StringRedisTemplate.class);
            Set keys = redisTemplate.keys("fast_cache_" + tableMapper.getTableName() + ":*");
            if (CollUtil.isNotEmpty(keys)) {
                redisTemplate.delete(keys);
            }
        }
    }

    /**
     * 当使用了@FastRedisLocalCache缓存,刷新缓存数据时候更新Redis数据版本信息
     * @param tableName
     */
    private static void localCacheVersionController(String tableName) {
        RedisConnectionFactory connectionFactory = SpringUtil.getBean(RedisConnectionFactory.class);
        if (connectionFactory == null) {
            return;
        }

        RedisAtomicLong redisAtomicLong = new RedisAtomicLong("fast_cache_version:" + tableName, connectionFactory);
        long version = redisAtomicLong.incrementAndGet();
        localCacheVersion.put(tableName, version);
    }


}

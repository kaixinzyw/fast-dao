package com.fast.db.template.cache;

import cn.hutool.json.JSONUtil;
import com.fast.db.template.mapper.FastClassTableMapper;
import com.fast.db.template.template.CompoundQuery;
import com.fast.db.template.utils.JedisUtils;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FastCacheTemplate<T> {

    public static Map<String, Cache> cacheOneMap = new HashMap<>();
    public static Map<String, Cache> cacheListMap = new HashMap<>();
    public static Map<String, Long> localCacheVersion = new HashMap<>();


    private String keyName;
    private String tableName;
    private FastClassTableMapper tableMapper;
    private String redisVersionKey;

    public FastCacheTemplate(FastClassTableMapper tableMapper, CompoundQuery compoundQuery) {
        this.tableMapper = tableMapper;
        this.tableName = tableMapper.getTableName();
        this.redisVersionKey = "fast_cache_version:" + tableName;
        if (compoundQuery == null) {
            this.keyName = "null";
        } else {
            this.keyName = JSONUtil.toJsonStr(compoundQuery);
        }
        localCacheVersionInitialize();
    }

    public FastCacheTemplate(FastClassTableMapper tableMapper, CompoundQuery compoundQuery, String keyName) {
        this.tableMapper = tableMapper;
        this.tableName = tableMapper.getTableName();
        this.redisVersionKey = "fast_cache_version:" + tableName;

        if (compoundQuery == null) {
            this.keyName = "null" + keyName;
        } else {
            this.keyName = JSONUtil.toJsonStr(compoundQuery) + keyName;
        }
        localCacheVersionInitialize();
    }

    public FastCacheTemplate(FastClassTableMapper tableMapper, String keyName) {
        this.tableMapper = tableMapper;
        this.tableName = tableMapper.getTableName();
        this.keyName = keyName;
        this.redisVersionKey = "fast_cache_version:" + tableName;
        localCacheVersionInitialize();
    }


    public FastCacheTemplate(String tableName) {
        this.tableName = tableName;
        this.redisVersionKey = "fast_cache_version:" + tableName;
        localCacheVersionInitialize();
    }


    private void localCacheVersionInitialize() {
        if (JedisUtils.jedisPool == null) {
            return;
        }
        if (localCacheVersion.get(tableName) == null) {
            synchronized (("FastCacheTemplate:initialize:" + tableName).intern()) {
                if (localCacheVersion.get(tableName) != null) {
                    return;
                }
                Long incr = JedisUtils.getIncr(redisVersionKey);
                localCacheVersion.put(tableName, incr);
                return;
            }
        }
    }


    private Cache createCache() {
        Cache cache = CacheBuilder.newBuilder()
                .expireAfterWrite(tableMapper.getCacheTime(), tableMapper.getCacheTimeType())
//                        .concurrencyLevel(10)
                .recordStats()
                .build();
        return cache;
    }


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

    public T getOne() {
        if (isLocalCacheVersionExpired()) {
            return null;
        }
        Cache<String, T> one = createOne();
        if (one.size() == 0) {
            return null;
        }
        T t = one.getIfPresent(keyName);
        return t;

    }

    public void setOne(T t) {
        Cache<String, T> one = createOne();
        one.put(keyName, t);

    }


    public List<T> getList() {
        if (isLocalCacheVersionExpired()) {
            return null;
        }
        Cache<String, List<T>> cache = createList();
        if (cache.size() == 0) {
            return null;
        }
        List<T> ts = cache.getIfPresent(keyName);
        return ts;

    }


    public void setList(List<T> ts) {
        Cache<String, List<T>> list = createList();
        list.put(keyName, ts);
    }


    private boolean isLocalCacheVersionExpired() {
        if (JedisUtils.jedisPool == null) {
            return false;
        }
        Long incr = JedisUtils.getIncr(redisVersionKey);
        if (localCacheVersion.get(tableName) != incr) {
            cacheOneMap.remove(tableName);
            cacheListMap.remove(tableName);
            localCacheVersion.put(tableName, incr);
            return true;
        }
        return false;
    }


    public static void upCacheVersion(String tableName) {
        new FastCacheTemplate(tableName).upCacheVersion();
    }

    private void upCacheVersion() {
        cacheOneMap.remove(tableName);
        cacheListMap.remove(tableName);
        localCacheVersionController();
    }

    private void localCacheVersionController() {
        if (JedisUtils.jedisPool == null) {
            return;
        }
        long version = JedisUtils.incr(redisVersionKey);
        localCacheVersion.put(tableName, version);
    }


}

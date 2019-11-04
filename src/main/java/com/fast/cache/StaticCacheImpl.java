package com.fast.cache;

import com.fast.mapper.TableMapper;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StaticCacheImpl {

    /**
     * 本地缓存的单条列表数据
     * 适用于@FastStatisCache
     */
    private static final Map<String, Cache<String, List<?>>> cacheMap = new HashMap<>();

    private static  Cache<String, List<?>> createCache(TableMapper tableMapper) {
        return CacheBuilder.newBuilder()
                .expireAfterWrite(tableMapper.getCacheTime(), tableMapper.getCacheTimeType())
                .recordStats()
                .build();
    }

    public static Cache<String, List<?>> create(TableMapper tableMapper) {
        String tableName = tableMapper.getTableName();
        Cache<String, List<?>> cache = cacheMap.get(tableName);
        if (cache == null) {
            synchronized (("create:" + tableName).intern()) {
                if (cacheMap.get(tableName) != null) {
                    return cacheMap.get(tableName);
                }
                cacheMap.put(tableName, createCache(tableMapper));
            }
            cache = cacheMap.get(tableName);
        }
        return cache;
    }

    public static <T> List<T> get(TableMapper tableMapper, String keyName) {
        Cache<String, List<?>> cache = create(tableMapper);
        if (cache.size() == 0) {
            return null;
        }
        return (List<T>) cache.getIfPresent(keyName);

    }

    public static <T> void set(List<T> ts, TableMapper tableMapper, String keyName) {
        Cache<String, List<?>> list = create(tableMapper);
        list.put(keyName, ts);
    }

    public static void update(TableMapper tableMapper) {
        cacheMap.remove(tableMapper.getTableName());
    }
}

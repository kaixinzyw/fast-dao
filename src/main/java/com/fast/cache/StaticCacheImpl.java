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
     * 适用于@FastStatisCache和@FastRedisLocalCache
     */
    private static final Map<String, Cache<String, List<?>>> cacheMap = new HashMap<>();

    /**
     * 当操作对象使用@FastStatisCache会使用此方法进行本地缓存的创建
     *
     * @return 使用google的guava缓存工具Cache
     */
    private static  Cache<String, List<?>> createCache(TableMapper tableMapper) {
        return CacheBuilder.newBuilder()
                .expireAfterWrite(tableMapper.getCacheTime(), tableMapper.getCacheTimeType())
                .recordStats()
                .build();
    }


    /**
     * 当操作对象使用@FastStatisCache 并进行列表查询操作,会使用此方法进行本地缓存的创建
     *
     * @return 使用google的guava缓存工具Cache
     */
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

    /**
     * 从缓存中回去列表数据
     *
     * @return 查询结果
     */
    public static <T> List<T> get(TableMapper tableMapper, String keyName) {
        Cache<String, List<?>> cache = create(tableMapper);
        if (cache.size() == 0) {
            return null;
        }
        return (List<T>) cache.getIfPresent(keyName);

    }

    /**
     * 设置缓存
     *
     * @param ts 设置的参数
     */
    public static <T> void set(List<T> ts, TableMapper tableMapper, String keyName) {
        Cache<String, List<?>> list = create(tableMapper);
        list.put(keyName, ts);
    }


    /**
     * 更新缓存,对象的缓存信息
     *
     * @param tableMapper 数据操作对象映射信息
     * @return 更新条数
     */
    public static void update(TableMapper tableMapper) {
        cacheMap.remove(tableMapper.getTableName());
    }
}

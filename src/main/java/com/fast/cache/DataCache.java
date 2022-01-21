package com.fast.cache;

import cn.hutool.core.text.StrBuilder;
import com.alibaba.fastjson.JSONObject;
import com.fast.condition.ConditionPackages;
import com.fast.config.FastDaoAttributes;
import com.fast.mapper.TableMapper;

import java.util.List;

/**
 * 缓存实现,目前支持三种缓存
 * 在需要进行缓存的Bean中加入注解
 * 1:@FastRedisCache Redis缓存
 * 2:@FastStatisCache 本地缓存
 * 默认参数为框架设置的缓存时间和类型
 * 缓存可选参数
 * 1:(Long 秒) 如@FastRedisCache(60L) 缓存60秒
 * 2:(cacheTime = 时间,cacheTimeType = TimeUnit) 如@FastRedisCache(cacheTime =1L,cacheTimeType = TimeUnit.HOURS) 缓存1小时
 * <p>
 * 缓存在进行新增,更新,删除是会自动刷新
 *
 * @param <T> 缓存操作对象泛型
 * @author 张亚伟 https://github.com/kaixinzyw
 */
public class DataCache<T> {

    public final static String PREFIX_NAME = "fast_cache_";
    private final static String SEPARATOR = ":";
    /**
     * 缓存键名
     */
    private StrBuilder keyName;
    /**
     * 表映射关系
     */
    private TableMapper tableMapper;

    private Class<T> resultClass;


    public static <T> DataCache<T> init(Class<T> resultClass,ConditionPackages conditionPackages, String prefix) {
        DataCache<T> dataCache = new DataCache<>();
        dataCache.tableMapper = conditionPackages.getTableMapper();
        dataCache.resultClass = conditionPackages.getTableMapper().getObjClass();
        dataCache.keyName = StrBuilder.create(PREFIX_NAME, conditionPackages.getTableMapper().getTableName(), SEPARATOR, prefix, JSONObject.toJSONString(conditionPackages));
        return dataCache;
    }


    /**
     * 从缓存中回去列表数据
     *
     * @return 查询结果
     */
    public List<T> get() {
        if (tableMapper.getCacheType().equals(DataCacheType.StatisCache)) {
            return StaticCacheImpl.get(tableMapper, keyName.toString());
        } else if (tableMapper.getCacheType().equals(DataCacheType.RedisCache)) {
            return RedisCacheImpl.get(resultClass, keyName.toString());
        }
        return null;
    }

    /**
     * 设置缓存
     *
     * @param ts 设置的参数
     */
    public void set(List<T> ts) {
        if (tableMapper.getCacheType().equals(DataCacheType.StatisCache)) {
            StaticCacheImpl.set(ts, tableMapper, keyName.toString());
        } else if (tableMapper.getCacheType().equals(DataCacheType.RedisCache)) {
            RedisCacheImpl.set(ts, tableMapper, keyName.toString());
        }
    }

    /**
     * 更新缓存,对象的缓存信息
     *
     * @param updateCount 更新条数
     * @param tableMapper 数据操作对象映射信息
     * @return 更新条数
     */
    public static int upCache(Integer updateCount, TableMapper tableMapper) {
        if (updateCount < 1 || !FastDaoAttributes.isOpenCache || tableMapper.getCacheType() == null) {
            return updateCount;
        }
        upCache(tableMapper);
        return updateCount;
    }


    /**
     * 更新缓存,对象的缓存信息
     *
     * @param tableMapper 数据操作对象映射信息
     */
    public static void upCache(TableMapper tableMapper) {
        if (!tableMapper.getOpenCache()) {
            return;
        }
        if (tableMapper.getCacheType().equals(DataCacheType.StatisCache)) {
            StaticCacheImpl.update(tableMapper);
        } else if (tableMapper.getCacheType().equals(DataCacheType.RedisCache)) {
            RedisCacheImpl.update(tableMapper);
        }
    }


}

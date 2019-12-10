package com.fast.cache;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.text.StrBuilder;
import cn.hutool.json.JSONUtil;
import com.fast.condition.FastExample;
import com.fast.config.FastDaoAttributes;
import com.fast.mapper.TableMapper;
import io.netty.util.concurrent.FastThreadLocal;

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
    private final static String NULL = "null";
    private final static String COUNT = "_count";
    private final static String SEPARATOR = ": ";
    /**
     * 缓存键名
     */
    private StrBuilder keyName;
    /**
     * 缓存的表明
     */
    private String tableName;
    /**
     * 表映射关系
     */
    private TableMapper<T> tableMapper;

    private static final FastThreadLocal<DataCache> dataCacheThreadLocal = new FastThreadLocal<>();

    public static <T> DataCache<T> init(TableMapper tableMapper, FastExample fastExample) {
        DataCache<T> dataCache = dataCacheThreadLocal.get();
        if (dataCache == null) {
            dataCache = new DataCache<>();
            dataCacheThreadLocal.set(dataCache);
        }
        dataCache.tableMapper = tableMapper;
        dataCache.tableName = tableMapper.getTableName();

        if (fastExample.conditionPackages() == null) {
            dataCache.keyName = StrBuilder.create(PREFIX_NAME,tableMapper.getTableName(),SEPARATOR,NULL);
        } else {
            dataCache.keyName = StrBuilder.create(PREFIX_NAME, tableMapper.getTableName(),SEPARATOR,JSONUtil.toJsonStr(fastExample.conditionPackages()));
        }

        return dataCache;
    }

    /**
     * 从缓存中获取单条数据
     *
     * @return 查询结果
     */
    public T getOne() {
        if (tableMapper.getCacheType().equals(DataCacheType.StatisCache)) {
            List<T> ts = StaticCacheImpl.get(tableMapper, keyName.toString());
            if (CollUtil.isEmpty(ts)) {
                return null;
            }
            return ts.get(0);
        } else if (tableMapper.getCacheType().equals(DataCacheType.RedisCache)) {
            List<T> ts = RedisCacheImpl.<T>get(tableMapper, keyName.toString());
            if (CollUtil.isEmpty(ts)) {
                return null;
            }
            return ts.get(0);
        }
        return null;
    }

    /**
     * 设置缓存
     *
     * @param t 设置的参数
     */
    public void setOne(T t) {
        if (tableMapper.getCacheType().equals(DataCacheType.StatisCache)) {
            StaticCacheImpl.set(CollUtil.newArrayList(t),tableMapper,keyName.toString());
        } else if (tableMapper.getCacheType().equals(DataCacheType.RedisCache)) {
            RedisCacheImpl.set(CollUtil.newArrayList(t),tableMapper,keyName.toString());
        }
    }

    /**
     * 从缓存中获取存储的数据,一般会使用此方法存储count查询结果
     *
     * @return 查询结果
     */
    public T getCount() {
        keyName.append(COUNT);
        return getOne();

    }

    /**
     * 在缓存中设置存储的数据,一般会使用此方法存储count查询结果
     *
     * @param t 设置的参数
     */
    public void setCount(T t) {
        keyName.append(COUNT);
        setOne(t);
    }

    /**
     * 从缓存中回去列表数据
     *
     * @return 查询结果
     */
    public List<T> getList() {
        if (tableMapper.getCacheType().equals(DataCacheType.StatisCache)) {
            return StaticCacheImpl.get(tableMapper,keyName.toString());
        } else if (tableMapper.getCacheType().equals(DataCacheType.RedisCache)) {
            return RedisCacheImpl.get(tableMapper,keyName.toString());
        }
        return null;
    }

    /**
     * 设置缓存
     *
     * @param ts 设置的参数
     */
    public void setList(List<T> ts) {
        if (tableMapper.getCacheType().equals(DataCacheType.StatisCache)) {
            StaticCacheImpl.set(ts,tableMapper,keyName.toString());
        } else if (tableMapper.getCacheType().equals(DataCacheType.RedisCache)) {
            RedisCacheImpl.set(ts,tableMapper,keyName.toString());
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
        if (tableMapper.getCacheType() == null) {
            return;
        }
        if (tableMapper.getCacheType().equals(DataCacheType.StatisCache)) {
            StaticCacheImpl.update(tableMapper);
        } else if (tableMapper.getCacheType().equals(DataCacheType.RedisCache)) {
            RedisCacheImpl.update(tableMapper);
        }
    }


}

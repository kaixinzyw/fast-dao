package com.fast.db.template.mapper;

import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

/**
 * 表和对象关系映射对象池
 *
 * @author 张亚伟 https://github.com/kaixinzyw/fast-db-template
 */
public class FastMapperUtilFactory extends BasePooledObjectFactory<FastMapperUtil> {
    /**
     * 对象池
     */
    private static GenericObjectPool<FastMapperUtil> pool;

    /**
     * 对象池的参数设置
     */
    private static final GenericObjectPoolConfig config;

    /**
     * 对象池每个key最大实例化对象数
     */
    private final static int TOTAL_PERKEY = -1;
    /**
     * 对象池每个key最少需要容纳的实例个数
     */
    private final static int MAX_IDLE = 200;
    private final static int IDLE_PERKEY = 100;

    static {
        config = new GenericObjectPoolConfig();
        config.setMaxTotal(TOTAL_PERKEY);
        config.setMaxIdle(MAX_IDLE);
        config.setMinIdle(IDLE_PERKEY);
        pool = new GenericObjectPool<>(new FastMapperUtilFactory(), config);
    }


    public static <T> FastMapperUtil<T> getUtil() throws Exception {
        return pool.borrowObject();
    }

    @Override
    public FastMapperUtil create() {
        return FastMapperUtil.create();
    }

    @Override
    public PooledObject<FastMapperUtil> wrap(FastMapperUtil value) {
        return new DefaultPooledObject<>(value);
    }

}

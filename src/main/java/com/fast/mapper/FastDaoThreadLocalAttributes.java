package com.fast.mapper;

import com.fast.cache.DataCache;
import com.fast.config.FastDaoAttributes;
import com.fast.dao.DaoActuator;
import com.fast.dao.jdbc.SpringJDBCMySqlImpl;
import com.fast.dao.mybatis.FastMyBatisConnection;
import com.fast.dao.mybatis.FastMyBatisImpl;
import com.fast.dao.mybatis.FastMyBatisMapper;
import com.fast.example.*;
import io.netty.util.concurrent.FastThreadLocal;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.support.atomic.RedisAtomicLong;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import javax.sql.DataSource;

/**
 * 表和对象关系映射生成工具
 *
 * @author 张亚伟 https://github.com/kaixinzyw
 */
public class FastDaoThreadLocalAttributes<Pojo> {

    /**
     * 线程缓存
     */
    private static final FastThreadLocal<FastDaoThreadLocalAttributes> threadLocal = new FastThreadLocal<>();

    /**
     * 模板执行器
     */
    private FastDao<Pojo> fastDao;
    /**
     * 自定义SQL执行器
     */
    private FastCustomSqlDao<Pojo> fastCustomSqlDao;
    /**
     * 模板操作执行器
     */
    private DaoTemplate<Pojo> daoTemplate;
    /**
     * ORM实现执行器
     */
    private DaoActuator<Pojo> daoActuator;
    /**
     * 属性和表的映射
     */
    private TableMapper<Pojo> tableMapper;
    /**
     * 数据缓存执行器
     */
    private DataCache<Pojo> dataCache;
    /**
     * 条件封装器
     */
    private FastDaoParam<Pojo> daoParam;
    /**
     * 条件设置器
     */
    private FastExample<Pojo> fastExample;

    /**
     * MyBatis插件模式使用
     */
    private FastMyBatisMapper myBatisMapper;

    /**
     * JDBC框架模式使用
     */
    private NamedParameterJdbcTemplate jdbcTemplate;

    /**
     * 使用Redis缓存时使用
     */
    private StringRedisTemplate redisTemplate;


    /**
     * 初始化FastDao执行器
     *
     * @param clazz       需要操作的对象
     * @param fastExample 条件封装
     * @param <T>         操作对象的泛型
     * @return 初始化
     */
    public static <T> FastDao<T> fastDao(Class<T> clazz, FastExample<T> fastExample) {
        FastDaoThreadLocalAttributes<T> dataUtil = FastDaoThreadLocalAttributes.init(clazz, fastExample);
        return dataUtil.fastDao;
    }

    /**
     * 初始化FastDao执行器
     *
     * @param clazz 需要操作的对象
     * @param <T>   操作对象的泛型
     * @return 初始化
     */
    public static <T> FastDao<T> fastDao(Class<T> clazz) {
        FastDaoThreadLocalAttributes<T> dataUtil = FastDaoThreadLocalAttributes.init(clazz, new FastExample<>(clazz));
        return dataUtil.fastDao;
    }

    /**
     * 初始化自定义SQL执行器
     *
     * @param clazz       需要操作的对象
     * @param fastExample 条件封装
     * @param <T>         操作对象的泛型
     * @return 初始化
     */
    public static <T> FastCustomSqlDao<T> fastCustomSqlDao(Class<T> clazz, FastExample<T> fastExample) {
        FastDaoThreadLocalAttributes<T> dataUtil = FastDaoThreadLocalAttributes.init(clazz, fastExample);
        return dataUtil.fastCustomSqlDao;
    }


    public static StringRedisTemplate redisTemplate() {
        StringRedisTemplate redisTemplate = FastDaoThreadLocalAttributes.get().redisTemplate;
        if (redisTemplate == null) {
            redisTemplate = FastDaoThreadLocalAttributes.get().redisTemplate = new StringRedisTemplate(FastDaoAttributes.getRedisConnectionFactory());
        }
        return redisTemplate;
    }

    public static RedisAtomicLong redisAtomicLong() {
        return new RedisAtomicLong("fast_cache_version:" + FastDaoThreadLocalAttributes.get().tableMapper.getTableName(),
                FastDaoAttributes.getRedisConnectionFactory());
    }

    /**
     * 获取执行器封装信息
     *
     * @param <T> 执行器需要操作的对象
     * @return 初始化
     */
    public static <T> FastDaoThreadLocalAttributes<T> get() {
        FastDaoThreadLocalAttributes threadLocalAttributes = threadLocal.get();
        if (threadLocalAttributes == null) {
            threadLocalAttributes = init(null, null);
        }
        return threadLocalAttributes;
    }

    /**
     * 模板所有执行器初始化
     *
     * @param clazz       执行器操作的Class
     * @param fastExample 条件封装
     * @param <T>         执行器操作的对象泛型
     * @return 初始化
     */
    private static <T> FastDaoThreadLocalAttributes<T> init(Class<T> clazz, FastExample<T> fastExample) {
        FastDaoThreadLocalAttributes<T> fastDaoThreadLocalAttributes = threadLocal.get();
        if (fastDaoThreadLocalAttributes == null) {
            fastDaoThreadLocalAttributes = FastDaoAttributesBeanFactory.getBean();
            threadLocal.set(fastDaoThreadLocalAttributes);
        }
        fastDaoThreadLocalAttributes.fastExample = fastExample;
        if (clazz == null) {
            return fastDaoThreadLocalAttributes;
        }
        if (fastDaoThreadLocalAttributes.tableMapper != null && fastDaoThreadLocalAttributes.tableMapper.getClassName().equals(clazz.getSimpleName())) {
            return fastDaoThreadLocalAttributes;
        }
        fastDaoThreadLocalAttributes.tableMapper = TableMapperUtil.getTableMappers(clazz);
        return fastDaoThreadLocalAttributes;
    }

    /**
     * 执行器封装创建
     *
     * @param <T> 执行器需要操作的对象
     * @return 初始化
     */
    public static <T> FastDaoThreadLocalAttributes<T> create() {
        FastDaoThreadLocalAttributes<T> fastDaoThreadLocalAttributes = new FastDaoThreadLocalAttributes<>();
        fastDaoThreadLocalAttributes.daoParam = new FastDaoParam<>();
        fastDaoThreadLocalAttributes.fastDao = new FastDao<>();
        fastDaoThreadLocalAttributes.fastCustomSqlDao = new FastCustomSqlDao<>();
        if (FastDaoAttributes.getDaoActuator() == FastMyBatisImpl.class) {
            DataSource dataSource = FastDaoAttributes.getDataSource();
            fastDaoThreadLocalAttributes.myBatisMapper = FastMyBatisConnection.mapperPack(dataSource);
        } else if (FastDaoAttributes.getDaoActuator() == SpringJDBCMySqlImpl.class) {
            DataSource dataSource = FastDaoAttributes.getDataSource();
            fastDaoThreadLocalAttributes.jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
        }
        if (FastDaoAttributes.isOpenCache) {
            fastDaoThreadLocalAttributes.dataCache = new DataCache<>();
        }
        try {
            fastDaoThreadLocalAttributes.daoActuator = FastDaoAttributes.getDaoActuator().newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        fastDaoThreadLocalAttributes.daoTemplate = new DaoTemplate<>(fastDaoThreadLocalAttributes.daoActuator);
        return fastDaoThreadLocalAttributes;
    }

    public void setDataSourceThreadLocal(DataSource dataSource) {
        if (dataSource == null) {
            dataSource = FastDaoAttributes.getDataSource();
        }
        if (FastDaoAttributes.getDaoActuator() == FastMyBatisImpl.class) {
            this.myBatisMapper = FastMyBatisConnection.mapperPack(dataSource);
        } else if (FastDaoAttributes.getDaoActuator() == SpringJDBCMySqlImpl.class) {
            this.jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
        }
    }


    public DaoTemplate<Pojo> getDaoTemplate() {
        return daoTemplate;
    }

    public FastDao<Pojo> getFastDao() {
        return fastDao;
    }

    public DaoActuator<Pojo> getDaoActuator() {
        return daoActuator;
    }

    public TableMapper<Pojo> getTableMapper() {
        return tableMapper;
    }

    public DataCache<Pojo> getDataCache() {
        return dataCache;
    }

    public FastDaoParam<Pojo> getDaoParam() {
        return daoParam;
    }

    public FastExample<Pojo> getFastExample() {
        return fastExample;
    }

    public FastCustomSqlDao<Pojo> getFastCustomSqlDao() {
        return fastCustomSqlDao;
    }

    public FastMyBatisMapper getMyBatisMapper() {
        return myBatisMapper;
    }

    public NamedParameterJdbcTemplate getJdbcTemplate() {
        return jdbcTemplate;
    }


}

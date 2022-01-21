package com.fast.config;

import com.fast.dao.transaction.FastTransaction;
import com.fast.utils.SpringUtil;
import io.netty.util.concurrent.FastThreadLocal;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 框架配置参数
 *
 * @author 张亚伟 https://github.com/kaixinzyw
 */
public class FastDaoAttributes {

    /**
     * 设置SQL日志级别
     */
    public static SqlLogLevel sqlLogLevel = SqlLogLevel.OFF;

    /**
     * 是否打印简易格式SQL
     */
    public static Boolean isSqlSimplePrint = Boolean.TRUE;
    /**
     * 是否打印SQL执行结果
     */
    public static Boolean isSqlPrintResult = Boolean.FALSE;

    /**
     * 是否自动设置数据创建时间,目前只支持Date类型
     */
    public static Boolean isAutoSetCreateTime = Boolean.FALSE;
    /**
     * 创建时间字段名称
     */
    public static String createTimeFieldName;
    /**
     * 创建时间对应的表字段名称
     */
    public static String createTimeTableColumnName;

    /**
     * 是否自动设置数据更新时间,目前只支持Date类型
     */
    public static Boolean isAutoSetUpdateTime = Boolean.FALSE;
    /**
     * 更新时间字段名称
     */
    public static String updateTimeFieldName;
    /**
     * 更新时间对应的表字段名称
     */
    public static String updateTimeTableColumnName;


    /**
     * 是否启用逻辑删除,目前只支持Boolean
     */
    public static Boolean isOpenLogicDelete = Boolean.FALSE;
    /**
     * 逻辑删除的字段名称
     */
    public static String deleteFieldName;

    /**
     * 排除字段,排除的字段不会进行表映射
     */
    public static List<String> ruleOutFieldList = new ArrayList<>();
    /**
     * 逻辑删除的表名称
     */
    public static String deleteTableColumnName;
    /**
     * 逻辑删除标记值
     */
    public static Boolean defaultDeleteValue = Boolean.TRUE;

    public static String defaultSqlWhereDeleteValueTrue;
    public static String defaultSqlWhereDeleteValueFalse;

    /**
     * sql模板前缀
     */
    public static String sqlTemplatePrefix = "";
    /**
     * sql模板后缀
     */
    public static String sqlTemplateSuffix = "";


    /**
     * 是否开启缓存功能
     */
    public static Boolean isOpenCache = Boolean.TRUE;
    /**
     * 缓存时间
     */
    public static Long defaultCacheTime = 10L;
    /**
     * 缓存时间类型
     */
    public static TimeUnit defaultCacheTimeType = TimeUnit.SECONDS;

    /**
     * 是否启用驼峰转换
     */
    public static Boolean isToCamelCase = Boolean.TRUE;

    /**
     * 数据源
     */
    private static DataSource dataSource;
    private static final FastThreadLocal<DataSource> DATA_SOURCE_FAST_THREAD_LOCAL = new FastThreadLocal<>();
    private static final FastThreadLocal<NamedParameterJdbcTemplate> JDBC_TEMPLATE_THREAD_LOCAL = new FastThreadLocal<>();

    public static NamedParameterJdbcTemplate getJdbcTemplate() {
        NamedParameterJdbcTemplate jdbcTemplate = JDBC_TEMPLATE_THREAD_LOCAL.get();
        if (jdbcTemplate == null) {
            jdbcTemplate = new NamedParameterJdbcTemplate(FastDaoAttributes.getDataSource());
            JDBC_TEMPLATE_THREAD_LOCAL.set(jdbcTemplate);
        }
        return jdbcTemplate;
    }

    public static DataSource getDataSource() {
        DataSource dataSource = DATA_SOURCE_FAST_THREAD_LOCAL.get();
        if (dataSource != null) {
            return dataSource;
        }
        dataSource = FastDaoAttributes.dataSource;
        if (dataSource == null) {
            dataSource = SpringUtil.getBean(DataSource.class);
            if (dataSource == null) {
                throw new RuntimeException("未获取到DataSource,请使用<FastDaoConfig.dataSource(dataSource)>进行配置");
            }
        }
        return dataSource;
    }

    public static void setDataSource(DataSource dataSource) {
        FastDaoAttributes.dataSource = dataSource;
        switchDataSource();
    }

    public static void setThreadLocalDataSource(DataSource dataSource) {
        DATA_SOURCE_FAST_THREAD_LOCAL.set(dataSource);
        switchDataSource();
    }

    private static void switchDataSource() {
        FastTransaction.switchTransaction();
        JDBC_TEMPLATE_THREAD_LOCAL.remove();
    }

    /**
     * Redis
     */
    private static RedisConnectionFactory redisConnectionFactory;
    private static final FastThreadLocal<StringRedisTemplate> STRING_REDIS_TEMPLATE_FAST_THREAD_LOCAL = new FastThreadLocal<>();

    public static StringRedisTemplate getStringRedisTemplate() {
        StringRedisTemplate stringRedisTemplate = STRING_REDIS_TEMPLATE_FAST_THREAD_LOCAL.get();
        if (stringRedisTemplate == null) {
            stringRedisTemplate = new StringRedisTemplate(FastDaoAttributes.getRedisConnectionFactory());
            STRING_REDIS_TEMPLATE_FAST_THREAD_LOCAL.set(stringRedisTemplate);
        }
        return stringRedisTemplate;
    }

    private static RedisConnectionFactory getRedisConnectionFactory() {
        RedisConnectionFactory rcf = FastDaoAttributes.redisConnectionFactory;
        if (rcf == null) {
            rcf = SpringUtil.getBean(RedisConnectionFactory.class);
            if (rcf == null) {
                throw new RuntimeException("未获取到RedisConnectionFactory,请使用<FastDaoConfig.redisConnectionFactory(redisConnectionFactory)>进行配置");
            }
        }
        return rcf;
    }

    public static void setRedisConnectionFactory(RedisConnectionFactory redisConnectionFactory) {
        FastDaoAttributes.redisConnectionFactory = redisConnectionFactory;
    }
}


package com.fast.config;

import cn.hutool.core.util.ObjectUtil;
import com.fast.dao.DaoActuator;
import com.fast.dao.jdbc.JdbcConnection;
import com.fast.dao.jdbc.JdbcImpl;
import com.fast.dao.mybatis.MyBatisConnection;
import com.fast.dao.mybatis.MyBatisImpl;
import com.fast.dao.transaction.FastTransaction;
import com.fast.utils.SpringUtil;
import io.netty.util.concurrent.FastThreadLocal;
import org.springframework.data.redis.connection.RedisConnectionFactory;

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
     * 使用的OMR实现,目前框架自身对SpringJDBC和MyBatis 进行了Mysql实现
     * 参数
     * JdbcImpl.class:使用JDBC实现
     * MyBatisImpl.class: 使用MyBatis实现
     * <p>
     * 如果需要自定义扩展,可以实现DaoActuator接口进行自定义扩展
     */
    private static Class<? extends DaoActuator> daoActuator = JdbcImpl.class;

    public static <T> DaoActuator<T> getDaoActuator() {
        try {
            return daoActuator.newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static Class<? extends DaoActuator> getDaoActuatorClass() {
        return FastDaoAttributes.daoActuator;
    }

    public static void setDaoActuator(Class<? extends DaoActuator> dbActuator) {
        FastDaoAttributes.daoActuator = dbActuator;
    }

    /**
     * 数据源
     */
    private static DataSource dataSource;
    private static final FastThreadLocal<DataSource> dataSourceFastThreadLocal = new FastThreadLocal<>();
    public static DataSource getDataSource() {
        DataSource dataSource = dataSourceFastThreadLocal.get();
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
        dataSourceFastThreadLocal.set(dataSource);
        switchDataSource();
    }

    private static void switchDataSource(){
        if (ObjectUtil.equal(FastDaoAttributes.daoActuator, JdbcImpl.class)) {
            JdbcConnection.dataSource();
        }else if (ObjectUtil.equal(FastDaoAttributes.daoActuator, MyBatisImpl.class)) {
            MyBatisConnection.dataSource();
        }
        FastTransaction.switchTransaction();
    }

    /**
     * Redis
     */
    private static RedisConnectionFactory redisConnectionFactory;

    public static RedisConnectionFactory getRedisConnectionFactory() {
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


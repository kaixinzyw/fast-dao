package com.fast.config;

import com.fast.aspect.FastDaoExpander;
import com.fast.aspect.FastDaoExpanderRunner;
import com.fast.dao.DaoActuator;
import com.fast.dao.jdbc.JdbcConnection;
import com.fast.utils.FastValueUtil;
import org.springframework.data.redis.connection.RedisConnectionFactory;

import javax.sql.DataSource;
import java.util.concurrent.TimeUnit;

public class FastDaoConfig {

    /**
     * @param daoImpl 使用的ORM实现 ,框架自身可选值,不设置优先使用jdbc实现
     *                1:JdbcImpl.class:使用JDBC框架模式
     *                2:FastMyBatisImpl.class: 使用MyBatis插件模式
     */
    public static void daoActuator(Class<? extends DaoActuator> daoImpl) {
        FastDaoAttributes.setDaoActuator(daoImpl);
    }

    /**
     * 配置全局数据源
     *
     * @param dataSource 数据源
     */
    public static void dataSource(DataSource dataSource) {
        FastDaoAttributes.setDataSource(dataSource);
        dataSourceThreadLocal(dataSource);
    }


    /**
     * 配置当前线程数据源
     *
     * @param dataSource 数据源
     */
    public static void dataSourceThreadLocal(DataSource dataSource) {
            JdbcConnection.dataSource(dataSource);
    }


    /**
     * 配置全局Redis连接工厂
     *
     * @param redisConnectionFactory 使用自定义Redis连接工厂
     */
    public static void redisConnectionFactory(RedisConnectionFactory redisConnectionFactory) {
        FastDaoAttributes.setRedisConnectionFactory(redisConnectionFactory);
    }

    /**
     * 开启SQL日志打印,默认使用项目的日志框架
     *
     * @param sqlLogLevel    SQL日志级别
     * @param isSimplePrint 是否打印简单格式SQL,简单格式消耗性能低
     * @param isPrintResult 是否打印SQL执行结果
     */
    public static void openSqlPrint(SqlLogLevel sqlLogLevel, Boolean isSimplePrint, Boolean isPrintResult) {
        FastDaoAttributes.sqlLogLevel = sqlLogLevel;
        FastDaoAttributes.isSqlSimplePrint = isSimplePrint;
        FastDaoAttributes.isSqlPrintResult = isPrintResult;
    }

    /**
     * 开启自动对数据 新增操作 进行创建时间设置
     *
     * @param createTimeTableColumnName 对应数据库中的列名
     */
    public static void openAutoSetCreateTime(String createTimeTableColumnName) {
        FastDaoAttributes.isAutoSetCreateTime = Boolean.TRUE;
        FastDaoAttributes.createTimeTableColumnName = createTimeTableColumnName;
        FastDaoAttributes.createTimeFieldName = FastValueUtil.toCamelCase(createTimeTableColumnName);
    }

    /**
     * 开启自动对数据 更新操作|逻辑删除操作 进行更新时间设置
     *
     * @param updateTimeTableColumnName 对应数据库中的列名
     */
    public static void openAutoSetUpdateTime(String updateTimeTableColumnName) {
        FastDaoAttributes.isAutoSetUpdateTime = Boolean.TRUE;
        FastDaoAttributes.updateTimeTableColumnName = updateTimeTableColumnName;
        FastDaoAttributes.updateTimeFieldName = FastValueUtil.toCamelCase(updateTimeTableColumnName);
    }

    /**
     * 开启逻辑删除功能,开启后会对逻辑删除标记的数据在 更新|删除|查询 时进行保护,可通过模板进行单次操作逻辑删除保护的关闭
     *
     * @param deleteTableColumnName 对应数据库中的列名
     * @param defaultDeleteValue    逻辑删除默认值
     */
    public static void openLogicDelete(String deleteTableColumnName, Boolean defaultDeleteValue) {
        FastDaoAttributes.isOpenLogicDelete = Boolean.TRUE;
        FastDaoAttributes.defaultDeleteValue = defaultDeleteValue;
        FastDaoAttributes.deleteTableColumnName = deleteTableColumnName;
        FastDaoAttributes.deleteFieldName = FastValueUtil.toCamelCase(deleteTableColumnName);
        FastDaoAttributes.defaultSqlWhereDeleteValueTrue = "`" + FastDaoAttributes.deleteTableColumnName + "` = true";
        FastDaoAttributes.defaultSqlWhereDeleteValueFalse = "`" + FastDaoAttributes.deleteTableColumnName + "` = false";
    }

    /**
     * 设置全局缓存时间,开启后可使用@FastRedisCache,@FastStatisCache 两种数据缓存类型
     *
     * @param defaultCacheTime     默认全局缓存时间
     * @param defaultCacheTimeType 默认全局缓存时间类型
     */
    public static void openCache(Long defaultCacheTime, TimeUnit defaultCacheTimeType) {
        FastDaoAttributes.defaultCacheTime = defaultCacheTime;
        FastDaoAttributes.defaultCacheTimeType = defaultCacheTimeType;
    }

    /**
     * 开启字段驼峰转换 例 user_name = userName
     */
    public static void openToCamelCase() {
        FastDaoAttributes.isToCamelCase = Boolean.TRUE;
    }

    /**
     * 关闭字段驼峰转换
     */
    public static void closeCamelCase() {
        FastDaoAttributes.isToCamelCase = Boolean.FALSE;
    }

    /**
     * 添加执行器
     *
     * @param expanderClass 实现FastDaoExpander的扩展类
     */
    public static void addFastDaoExpander(Class<? extends FastDaoExpander> expanderClass) {
        if (expanderClass != null) {
            if (expanderClass.isInterface()) {
                return;
            }
            FastDaoExpanderRunner.addFastDaoExpander((Class<FastDaoExpander>) expanderClass);
        }
    }

}

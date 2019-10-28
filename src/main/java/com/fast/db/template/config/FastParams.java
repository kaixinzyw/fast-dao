package com.fast.db.template.config;

import com.fast.db.template.dao.DaoActuator;
import com.fast.db.template.dao.jdbc.SpringJDBCMySqlImpl;

import java.util.concurrent.TimeUnit;

/**
 * 框架配置参数
 * @author 张亚伟 https://github.com/kaixinzyw/fast-db-template
 */
public class FastParams {

    /**
     * 是否日志打印Sql语句
     * 使用项目设置的日志框架
     */
    public static Boolean isSqlPrint = Boolean.FALSE;
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
     * 是否自动设置指定主键信息,目前强制使用
     */
    public static Boolean isAutoSetPrimaryKey = Boolean.TRUE;

    /**
     * 主键类型,参考枚举PrimaryKeyType 目前有32位UUID和自增
     */
    public static PrimaryKeyType primaryKeyType = PrimaryKeyType.AUTO;
    /**
     * 指定设置的主键字段名称
     */
    public static String primaryKeyFieldName = "id";
    /**
     * 指定设置主键的表名称
     */
    public static String primaryKeyTableColumnName = "id";

    /**
     * 是否启用逻辑删除,目前只支持Boolean
     */
    public static Boolean isOpenLogicDelete = Boolean.FALSE;
    /**
     * 逻辑删除的字段名称
     */
    public static String deleteFieldName;
    /**
     * 逻辑删除的表名称
     */
    public static String deleteTableColumnName;
    /**
     * 逻辑删除标记值
     */
    public static Boolean defaultDeleteValue = Boolean.TRUE;

    /**
     * 是否开启缓存功能
     */
    public static Boolean isOpenCache = Boolean.FALSE;
    /**
     * 缓存时间
     */
    public static Long defaultCacheTime;
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
     * SpringJDBCMySqlImpl.class:使用SpringJDBC实现
     * FastMyBatisImpl.class: 使用MyBatis实现
     *
     * 如果需要自定义扩展,可以实现DaoActuator接口进行自定义扩展
     */
    private static Class<? extends DaoActuator> dbActuator = SpringJDBCMySqlImpl.class;
    public static Class<? extends DaoActuator> getDBActuator() {
        return dbActuator;
    }
    public static void setDBActuator(Class<? extends DaoActuator> dbActuator) {
        FastParams.dbActuator = dbActuator;
    }

}


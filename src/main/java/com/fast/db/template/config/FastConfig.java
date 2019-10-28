package com.fast.db.template.config;

import com.fast.db.template.dao.DaoActuator;
import com.fast.db.template.utils.FastValueUtil;

import java.util.concurrent.TimeUnit;

public class FastConfig {

    /**
     * 默认使用Spring-Jdbc,可通过setDaoActuator进行设置
     * 默认使用主键列为自增 "id",可通过openAutoSetPrimaryKey进行设置
     */
    public FastConfig() {}

    /**
     * @param mapperImpl 使用的ORM实现 ,框架自身可选值,不设置优先使用spring-jdbc实现
     *                   1:SpringJDBCMySqlImpl.class:使用SpringJDBC实现
     *                   2:FastMyBatisImpl.class: 使用MyBatis实现
     */
    public void daoActuator(Class<? extends DaoActuator> mapperImpl) {
        FastParams.setDBActuator(mapperImpl);
    }

    /**
     * 默认设置
     * 开启字段下划线转换 例 user_name = userName
     * 开启数据自动创建时间设置,字段名为createTime
     * 开启数据自动更新设置,updateTime
     * 开启数据逻辑删除,字段名为deleted,删除标记值为true
     */
    public void defaultSetting() {
        openAutoSetCreateTime("create_time");
        openAutoSetUpdateTime("update_time");
        openLogicDelete("deleted", Boolean.TRUE);
    }

    /**
     * 开启SQL日志打印,默认使用项目的日志框架
     *
     * @param isPrint       是否打印SQL语句
     * @param isPrintResult 是否打印SQL执行结果
     */
    public void openSqlPrint(Boolean isPrint, Boolean isPrintResult) {
        FastParams.isSqlPrint = isPrint;
        FastParams.isSqlPrintResult = isPrintResult;
    }

    /**
     * 开启自动对数据 新增操作 进行创建时间设置
     *
     * @param createTimeTableColumnName 对应数据库中的列名
     */
    public void openAutoSetCreateTime(String createTimeTableColumnName) {
        FastParams.isAutoSetCreateTime = Boolean.TRUE;
        FastParams.createTimeTableColumnName = createTimeTableColumnName;
        FastParams.createTimeFieldName = FastValueUtil.toCamelCase(createTimeTableColumnName);
    }

    /**
     * 开启自动对数据 更新操作|逻辑删除操作 进行更新时间设置
     *
     * @param updateTimeTableColumnName 对应数据库中的列名
     */
    public void openAutoSetUpdateTime(String updateTimeTableColumnName) {
        FastParams.isAutoSetUpdateTime = Boolean.TRUE;
        FastParams.updateTimeTableColumnName = updateTimeTableColumnName;
        FastParams.updateTimeFieldName = FastValueUtil.toCamelCase(updateTimeTableColumnName);
    }

    /**
     * 开启 设置模板操作的主键信息,默契在创建框架配置时候强制开启,暂无需单独设置
     *
     * @param primaryKeyTableColumnName 对应数据库中的列名
     * @param keyType                   主键类型
     */
    public void openAutoSetPrimaryKey(String primaryKeyTableColumnName, PrimaryKeyType keyType) {
        FastParams.isAutoSetPrimaryKey = Boolean.TRUE;
        FastParams.primaryKeyType = keyType;

        FastParams.primaryKeyTableColumnName = primaryKeyTableColumnName;
        FastParams.primaryKeyFieldName = FastValueUtil.toCamelCase(primaryKeyTableColumnName);
    }

    /**
     * 开启逻辑删除功能,开启后会对逻辑删除标记的数据在 更新|删除|查询 时进行保护,可通过模板进行单次操作逻辑删除保护的关闭
     *
     * @param deleteTableColumnName 对应数据库中的列名
     * @param defaultDeleteValue    逻辑删除默认值
     */
    public void openLogicDelete(String deleteTableColumnName, Boolean defaultDeleteValue) {
        FastParams.isOpenLogicDelete = Boolean.TRUE;
        FastParams.defaultDeleteValue = defaultDeleteValue;

        FastParams.deleteTableColumnName = deleteTableColumnName;
        FastParams.deleteFieldName = FastValueUtil.toCamelCase(deleteTableColumnName);
    }

    /**
     * 开启框架缓存功能,开启后可使用@FastRedisCache,@FastRedisLocalCache,@FastStatisCache 三种数据缓存类型
     *
     * @param defaultCacheTime     默认全局缓存时间
     * @param defaultCacheTimeType 默认全局缓存时间类型
     */
    public void openCache(Long defaultCacheTime, TimeUnit defaultCacheTimeType) {
        FastParams.isOpenCache = Boolean.TRUE;
        FastParams.defaultCacheTime = defaultCacheTime;
        FastParams.defaultCacheTimeType = defaultCacheTimeType;
    }

    /**
     * 开启字段驼峰转换 例 user_name = userName
     */
    public void openToCamelCase() {
        FastParams.isToCamelCase = Boolean.TRUE;
    }

    /**
     * 关闭字段驼峰转换
     */
    public void closeCamelCase() {
        FastParams.isToCamelCase = Boolean.FALSE;
    }


}

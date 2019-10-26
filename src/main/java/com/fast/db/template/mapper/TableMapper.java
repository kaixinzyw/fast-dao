package com.fast.db.template.mapper;

import com.fast.db.template.cache.DataCacheType;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 存储表和对象关系映射
 *
 * @author 张亚伟 https://github.com/kaixinzyw/fast-db-template
 */
public class TableMapper<T> {

    /**
     * 类名
     */
    private String className;

    /**
     * 表名称
     */
    private String tableName;

    /**
     * 字段集合
     */
    private List<String> fieldNames;

    /**
     * 字段类型集合
     */
    private Map<String, Class> fieldTypes;

    /**
     * <字段名,表列名>
     */
    private Map<String, String> fieldTableNames;
    /**
     * <表列名,字段名>
     */
    private Map<String, String> tableFieldNames;
    /**
     * select查询时候现实的列名
     */
    private Map<String, String> showTableNames;
    /**
     * 代替select *
     */
    private String showAllTableNames;

    /**
     * 缓存类型
     */
    private DataCacheType cacheType;
    /**
     * 缓存时间
     */
    private Long cacheTime;
    /**
     * 缓存时间类型
     */
    private TimeUnit cacheTimeType;

    /**
     * 操作对象的Class信息
     */
    private Class<T> objClass;

    /**
     * 操作主键字段名
     */
    private String primaryKeyField;
    /**
     * 操作主键表列名
     */
    private String primaryKeyTableField;
    /**
     * 操作主键类型
     */
    private Class primaryKeyType;


    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public List<String> getFieldNames() {
        return fieldNames;
    }

    public void setFieldNames(List<String> fieldNames) {
        this.fieldNames = fieldNames;
    }

    public Map<String, String> getFieldTableNames() {
        return fieldTableNames;
    }

    public void setFieldTableNames(Map<String, String> fieldTableNames) {
        this.fieldTableNames = fieldTableNames;
    }


    public Long getCacheTime() {
        return cacheTime;
    }

    public void setCacheTime(Long cacheTime) {
        this.cacheTime = cacheTime;
    }

    public TimeUnit getCacheTimeType() {
        return cacheTimeType;
    }

    public void setCacheTimeType(TimeUnit cacheTimeType) {
        this.cacheTimeType = cacheTimeType;
    }

    public DataCacheType getCacheType() {
        return cacheType;
    }

    public void setCacheType(DataCacheType cacheType) {
        this.cacheType = cacheType;
    }

    public Map<String, Class> getFieldTypes() {
        return fieldTypes;
    }

    public void setFieldTypes(Map<String, Class> fieldTypes) {
        this.fieldTypes = fieldTypes;
    }

    public Class<T> getObjClass() {
        return objClass;
    }

    public void setObjClass(Class<T> objClass) {
        this.objClass = objClass;
    }

    public String getPrimaryKeyField() {
        return primaryKeyField;
    }

    public void setPrimaryKeyField(String primaryKeyField) {
        this.primaryKeyField = primaryKeyField;
    }

    public String getPrimaryKeyTableField() {
        return primaryKeyTableField;
    }

    public void setPrimaryKeyTableField(String primaryKeyTableField) {
        this.primaryKeyTableField = primaryKeyTableField;
    }

    public String getShowAllTableNames() {
        return showAllTableNames;
    }

    public void setShowAllTableNames(String showAllTableNames) {
        this.showAllTableNames = showAllTableNames;
    }

    public Map<String, String> getShowTableNames() {
        return showTableNames;
    }

    public void setShowTableNames(Map<String, String> showTableNames) {
        this.showTableNames = showTableNames;
    }

    public Map<String, String> getTableFieldNames() {
        return tableFieldNames;
    }

    public void setTableFieldNames(Map<String, String> tableFieldNames) {
        this.tableFieldNames = tableFieldNames;
    }

    public Class getPrimaryKeyType() {
        return primaryKeyType;
    }

    public void setPrimaryKeyType(Class primaryKeyType) {
        this.primaryKeyType = primaryKeyType;
    }
}

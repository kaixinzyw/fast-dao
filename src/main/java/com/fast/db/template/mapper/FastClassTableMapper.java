package com.fast.db.template.mapper;

import com.fast.db.template.cache.FastCacheType;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 存储表和对象关系映射
 * @author 张亚伟 398850094@qq.com
 */
public class FastClassTableMapper {

    private String className;

    private String tableName;

    private List<String> fieldNames;

    private Map<String, Class> fieldTypes;

    private Map<String, String> fieldTableNames;
    private String showAllTableNames;


    private FastCacheType cacheType;
    private Long cacheTime;
    private TimeUnit cacheTimeType;

    private Class objClass;

    private String primaryKeyField;
    private String primaryKeyTableField;


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

    public FastCacheType getCacheType() {
        return cacheType;
    }

    public void setCacheType(FastCacheType cacheType) {
        this.cacheType = cacheType;
    }

    public Map<String, Class> getFieldTypes() {
        return fieldTypes;
    }

    public void setFieldTypes(Map<String, Class> fieldTypes) {
        this.fieldTypes = fieldTypes;
    }

    public Class getObjClass() {
        return objClass;
    }

    public void setObjClass(Class objClass) {
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
}

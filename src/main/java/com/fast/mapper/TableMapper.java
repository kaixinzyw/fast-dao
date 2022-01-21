package com.fast.mapper;

import com.fast.cache.DataCacheType;
import com.fast.config.PrimaryKeyType;
import com.fast.dao.many.FastJoinQueryInfo;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 存储表和对象关系映射
 *
 * @author 张亚伟 https://github.com/kaixinzyw
 */
public class TableMapper {

    /**
     * 类名
     */
    private String className;

    /**
     * 表名称
     */
    private String tableName;
    /**
     * 表别名
     */
    private String tableAlias;

    /**
     * 字段集合
     */
    private List<String> fieldNames;

    /**
     * 字段类型集合
     */
    private LinkedHashMap<String, Class> fieldTypes;

    /**
     * <字段名,表列名>
     */
    private LinkedHashMap<String, String> fieldTableNames;
    /**
     * <表列名,字段名>
     */
    private LinkedHashMap<String, String> tableFieldNames;
    /**
     * select查询时候现实的列名
     */
    private LinkedHashMap<String, String> showTableNames;
    /**
     * 代替select *
     */
    private String showAllTableNames;
    private String showPrefixAllTableNames;

    /**
     * 缓存类型
     */
    private DataCacheType cacheType;
    private Boolean isOpenCache = Boolean.FALSE;
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
    private Class objClass;

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
    private Class primaryKeyClass;



    /**
     * 主键类型 32位UUID和自增
     */
    private PrimaryKeyType primaryKeyType;

    private Boolean logicDelete = Boolean.FALSE;
    private Boolean autoSetCreateTime = Boolean.FALSE;
    private Boolean autoSetUpdateTime = Boolean.FALSE;

    /**
     * 多对象信息列表
     */
    private final Map<String, FastJoinQueryInfo> fastJoinQueryInfoMap = new HashMap<>();


    private final Map<String, Map<String, String>> tableAliasFieldMap = new HashMap<>();
    private final Map<String, String> columnAliasMap = new HashMap<>();


    public Map<String, String> getColumnAliasMap() {
        return columnAliasMap;
    }

    public void addColumnAliasMap(String columnName, String columnAliasName) {
        this.columnAliasMap.put(columnName, columnAliasName);
    }

    public void addFastJoinQueryInfoMap(String tableAlias, FastJoinQueryInfo fastJoinQueryInfo) {
        FastJoinQueryInfo fastJoinQueryInfos = fastJoinQueryInfoMap.get(tableAlias);
        if (fastJoinQueryInfos == null) {
            fastJoinQueryInfoMap.put(tableAlias, fastJoinQueryInfo);
        }
    }

    public void putFastJoinQueryInfoMap(Map<String, FastJoinQueryInfo> fastJoinQueryInfoMap) {
        this.fastJoinQueryInfoMap.putAll(fastJoinQueryInfoMap);
    }

    public Map<String, FastJoinQueryInfo> getFastJoinQueryInfoMap() {
        return fastJoinQueryInfoMap;
    }

    public Map<String, Map<String, String>> getTableAliasFieldMap() {
        return tableAliasFieldMap;
    }

    public void addTableAliasFieldMap(String tableAliasName, String columnName, String fileName) {
        Map<String, String> tableAliasMap = tableAliasFieldMap.get(tableAliasName);
        if (tableAliasMap == null) {
            tableAliasMap = new HashMap<>();
            tableAliasFieldMap.put(tableAliasName, tableAliasMap);
        }
        tableAliasMap.put(columnName, fileName);
    }

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

    public LinkedHashMap<String, String> getFieldTableNames() {
        return fieldTableNames;
    }

    public void setFieldTableNames(LinkedHashMap<String, String> fieldTableNames) {
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

    public LinkedHashMap<String, Class> getFieldTypes() {
        return fieldTypes;
    }

    public void setFieldTypes(LinkedHashMap<String, Class> fieldTypes) {
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

    public LinkedHashMap<String, String> getShowTableNames() {
        return showTableNames;
    }

    public void setShowTableNames(LinkedHashMap<String, String> showTableNames) {
        this.showTableNames = showTableNames;
    }

    public LinkedHashMap<String, String> getTableFieldNames() {
        return tableFieldNames;
    }

    public void setTableFieldNames(LinkedHashMap<String, String> tableFieldNames) {
        this.tableFieldNames = tableFieldNames;
    }

    public Class getPrimaryKeyClass() {
        return primaryKeyClass;
    }

    public void setPrimaryKeyClass(Class primaryKeyClass) {
        this.primaryKeyClass = primaryKeyClass;
    }

    public PrimaryKeyType getPrimaryKeyType() {
        return primaryKeyType;
    }

    public void setPrimaryKeyType(PrimaryKeyType primaryKeyType) {
        this.primaryKeyType = primaryKeyType;
    }

    public Boolean getLogicDelete() {
        return logicDelete;
    }

    public void setLogicDelete(Boolean logicDelete) {
        this.logicDelete = logicDelete;
    }

    public Boolean getAutoSetCreateTime() {
        return autoSetCreateTime;
    }

    public void setAutoSetCreateTime(Boolean autoSetCreateTime) {
        this.autoSetCreateTime = autoSetCreateTime;
    }

    public Boolean getAutoSetUpdateTime() {
        return autoSetUpdateTime;
    }

    public void setAutoSetUpdateTime(Boolean autoSetUpdateTime) {
        this.autoSetUpdateTime = autoSetUpdateTime;
    }


    public String getShowPrefixAllTableNames() {
        return showPrefixAllTableNames;
    }

    public void setShowPrefixAllTableNames(String showPrefixAllTableNames) {
        this.showPrefixAllTableNames = showPrefixAllTableNames;
    }

    public String getTableAlias() {
        return tableAlias;
    }

    public void setTableAlias(String tableAlias) {
        this.tableAlias = tableAlias;
    }

    public Boolean getOpenCache() {
        return isOpenCache;
    }

    public void setOpenCache(Boolean openCache) {
        isOpenCache = openCache;
    }
}

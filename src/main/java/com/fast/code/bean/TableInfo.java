package com.fast.code.bean;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 表对象
 *
 * @author 张亚伟 https://github.com/kaixinzyw
 */
public class TableInfo {

    /**
     * 表名
     */
    private String tableName;

    /**
     * 前缀名
     */
    private String prefixName;

    /**
     * bean
     */
    private String beanName;

    /**
     * 表名
     */
    private String tableDesc;

    /**
     * 主键映射
     */
    private Map<String, String> primaryKey;
    /**
     * 字段类型映射
     */
    private List<ColumnInfo> columns;

    /**
     * 属性,属性类型
     */
    private Map<String, String> properties;

    /**
     * 属性,属性类型
     */
    private List<BeanInfo> properties2;
    /**
     * 属性,属性类型
     */
    private Map<String, String> propertiesAnColumns;

    /**
     * 属性,属性类型
     */
    private Map<String, String> insertPropertiesAnColumns;

    /**
     * bean类导入的包
     */
    private Set<String> packages;

    /**
     * 以下为各个模板类型的文件路径和包信息
     */
    private String pojoPackPath;
    private String pojoFilePath;
    private String pojoName;
    private String pojoClassPackPath;

    private String fastPojoPackPath;
    private String fastPojoFilePath;
    private String fastPojoName;
    private String fastPojoClassPackPath;

    private String pojoFastDaoPackPath;
    private String pojoFastDaoFilePath;
    private String pojoFastDaoName;
    private String pojoFastDaoClassPackPath;

    private String pojoFieldsPackPath;
    private String pojoFieldsFilePath;
    private String pojoFieldsName;
    private String pojoFieldsClassPackPath;

    private String dtoPackPath;
    private String dtoFilePath;
    private String dtoName;
    private String dtoClassPackPath;

    private String daoPackPath;
    private String daoFilePath;
    private String daoName;
    private String daoClassPackPath;

    private String iservicePackPath;
    private String iserviceFilePath;
    private String iserviceName;
    private String iserviceClassPackPath;

    private String servicePackPath;
    private String serviceFilePath;
    private String serviceName;
    private String serviceClassPackPath;

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getBeanName() {
        return beanName;
    }

    public void setBeanName(String beanName) {
        this.beanName = beanName;
    }

    public String getTableDesc() {
        return tableDesc;
    }

    public void setTableDesc(String tableDesc) {
        this.tableDesc = tableDesc;
    }

    public Map<String, String> getPrimaryKey() {
        return primaryKey;
    }

    public void setPrimaryKey(Map<String, String> primaryKey) {
        this.primaryKey = primaryKey;
    }

    public List<ColumnInfo> getColumns() {
        return columns;
    }

    public void setColumns(List<ColumnInfo> columns) {
        this.columns = columns;
    }

    public Map<String, String> getProperties() {
        return properties;
    }

    public void setProperties(Map<String, String> properties) {
        this.properties = properties;
    }

    public List<BeanInfo> getProperties2() {
        return properties2;
    }

    public void setProperties2(List<BeanInfo> properties2) {
        this.properties2 = properties2;
    }

    public Map<String, String> getPropertiesAnColumns() {
        return propertiesAnColumns;
    }

    public void setPropertiesAnColumns(Map<String, String> propertiesAnColumns) {
        this.propertiesAnColumns = propertiesAnColumns;
    }

    public Map<String, String> getInsertPropertiesAnColumns() {
        return insertPropertiesAnColumns;
    }

    public void setInsertPropertiesAnColumns(Map<String, String> insertPropertiesAnColumns) {
        this.insertPropertiesAnColumns = insertPropertiesAnColumns;
    }

    public Set<String> getPackages() {
        return packages;
    }

    public void setPackages(Set<String> packages) {
        this.packages = packages;
    }

    public String getPrefixName() {
        return prefixName;
    }

    public void setPrefixName(String prefixName) {
        this.prefixName = prefixName;
    }

    public String getDaoPackPath() {
        return daoPackPath;
    }

    public void setDaoPackPath(String daoPackPath) {
        this.daoPackPath = daoPackPath;
    }

    public String getDtoPackPath() {
        return dtoPackPath;
    }

    public void setDtoPackPath(String dtoPackPath) {
        this.dtoPackPath = dtoPackPath;
    }

    public String getIservicePackPath() {
        return iservicePackPath;
    }

    public void setIservicePackPath(String iservicePackPath) {
        this.iservicePackPath = iservicePackPath;
    }

    public String getServicePackPath() {
        return servicePackPath;
    }

    public void setServicePackPath(String servicePackPath) {
        this.servicePackPath = servicePackPath;
    }

    public String getPojoPackPath() {
        return pojoPackPath;
    }

    public void setPojoPackPath(String pojoPackPath) {
        this.pojoPackPath = pojoPackPath;
    }

    public String getPojoFastDaoPackPath() {
        return pojoFastDaoPackPath;
    }

    public void setPojoFastDaoPackPath(String pojoFastDaoPackPath) {
        this.pojoFastDaoPackPath = pojoFastDaoPackPath;
    }

    public String getPojoFieldsPackPath() {
        return pojoFieldsPackPath;
    }

    public void setPojoFieldsPackPath(String pojoFieldsPackPath) {
        this.pojoFieldsPackPath = pojoFieldsPackPath;
    }

    public String getPojoClassPackPath() {
        return pojoClassPackPath;
    }

    public void setPojoClassPackPath(String pojoClassPackPath) {
        this.pojoClassPackPath = pojoClassPackPath;
    }

    public String getPojoFastDaoClassPackPath() {
        return pojoFastDaoClassPackPath;
    }

    public void setPojoFastDaoClassPackPath(String pojoFastDaoClassPackPath) {
        this.pojoFastDaoClassPackPath = pojoFastDaoClassPackPath;
    }

    public String getPojoFieldsClassPackPath() {
        return pojoFieldsClassPackPath;
    }

    public void setPojoFieldsClassPackPath(String pojoFieldsClassPackPath) {
        this.pojoFieldsClassPackPath = pojoFieldsClassPackPath;
    }

    public String getDtoClassPackPath() {
        return dtoClassPackPath;
    }

    public void setDtoClassPackPath(String dtoClassPackPath) {
        this.dtoClassPackPath = dtoClassPackPath;
    }

    public String getDaoClassPackPath() {
        return daoClassPackPath;
    }

    public void setDaoClassPackPath(String daoClassPackPath) {
        this.daoClassPackPath = daoClassPackPath;
    }

    public String getIserviceClassPackPath() {
        return iserviceClassPackPath;
    }

    public void setIserviceClassPackPath(String iserviceClassPackPath) {
        this.iserviceClassPackPath = iserviceClassPackPath;
    }

    public String getServiceClassPackPath() {
        return serviceClassPackPath;
    }

    public void setServiceClassPackPath(String serviceClassPackPath) {
        this.serviceClassPackPath = serviceClassPackPath;
    }

    public String getPojoName() {
        return pojoName;
    }

    public void setPojoName(String pojoName) {
        this.pojoName = pojoName;
    }

    public String getFastPojoPackPath() {
        return fastPojoPackPath;
    }

    public void setFastPojoPackPath(String fastPojoPackPath) {
        this.fastPojoPackPath = fastPojoPackPath;
    }

    public String getFastPojoFilePath() {
        return fastPojoFilePath;
    }

    public void setFastPojoFilePath(String fastPojoFilePath) {
        this.fastPojoFilePath = fastPojoFilePath;
    }

    public String getFastPojoName() {
        return fastPojoName;
    }

    public void setFastPojoName(String fastPojoName) {
        this.fastPojoName = fastPojoName;
    }

    public String getFastPojoClassPackPath() {
        return fastPojoClassPackPath;
    }

    public void setFastPojoClassPackPath(String fastPojoClassPackPath) {
        this.fastPojoClassPackPath = fastPojoClassPackPath;
    }

    public String getPojoFastDaoName() {
        return pojoFastDaoName;
    }

    public void setPojoFastDaoName(String pojoFastDaoName) {
        this.pojoFastDaoName = pojoFastDaoName;
    }

    public String getPojoFieldsName() {
        return pojoFieldsName;
    }

    public void setPojoFieldsName(String pojoFieldsName) {
        this.pojoFieldsName = pojoFieldsName;
    }

    public String getDtoName() {
        return dtoName;
    }

    public void setDtoName(String dtoName) {
        this.dtoName = dtoName;
    }

    public String getDaoName() {
        return daoName;
    }

    public void setDaoName(String daoName) {
        this.daoName = daoName;
    }

    public String getIserviceName() {
        return iserviceName;
    }

    public void setIserviceName(String iserviceName) {
        this.iserviceName = iserviceName;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getPojoFilePath() {
        return pojoFilePath;
    }

    public void setPojoFilePath(String pojoFilePath) {
        this.pojoFilePath = pojoFilePath;
    }

    public String getPojoFastDaoFilePath() {
        return pojoFastDaoFilePath;
    }

    public void setPojoFastDaoFilePath(String pojoFastDaoFilePath) {
        this.pojoFastDaoFilePath = pojoFastDaoFilePath;
    }

    public String getPojoFieldsFilePath() {
        return pojoFieldsFilePath;
    }

    public void setPojoFieldsFilePath(String pojoFieldsFilePath) {
        this.pojoFieldsFilePath = pojoFieldsFilePath;
    }

    public String getDtoFilePath() {
        return dtoFilePath;
    }

    public void setDtoFilePath(String dtoFilePath) {
        this.dtoFilePath = dtoFilePath;
    }

    public String getDaoFilePath() {
        return daoFilePath;
    }

    public void setDaoFilePath(String daoFilePath) {
        this.daoFilePath = daoFilePath;
    }

    public String getIserviceFilePath() {
        return iserviceFilePath;
    }

    public void setIserviceFilePath(String iserviceFilePath) {
        this.iserviceFilePath = iserviceFilePath;
    }

    public String getServiceFilePath() {
        return serviceFilePath;
    }

    public void setServiceFilePath(String serviceFilePath) {
        this.serviceFilePath = serviceFilePath;
    }
}

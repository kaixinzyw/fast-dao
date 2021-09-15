package com.fast.dao.many;

public class FastJoinQueryInfo {

    /**
     * 表别名
     */
    private String thisTableAlias;
    /**
     * 映射列名
     */
    private String thisColumnName;

    /**
     * 连接表别名
     */
    private String joinTableAlias;

    /**
     * 加入行名称
     */
    private String joinColumnName;

    /**
     * join表主键
     */
    private String joinPrimaryKey;

    /**
     * 字段名
     */
    private String fieldName;

    /**
     * 是否为集合
     */
    private Boolean isCollectionType;



    public String getThisTableAlias() {
        return thisTableAlias;
    }

    public void setThisTableAlias(String thisTableAlias) {
        this.thisTableAlias = thisTableAlias;
    }

    public String getThisColumnName() {
        return thisColumnName;
    }

    public void setThisColumnName(String thisColumnName) {
        this.thisColumnName = thisColumnName;
    }

    public String getJoinTableAlias() {
        return joinTableAlias;
    }

    public void setJoinTableAlias(String joinTableAlias) {
        this.joinTableAlias = joinTableAlias;
    }

    public String getJoinColumnName() {
        return joinColumnName;
    }

    public void setJoinColumnName(String joinColumnName) {
        this.joinColumnName = joinColumnName;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public Boolean getCollectionType() {
        return isCollectionType;
    }

    public void setCollectionType(Boolean collectionType) {
        isCollectionType = collectionType;
    }

    public String getJoinPrimaryKey() {
        return joinPrimaryKey;
    }

    public void setJoinPrimaryKey(String joinPrimaryKey) {
        this.joinPrimaryKey = joinPrimaryKey;
    }

}

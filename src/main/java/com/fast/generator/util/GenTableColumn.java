package com.fast.generator.util;


/**
 * 表字段信息
 * @author zyw
 */
public class GenTableColumn {

    /**
     * 列名称
     */
    private String columnName;

    /**
     * 列描述
     */
    private String columnComment;

    /**
     * 列类型
     */
    private String columnType;

    /**
     * JAVA类型
     */
    private String javaType;

    /**
     * JAVA字段名
     */
    private String javaField;

    /**
     * 是否主键（1是）
     */
    private Boolean isPk;

    /**
     * 是否自增（1是）
     */
    private Boolean isIncrement;

    /**
     * 是否必填（1是）
     */
    private Boolean isRequired;

    /**
     * 排序
     */
    private Integer sort;

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public String getColumnComment() {
        return columnComment;
    }

    public void setColumnComment(String columnComment) {
        this.columnComment = columnComment;
    }

    public String getColumnType() {
        return columnType;
    }

    public void setColumnType(String columnType) {
        this.columnType = columnType;
    }

    public String getJavaType() {
        return javaType;
    }

    public void setJavaType(String javaType) {
        this.javaType = javaType;
    }

    public String getJavaField() {
        return javaField;
    }

    public void setJavaField(String javaField) {
        this.javaField = javaField;
    }

    public Boolean getPk() {
        return isPk;
    }

    public void setPk(Boolean pk) {
        isPk = pk;
    }

    public Boolean getIncrement() {
        return isIncrement;
    }

    public void setIncrement(Boolean increment) {
        isIncrement = increment;
    }

    public Boolean getRequired() {
        return isRequired;
    }

    public void setRequired(Boolean required) {
        isRequired = required;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }
}

package com.fast.db.template.code.bean;

/**
 * 表的所有字段
 * @author 张亚伟 https://github.com/kaixinzyw/fast-db-template
 */
public class ColumnInfo {
    /**
     * 字段名
     */
    private String columnName;
    /**
     * 字段类型
     */
    private String columnType;
    /**
     * 字段注释
     */
    private String columnRemarks;

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public String getColumnType() {
        return columnType;
    }

    public void setColumnType(String columnType) {
        this.columnType = columnType;
    }

    public String getColumnRemarks() {
        return columnRemarks;
    }

    public void setColumnRemarks(String columnRemarks) {
        this.columnRemarks = columnRemarks;
    }
}

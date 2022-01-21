package com.fast.generator.util;

/**
 * 表结构常量
 */
public class GenConstants
{


    /** 数据库字符串类型 */
    public static final String[] COLUMNTYPE_STR = { "char", "varchar", "nvarchar", "varchar2", "tinytext", "text",
            "mediumtext", "longtext" };

    /** 数据库时间类型 */
    public static final String[] COLUMNTYPE_TIME = { "datetime", "time", "date", "timestamp" };

    /** 数据库数字类型 */
    public static final String[] COLUMNTYPE_NUMBER = { "tinyint", "smallint", "mediumint", "int", "number", "integer",
            "bigint", "float", "float", "double", "decimal" };



    /** 字符串类型 */
    public static final String TYPE_STRING = "String";

    /** 整型 */
    public static final String TYPE_INTEGER = "Integer";

    /** 布尔 */
    public static final String TYPE_BOOLEAN = "Boolean";

    /** 长整型 */
    public static final String TYPE_LONG = "Long";

    /** 浮点型 */
    public static final String TYPE_DOUBLE = "Double";

    /** 高精度计算类型 */
    public static final String TYPE_BIGDECIMAL = "BigDecimal";

    /** 时间类型 */
    public static final String TYPE_DATE = "Date";

    /** 模糊查询 */
    public static final String QUERY_LIKE = "LIKE";

}

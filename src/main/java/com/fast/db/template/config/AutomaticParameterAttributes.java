package com.fast.db.template.config;

import com.fast.db.template.implement.FastDBActuator;

import java.util.concurrent.TimeUnit;

public class AutomaticParameterAttributes {


    /**
     * 是否自动设置创建时间,目前只支持Date类型
     */
    public static Boolean isAutoSetCreateTime = Boolean.FALSE;

    public static String createTimeField;
    public static String createTimeTableField;


    /**
     * 是否自动设置更新时间,目前只支持Date类型
     */
    public static Boolean isAutoSetUpdateTime = Boolean.FALSE;

    public static String updateTimeField;
    public static String updateTimeTableField;


    /**
     * 是否自动设置主键,目前只支持32位UUID,不支持联合主键
     */
    public static Boolean isAutoSetPrimaryKey = Boolean.FALSE;

    public static String primaryKeyField;
    public static String primaryKeyTableField;

    //是否启用逻辑删除,目前只支持Boolean
    public static Boolean isOpenLogicDelete = Boolean.FALSE;

    public static String deleteField;
    public static String deleteTableField;
    public static Boolean defaultDeleteValue;

    public static Boolean isOpenCache = Boolean.FALSE;

    /**
     * 是否启用驼峰转换
     */
    public static Boolean isToCamelCase = Boolean.FALSE;

    public static Long defaultCacheTime;
    public static TimeUnit defaultCacheTimeType;

    private static Class<? extends FastDBActuator> dbActuator;

    public static Class<? extends FastDBActuator> getDBActuator() {
        return dbActuator;
    }

    public static void setDBActuator(Class<? extends FastDBActuator> dbActuator) {
        AutomaticParameterAttributes.dbActuator = dbActuator;
    }
}


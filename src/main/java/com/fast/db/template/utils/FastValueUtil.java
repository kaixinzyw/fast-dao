package com.fast.db.template.utils;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.UUID;
import cn.hutool.core.util.StrUtil;
import com.fast.db.template.config.FastParams;
import com.fast.db.template.config.PrimaryKeyType;

import java.util.Date;

public class FastValueUtil {

    public static void setCreateTime(Object o) {
        if (FastParams.isAutoSetCreateTime && o != null) {
            BeanUtil.setFieldValue(o, FastParams.createTimeFieldName, new Date());
        }
    }

    public static void setUpdateTime(Object o) {
        if (FastParams.isAutoSetUpdateTime && o != null) {
            BeanUtil.setFieldValue(o, FastParams.updateTimeFieldName, new Date());
        }
    }


    public static void setPrimaryKey(Object o, Object primaryKey) {
        if (FastParams.isAutoSetPrimaryKey && o != null) {
            BeanUtil.setFieldValue(o, FastParams.primaryKeyFieldName, primaryKey);
        }
    }

    public static void setPrimaryKey(Object o) {
        if (FastParams.isAutoSetPrimaryKey) {
            if (FastParams.primaryKeyType.equals(PrimaryKeyType.UUID) && o != null) {
                Object primaryKey = BeanUtil.getFieldValue(o, FastParams.primaryKeyFieldName);
                if (primaryKey == null) {
                    BeanUtil.setFieldValue(o, FastParams.primaryKeyFieldName, UUID.randomUUID().toString(true));
                }
            }
        }
    }

    public static Object getPrimaryKeyValue(Object o) {
        if (o == null) {
            return null;
        }
        Object primaryKey = BeanUtil.getFieldValue(o, FastParams.primaryKeyFieldName);
        return primaryKey;
    }


    public static void setDeleted(Object o) {
        if (FastParams.isOpenLogicDelete && o != null) {
            BeanUtil.setFieldValue(o, FastParams.deleteFieldName, FastParams.defaultDeleteValue);
        }
    }

    public static void setNoDelete(Object o) {
        if (FastParams.isOpenLogicDelete && o != null) {
            BeanUtil.setFieldValue(o, FastParams.deleteFieldName, !FastParams.defaultDeleteValue);
        }
    }

    /**
     * 驼峰转换
     * 例如：hello_world=》helloWorld
     *
     * @param val 需要转换的值
     * @return 转换后的值
     */
    public static String toCamelCase(String val) {
        if (val == null) {
            return val;
        }
        if (FastParams.isToCamelCase) {
            val = StrUtil.toCamelCase(val);
        }
        return val;
    }



}

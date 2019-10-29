package com.fast.db.template.utils;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.UUID;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.fast.db.template.config.FastParams;
import com.fast.db.template.config.PrimaryKeyType;
import com.fast.db.template.mapper.TableMapper;

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


    public static void setPrimaryKey(Object o, Object val, TableMapper tableMapper) {
        BeanUtil.setFieldValue(o, tableMapper.getPrimaryKeyField(), val);
    }

    public static void setPrimaryKey(Object o, TableMapper tableMapper) {
        if (tableMapper.getPrimaryKeyType().equals(PrimaryKeyType.UUID) && o != null) {
            Object primaryKey = BeanUtil.getFieldValue(o, tableMapper.getPrimaryKeyField());
            if (primaryKey == null) {
                BeanUtil.setFieldValue(o, tableMapper.getPrimaryKeyField(), UUID.randomUUID().toString(true));
            }
        }
    }

    public static Object getPrimaryKeyValue(Object o, TableMapper tableMapper) {
        if (o == null) {
            return null;
        }
        Object primaryKeyVal = BeanUtil.getFieldValue(o, tableMapper.getPrimaryKeyField());
        if (primaryKeyVal == null) {
            throw new RuntimeException("主键值不存在: " + JSONObject.toJSONString(o));
        }
        return primaryKeyVal;
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

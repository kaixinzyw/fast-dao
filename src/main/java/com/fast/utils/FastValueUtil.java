package com.fast.utils;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.UUID;
import cn.hutool.core.util.StrUtil;
import com.fast.config.FastDaoAttributes;
import com.fast.config.PrimaryKeyType;
import com.fast.mapper.TableMapper;

import java.util.Date;

public class FastValueUtil {

    public static void setCreateTime(Object o, TableMapper tableMapper) {
        if (FastDaoAttributes.isAutoSetCreateTime && tableMapper.getAutoSetCreateTime()) {
            BeanUtil.setFieldValue(o, FastDaoAttributes.createTimeFieldName, new Date());
        }
    }

    public static void setUpdateTime(Object o, TableMapper tableMapper) {
        if (FastDaoAttributes.isAutoSetUpdateTime && tableMapper.getAutoSetUpdateTime()) {
            BeanUtil.setFieldValue(o, FastDaoAttributes.updateTimeFieldName, new Date());
        }
    }

    public static Object getPrimaryKeyVal(Object o, TableMapper tableMapper) {
        if (tableMapper.getPrimaryKeyField() == null || o == null) {
            return null;
        }
        return BeanUtil.getFieldValue(o, tableMapper.getPrimaryKeyField());
    }

    public static void setPrimaryKey(Object o, Object val, TableMapper tableMapper) {
        BeanUtil.setFieldValue(o, tableMapper.getPrimaryKeyField(), val);
    }

    public static void setPrimaryKey(Object o, TableMapper tableMapper) {
        if (tableMapper.getPrimaryKeyField() != null && tableMapper.getPrimaryKeyType().equals(PrimaryKeyType.UUID)) {
            Object primaryKey = BeanUtil.getFieldValue(o, tableMapper.getPrimaryKeyField());
            if (primaryKey == null) {
                BeanUtil.setFieldValue(o, tableMapper.getPrimaryKeyField(), UUID.randomUUID().toString(true));
            }
        }
    }

    public static void setDeleted(Object o, TableMapper tableMapper) {
        if (FastDaoAttributes.isOpenLogicDelete && tableMapper.getLogicDelete()) {
            BeanUtil.setFieldValue(o, FastDaoAttributes.deleteFieldName, FastDaoAttributes.defaultDeleteValue);
        }
    }

    public static void setNoDelete(Object o, TableMapper tableMapper) {
        if (FastDaoAttributes.isOpenLogicDelete && tableMapper.getLogicDelete()) {
            BeanUtil.setFieldValue(o, FastDaoAttributes.deleteFieldName, !FastDaoAttributes.defaultDeleteValue);
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
        if (FastDaoAttributes.isToCamelCase) {
            val = StrUtil.toCamelCase(val);
        }
        return val;
    }


}

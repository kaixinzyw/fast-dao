package com.fast.db.template.template;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.UUID;
import com.fast.db.template.config.AutomaticParameterAttributes;

import java.util.Date;

public class FastSQLUtil {

    public static void setCreateTime(Object pojo) {
        if (AutomaticParameterAttributes.isAutoSetCreateTime) {
            BeanUtil.setFieldValue(pojo, AutomaticParameterAttributes.createTimeField, new Date());
        }
    }

    public static void setUpdateTime(Object pojo) {
        if (AutomaticParameterAttributes.isAutoSetUpdateTime) {
            BeanUtil.setFieldValue(pojo, AutomaticParameterAttributes.updateTimeField, new Date());
        }
    }


    public static void setPrimaryKey(Object pojo, Object primaryKey) {
        if (AutomaticParameterAttributes.isAutoSetPrimaryKey) {
            BeanUtil.setFieldValue(pojo, AutomaticParameterAttributes.primaryKeyField, primaryKey);
        }
    }

    public static void setPrimaryKey(Object pojo) {
        if (AutomaticParameterAttributes.isAutoSetPrimaryKey) {
            Object primaryKey = BeanUtil.getFieldValue(pojo, AutomaticParameterAttributes.primaryKeyField);
            if (primaryKey == null) {
                BeanUtil.setFieldValue(pojo, AutomaticParameterAttributes.primaryKeyField, UUID.randomUUID().toString(true));
            }
        }

    }

    public static Object getPrimaryKey(Object pojo) {
        Object primaryKey = BeanUtil.getFieldValue(pojo, AutomaticParameterAttributes.primaryKeyField);
        return primaryKey;
    }


    public static void setDeleted(Object o) {
        if (AutomaticParameterAttributes.isOpenLogicDelete) {
            BeanUtil.setFieldValue(o, AutomaticParameterAttributes.deleteField, AutomaticParameterAttributes.defaultDeleteValue);
        }

    }

    public static void setNoDelete(Object o) {
        if (AutomaticParameterAttributes.isOpenLogicDelete) {
            BeanUtil.setFieldValue(o, AutomaticParameterAttributes.deleteField, !AutomaticParameterAttributes.defaultDeleteValue);
        }
    }


}

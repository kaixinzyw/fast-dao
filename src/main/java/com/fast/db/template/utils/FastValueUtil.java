package com.fast.db.template.utils;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.UUID;
import com.fast.db.template.config.AutomaticParameterAttributes;
import com.fast.db.template.config.PrimaryKeyType;

import java.util.Date;

public class FastValueUtil {

    public static void setCreateTime(Object o) {
        if (AutomaticParameterAttributes.isAutoSetCreateTime && o != null) {
            BeanUtil.setFieldValue(o, AutomaticParameterAttributes.createTimeField, new Date());
        }
    }

    public static void setUpdateTime(Object o) {
        if (AutomaticParameterAttributes.isAutoSetUpdateTime && o != null) {
            BeanUtil.setFieldValue(o, AutomaticParameterAttributes.updateTimeField, new Date());
        }
    }


    public static void setPrimaryKey(Object o, Object primaryKey) {
        if (AutomaticParameterAttributes.isAutoSetPrimaryKey && o != null) {
            BeanUtil.setFieldValue(o, AutomaticParameterAttributes.primaryKeyField, primaryKey);
        }
    }

    public static void setPrimaryKey(Object o) {
        if (AutomaticParameterAttributes.isAutoSetPrimaryKey) {
            if (AutomaticParameterAttributes.primaryKeyType.equals(PrimaryKeyType.UUID) && o != null) {
                Object primaryKey = BeanUtil.getFieldValue(o, AutomaticParameterAttributes.primaryKeyField);
                if (primaryKey == null) {
                    BeanUtil.setFieldValue(o, AutomaticParameterAttributes.primaryKeyField, UUID.randomUUID().toString(true));
                }
            }
        }
    }

    public static Object getPrimaryKeyValue(Object o) {
        if (o == null) {
            return null;
        }
        Object primaryKey = BeanUtil.getFieldValue(o, AutomaticParameterAttributes.primaryKeyField);
        return primaryKey;
    }


    public static void setDeleted(Object o) {
        if (AutomaticParameterAttributes.isOpenLogicDelete && o != null) {
            BeanUtil.setFieldValue(o, AutomaticParameterAttributes.deleteField, AutomaticParameterAttributes.defaultDeleteValue);
        }
    }

    public static void setNoDelete(Object o) {
        if (AutomaticParameterAttributes.isOpenLogicDelete && o != null) {
            BeanUtil.setFieldValue(o, AutomaticParameterAttributes.deleteField, !AutomaticParameterAttributes.defaultDeleteValue);
        }
    }


}

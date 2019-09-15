package com.fast.db.template.config;

import cn.hutool.core.util.StrUtil;
import com.fast.db.template.implement.FastDBActuator;
import com.fast.db.template.utils.JedisUtils;
import redis.clients.jedis.JedisPool;

import java.util.concurrent.TimeUnit;

/**
 * @author 张亚伟 398850094@qq.com
 */
public class AutomaticParameterSetting {

    public AutomaticParameterSetting(Class<? extends FastDBActuator> mapperImpl) {
        AutomaticParameterAttributes.setDBActuator(mapperImpl);
    }


    public void defaultSetting(){
        openToCamelCase();
        openAutoSetCreateTime("createTime");
        openAutoSetUpdateTime("updateTime");
        openAutoSetPrimaryKey("id");
        openLogicDelete("deleted" , Boolean.TRUE);
    }

    public void openAutoSetCreateTime(String createTimeField) {
        AutomaticParameterAttributes.isAutoSetCreateTime = Boolean.TRUE;
        AutomaticParameterAttributes.createTimeField = createTimeField;
        AutomaticParameterAttributes.createTimeTableField = getToUnderlineCase(createTimeField);
    }


    public void openAutoSetUpdateTime(String updateTimeField) {
        AutomaticParameterAttributes.isAutoSetUpdateTime = Boolean.TRUE;
        AutomaticParameterAttributes.updateTimeField = updateTimeField;
        AutomaticParameterAttributes.updateTimeTableField = getToUnderlineCase(updateTimeField);
    }


    public void openAutoSetPrimaryKey(String primaryKeyField) {
        AutomaticParameterAttributes.isAutoSetPrimaryKey = Boolean.TRUE;
        AutomaticParameterAttributes.primaryKeyField = primaryKeyField;
        AutomaticParameterAttributes.primaryKeyTableField = getToUnderlineCase(primaryKeyField);
    }


    public void openLogicDelete(String deleteField, Boolean defaultDeleteValue) {
        AutomaticParameterAttributes.isOpenLogicDelete = Boolean.TRUE;
        AutomaticParameterAttributes.deleteField = deleteField;
        AutomaticParameterAttributes.deleteTableField = getToUnderlineCase(deleteField);
        AutomaticParameterAttributes.defaultDeleteValue = defaultDeleteValue;
    }


    public void openCache(Long defaultCacheTime, TimeUnit defaultCacheTimeType, JedisPool openReisSync) {
        AutomaticParameterAttributes.isOpenCache = Boolean.TRUE;
        AutomaticParameterAttributes.defaultCacheTime = defaultCacheTime;

        AutomaticParameterAttributes.defaultCacheTimeType = defaultCacheTimeType;
        JedisUtils.jedisPool = openReisSync;
    }

    public void openToCamelCase() {
        AutomaticParameterAttributes.isToCamelCase = Boolean.TRUE;
        AutomaticParameterAttributes.primaryKeyTableField = getToUnderlineCase(AutomaticParameterAttributes.primaryKeyTableField);
        AutomaticParameterAttributes.createTimeTableField = getToUnderlineCase(AutomaticParameterAttributes.createTimeTableField);
        AutomaticParameterAttributes.updateTimeTableField = getToUnderlineCase(AutomaticParameterAttributes.updateTimeTableField);
        AutomaticParameterAttributes.deleteTableField = getToUnderlineCase(AutomaticParameterAttributes.deleteTableField);
    }

    private String getToUnderlineCase(String val) {
        if (val == null) {
            return val;
        }
        if (AutomaticParameterAttributes.isToCamelCase) {
            val = StrUtil.toUnderlineCase(val);
        }
        return val;
    }


}

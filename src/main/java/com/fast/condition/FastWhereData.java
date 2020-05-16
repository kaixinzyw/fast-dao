package com.fast.condition;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * 对象条件
 *
 * @author 张亚伟 https://github.com/kaixinzyw
 */
public class FastWhereData {

    private final static Map<String, Map<String, FastWhereData>> fastWhereDataMap = new HashMap<>();

    private String fieldName;
    private FastWhere.WhereCondition condition;
    private FastWhere.WhereWay way;
    private String sql;

    public static Map<String, FastWhereData> getObjWhere(Object obj) {
        Map<String, FastWhereData> fastWhereDataMap = FastWhereData.fastWhereDataMap.get(obj.getClass().getName());
        if (fastWhereDataMap != null) {
            return fastWhereDataMap;
        }
        synchronized (obj.getClass().getName().intern()) {
            fastWhereDataMap = FastWhereData.fastWhereDataMap.get(obj.getClass().getName());
            if (fastWhereDataMap != null) {
                return fastWhereDataMap;
            }
            Field[] fields = obj.getClass().getDeclaredFields();
            if (ArrayUtil.isEmpty(fields)) {
                FastWhereData.fastWhereDataMap.put(obj.getClass().getName(), null);
                return null;
            }
            fastWhereDataMap = new HashMap<>();
            for (Field field : fields) {
                FastWhereData whereData = new FastWhereData();
                boolean isWhereField = field.isAnnotationPresent(FastWhere.class);
                if (!isWhereField) {
                    whereData.setFieldName(field.getName());
                    whereData.setCondition(FastWhere.WhereCondition.Equal);
                    whereData.setWay(FastWhere.WhereWay.AND);
                    fastWhereDataMap.put(field.getName(), whereData);
                    continue;
                }
                FastWhere fastWhereAnnotation = field.getAnnotation(FastWhere.class);
                whereData.setCondition(fastWhereAnnotation.condition());
                whereData.setWay(fastWhereAnnotation.way());
                whereData.setSql(fastWhereAnnotation.sql());
                if (StrUtil.isNotBlank(fastWhereAnnotation.fieldName())) {
                    whereData.setFieldName(fastWhereAnnotation.fieldName());
                } else {
                    whereData.setFieldName(field.getName());
                }
                fastWhereDataMap.put(field.getName(), whereData);
            }
            FastWhereData.fastWhereDataMap.put(obj.getClass().getName(), fastWhereDataMap);
            return fastWhereDataMap;
        }
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public FastWhere.WhereCondition getCondition() {
        return condition;
    }

    public void setCondition(FastWhere.WhereCondition condition) {
        this.condition = condition;
    }

    public FastWhere.WhereWay getWay() {
        return way;
    }

    public void setWay(FastWhere.WhereWay way) {
        this.way = way;
    }

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }
}

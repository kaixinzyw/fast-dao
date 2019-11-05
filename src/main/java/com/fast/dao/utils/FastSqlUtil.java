package com.fast.dao.utils;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import com.alibaba.fastjson.JSONObject;
import com.fast.condition.ConditionPackages;
import com.fast.condition.FastCondition;
import com.fast.config.FastDaoAttributes;
import com.fast.dao.jdbc.JdbcImpl;
import com.fast.dao.mybatis.FastMyBatisImpl;
import com.fast.fast.FastDaoParam;
import com.fast.mapper.TableMapper;
import com.fast.utils.FastSQL;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

/**
 * SQL拼接工具类
 *
 * @author 张亚伟 https://github.com/kaixinzyw
 */
public class FastSqlUtil {

    /**
     * SQL日志打印
     *
     * @param param 参数
     * @param o     SQL执行结果
     */
    public static void printSql(FastDaoParam param, Object o) {
        if (!FastDaoAttributes.isSqlPrint) {
            return;
        }
        Map<String, Object> paramMap = param.getParamMap();
        String sql = param.getSql();
        if (CollUtil.isNotEmpty(paramMap)) {
            for (String key : paramMap.keySet()) {
                Object value = paramMap.get(key);
                String sqlValue;
                if (value == null) {
                    sqlValue = "null";
                } else if ("String".equals(value.getClass().getSimpleName())) {
                    sqlValue = "\'" + value + "\'";
                } else if ("Date".equals(value.getClass().getSimpleName())) {
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    sqlValue = "\'" + formatter.format(value) + "\'";
                } else if ("Boolean".equals(value.getClass().getSimpleName())) {
                    if ((Boolean) value) {
                        sqlValue = "true";
                    } else {
                        sqlValue = "false";
                    }
                } else {
                    sqlValue = value.toString();
                }
                if (FastDaoAttributes.getDaoActuatorClass().equals(FastMyBatisImpl.class)) {
                    sql = sql.replaceAll("[#][{](paramMap.)(" + key + ")[}]", sqlValue);
                } else if (FastDaoAttributes.getDaoActuatorClass().equals(JdbcImpl.class)) {
                    sql = sql.replaceAll("[:](" + key + ")([,]|[)]|[\\s]|[;])", sqlValue + "$2");
                }
            }
        }

        String result = "";
        if (FastDaoAttributes.isSqlPrintResult) {
            result = "执行结果: " + StrUtil.CRLF + JSONObject.toJSONString(o) + StrUtil.CRLF;
        }
        Log log = LogFactory.get(param.getTableMapper().getObjClass());
        log.info(sql.substring(0, sql.indexOf(" ")) + " -> " + param.getTableMapper().getTableName() + ": SQL执行报告↓ " + StrUtil.CRLF + sql + StrUtil.CRLF + "本次执行耗时:" + param.getSqlTime() + "毫秒" + StrUtil.CRLF + result);
    }

    /**
     * 封装select需要查询的字段信息,
     *
     * @param select      条件参数
     * @param tableMapper 映射信息
     * @return 封装后的SQL语句
     */
    public static String selectShowField(ConditionPackages select, TableMapper tableMapper) {
        List<String> fieldNames = tableMapper.getFieldNames();
        Map<String, String> fieldTableNames = tableMapper.getShowTableNames();
        StringBuilder selectShowField = new StringBuilder();

        if (select == null) {
            return tableMapper.getShowAllTableNames();
        }
        if (CollUtil.isNotEmpty(select.getDistinctFields())) {
            String distinctQuery = "DISTINCT" + " ";
            for (String distinctField : select.getDistinctFields()) {
                distinctQuery += (fieldTableNames.get(distinctField) + ", ");
            }
            return distinctQuery.substring(0, distinctQuery.length() - 1);
        }
        if (CollUtil.isNotEmpty(select.getShowFields())) {
            for (String showField : select.getShowFields()) {
                selectShowField.append(fieldTableNames.get(showField) + ", ");
            }
        } else if (CollUtil.isNotEmpty(select.getHideFields())) {
            for (String fieldName : fieldNames) {
                if (!select.getHideFields().contains(fieldName)) {
                    selectShowField.append(fieldTableNames.get(fieldName) + ", ");
                }
            }
        }
        if (selectShowField.length() == 0) {
            return tableMapper.getShowAllTableNames();
        } else {
            return selectShowField.substring(0, selectShowField.length() - 2);
        }
    }

    /**
     * 对封装SQL拼接时的参数信息
     *
     * @param paramMap   参数
     * @param value      值
     * @param paramIndex 索引
     * @return 封装后的SQL
     */
    private static String pageParam(Map<String, Object> paramMap, Object value, ParamIndex paramIndex) {
        String paramKey = "where_param_" + paramIndex.get();
        paramMap.put(paramKey, value);
        String s = "#{" + paramKey + "}";
        paramIndex.add();
        return s;
    }

    /**
     * 参数索引
     */
    private static class ParamIndex {
        private int index = 0;

        public int get() {
            return index;
        }

        public void add() {
            this.index++;
        }
    }

    /**
     * 对SQL where条件进行封装
     *
     * @param select      条件信息
     * @param fastSQL     sql信息
     * @param paramMap    参数信息
     * @param tableMapper 映射信息
     */
    public static void whereSql(ConditionPackages select, FastSQL fastSQL, Map<String, Object> paramMap, TableMapper tableMapper) {
        if (select == null) {
            if (FastDaoAttributes.isOpenLogicDelete) {
                fastSQL.AND().WHERE("`" + FastDaoAttributes.deleteTableColumnName + "` = " + !FastDaoAttributes.defaultDeleteValue);
            }
            return;
        }

        if (select.getLogicDeleteProtect()) {
            if (FastDaoAttributes.isOpenLogicDelete) {
                fastSQL.AND().WHERE("`" + FastDaoAttributes.deleteTableColumnName + "` = " + !FastDaoAttributes.defaultDeleteValue);
            }
        }

        ParamIndex paramIndex = new ParamIndex();
        List<FastCondition> conditions = select.getConditions();
        for (FastCondition condition : conditions) {
            whereCondition(condition, fastSQL, paramMap, tableMapper, paramIndex);
        }
    }

    /**
     * where条件封装器
     *
     * @param condition   条件信息
     * @param fastSQL     SQL信息
     * @param paramMap    参数信息
     * @param tableMapper 对象属性映射
     * @param paramIndex  参数角标
     */
    private static void whereCondition(FastCondition condition, FastSQL fastSQL, Map<String, Object> paramMap, TableMapper tableMapper, ParamIndex paramIndex) {
        switch (condition.getExpression().name) {
            case "in":
            case "notIn":
                if (condition.getWay().equals(FastCondition.Way.AND)) {
                    fastSQL.AND();
                } else {
                    fastSQL.OR();
                }
                String vals = "";
                for (Object value : condition.getValueList()) {
                    vals = vals + pageParam(paramMap, value, paramIndex) + ",";
                }
                fastSQL.WHERE(tableMapper.getShowTableNames().get(condition.getField()) + condition.getExpression().expression
                        + "(" + vals.substring(0, vals.length() - 1) + ") ");
                break;
            case "between":
            case "notBetween":
                if (condition.getWay().equals(FastCondition.Way.AND)) {
                    fastSQL.AND();
                } else {
                    fastSQL.OR();
                }
                fastSQL.WHERE(tableMapper.getShowTableNames().get(condition.getField()) + condition.getExpression().expression
                        + pageParam(paramMap, condition.getBetweenMin(), paramIndex) + " AND " + pageParam(paramMap, condition.getBetweenMax(), paramIndex));
                break;
            case "isNull":
            case "isNotNull":
                if (condition.getWay().equals(FastCondition.Way.AND)) {
                    fastSQL.AND();
                } else {
                    fastSQL.OR();
                }
                fastSQL.WHERE(tableMapper.getShowTableNames().get(condition.getField()) + condition.getExpression().expression);
                break;
            case "sql":
                if (condition.getWay().equals(FastCondition.Way.AND)) {
                    fastSQL.AND();
                } else {
                    fastSQL.OR();
                }
                fastSQL.WHERE(condition.getSql());
                if (CollUtil.isNotEmpty(condition.getParams())) {
                    paramMap.putAll(condition.getParams());
                }
                break;
            case "object":
                fastSQL.AND();
                Map<String, Object> pojoFieldTable = BeanUtil.beanToMap(condition.getObject(), false, true);
                for (String fieldName : pojoFieldTable.keySet()) {
                    fastSQL.WHERE(tableMapper.getShowTableNames().get(fieldName) + condition.getExpression().expression
                            + pageParam(paramMap, pojoFieldTable.get(fieldName), paramIndex));
                }
                break;
            default:
                if (condition.getWay().equals(FastCondition.Way.AND)) {
                    fastSQL.AND();
                } else {
                    fastSQL.OR();
                }
                fastSQL.WHERE(tableMapper.getShowTableNames().get(condition.getField()) + condition.getExpression().expression
                        + pageParam(paramMap, condition.getValue(), paramIndex));
        }
    }

    /**
     * 对更新SQL进行封装
     *
     * @param fastSQL     SQL信息
     * @param obj         更新对象信息
     * @param isSelective 是否对参数为null的属性进行操作
     * @param paramMap    参数信息
     * @param tableMapper 映射信息
     */
    public static void updateSql(FastSQL fastSQL, Object obj, Boolean isSelective, Map<String, Object> paramMap, TableMapper tableMapper) {
        int updateIndex = 0;
        List<String> fieldNames = tableMapper.getFieldNames();
        Map<String, String> fieldTableNames = tableMapper.getShowTableNames();

        for (String fieldName : fieldNames) {
            if (FastDaoAttributes.isAutoSetCreateTime && fieldName.equals(FastDaoAttributes.createTimeFieldName)) {
                continue;
            }
            Object fieldValue = BeanUtil.getFieldValue(obj, fieldName);
            if (fieldValue == null) {
                if (isSelective) {
                    continue;
                }
                if (FastDaoAttributes.isOpenLogicDelete && fieldName.equals(FastDaoAttributes.deleteFieldName)) {
                    continue;
                }
            }
            String paramKey = "update_param_" + updateIndex;
            fastSQL.SET(fieldTableNames.get(fieldName) + " = " + "#{" + paramKey + "}");
            paramMap.put(paramKey, fieldValue);
            updateIndex++;
        }
    }

    /**
     * 查询排序
     *
     * @param conditionPackages 条件信息
     * @param fastSQL           SQL信息
     * @param tableMapper       映射
     */
    public static void orderBy(ConditionPackages conditionPackages, FastSQL fastSQL, TableMapper tableMapper) {
        if (conditionPackages != null && conditionPackages.getOrderByQuery() != null) {
            for (ConditionPackages.OrderByQuery orderByQuery : conditionPackages.getOrderByQuery()) {
                if (orderByQuery.getDesc()) {
                    fastSQL.ORDER_BY(tableMapper.getShowTableNames().get(orderByQuery.getOrderByName()) + " desc ");
                } else {
                    fastSQL.ORDER_BY(tableMapper.getShowTableNames().get(orderByQuery.getOrderByName()) + " asc ");
                }
            }
        }
    }

    public static String sqlConversion(String sql) {
        if (FastDaoAttributes.getDaoActuatorClass().equals(FastMyBatisImpl.class)) {
            return sql.replaceAll("[#][{]", "#{paramMap.");
        } else if (FastDaoAttributes.getDaoActuatorClass().equals(JdbcImpl.class)) {
            return sql.replaceAll("[#][{](\\w*)[}]", ":$1");
        }
        return sql;
    }

}

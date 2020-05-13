package com.fast.dao.utils;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.text.StrBuilder;
import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import com.alibaba.fastjson.JSONObject;
import com.fast.config.FastDaoAttributes;
import com.fast.config.SqlLogLevel;
import com.fast.fast.FastDaoParam;

import java.util.Map;

public class FastSqlPrintLog {

    private static final String APOSTROPHE = "\'";
    private static final String DATA_STR = "Date";
    private static final String NULL_STR = "null";
    private static final String TRUE_STR = "true";
    private static final String FALSE_STR = "false";
    private static final String PARAM_PREFIX = "#{";
    private static final String PARAM_SUFFIX = "}";
    private static final String SQL_REPORT = ": SQL 执行 ↓ " + System.lineSeparator();
    private static final String PARAM = "参数: ";
    private static final String RESULT = "执行结果: ";
    private static final String TIME = "用时: ";
    private static final String TIME_TYPE = "毫秒" + System.lineSeparator();

    /**
     * SQL日志打印
     *
     * @param param DAO执行参数
     */
    public static void printSql(FastDaoParam param) {
        if (FastDaoAttributes.sqlLogLevel.equals(SqlLogLevel.OFF)) {
            return;
        }
        if (FastDaoAttributes.isSqlSimplePrint) {
            printSql(param.getSql(), param.getParamMap(), param);
            return;
        }
        Map<String, Object> paramMap = param.getParamMap();
        String sql = param.getSql();
        if (CollUtil.isNotEmpty(paramMap)) {
            for (String key : paramMap.keySet()) {
                Object value = paramMap.get(key);
                String sqlValue;
                if (value == null) {
                    sqlValue = NULL_STR;
                } else if (value instanceof CharSequence) {
                    sqlValue = StrUtil.strBuilder(APOSTROPHE, Convert.toStr(value), APOSTROPHE).toString();
                } else if (DATA_STR.equals(value.getClass().getSimpleName())) {
                    sqlValue = StrUtil.strBuilder(APOSTROPHE, DateUtil.formatDateTime(Convert.toDate(value)), APOSTROPHE).toString();
                } else if (BooleanUtil.isBoolean(value.getClass())) {
                    if ((Boolean) value) {
                        sqlValue = TRUE_STR;
                    } else {
                        sqlValue = FALSE_STR;
                    }
                } else {
                    sqlValue = value.toString();
                }
                sql = StrUtil.replace(sql, StrUtil.strBuilder(PARAM_PREFIX, key, PARAM_SUFFIX), sqlValue);
            }
        }
        printSql(sql, null, param);
    }

    private static void printSql(String sql, Map<String, Object> sqlParams, FastDaoParam param) {
        Log log = LogFactory.get(param.getTableMapper().getObjClass());
        StrBuilder printLog = StrUtil.strBuilder(param.getTableMapper().getTableName(), SQL_REPORT, sql, TIME, param.getSqlTime().toString(), TIME_TYPE);
        if (sqlParams != null) {
            printLog.append(PARAM);
            printLog.append(JSONObject.toJSONString(sqlParams));
            printLog.append(System.lineSeparator());
        }
        if (FastDaoAttributes.isSqlPrintResult) {
            printLog.append((RESULT));
            printLog.append(JSONObject.toJSONString(param.getReturnVal()));
            printLog.append(System.lineSeparator());
        }
        switch (FastDaoAttributes.sqlLogLevel) {
            case INFO:
                log.info(printLog.toString());
                break;
            case DEBUG:
                log.debug(printLog.toString());
                break;
            default:
                break;
        }
    }
}

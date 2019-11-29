package com.fast.dao.utils;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.text.StrBuilder;
import cn.hutool.core.util.StrUtil;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import com.alibaba.fastjson.JSONObject;
import com.fast.config.FastDaoAttributes;
import com.fast.config.SqlLogLevel;
import com.fast.fast.FastDaoParam;

import java.text.SimpleDateFormat;
import java.util.Map;

public class FastSqlPrintLog {

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
                if (FastDaoAttributes.IS_JDBC_PARAM_TYPE) {
                    sql = sql.replaceAll("[:](" + key + ")[\\s]", sqlValue);
                } else {
                    sql = sql.replaceAll("[#][{](paramMap.)(" + key + ")[}]", sqlValue);
                }
            }
        }
        printSql(sql, null, param);
    }

    private static void printSql(String sql, Map<String, Object> sqlParams, FastDaoParam param) {
        Log log = LogFactory.get(param.getTableMapper().getObjClass());
        StrBuilder printLog = StrUtil.strBuilder(param.getTableMapper().getTableName(), ": SQL执行报告↓ ", System.lineSeparator(), sql, System.lineSeparator(), "执行耗时: " + param.getSqlTime(), System.lineSeparator());
        if (sqlParams != null) {
            printLog.append("参数: ");
            printLog.append(JSONObject.toJSONString(sqlParams));
            printLog.append(System.lineSeparator());
        }
        if (FastDaoAttributes.isSqlPrintResult) {
            printLog.append(("执行结果: "));
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
        }
    }
}

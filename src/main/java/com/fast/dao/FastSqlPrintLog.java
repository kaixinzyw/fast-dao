package com.fast.dao;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.text.StrBuilder;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import com.alibaba.fastjson.JSONObject;
import com.fast.base.FastBaseDAO;
import com.fast.base.FastBaseServiceImpl;
import com.fast.condition.ConditionPackages;
import com.fast.config.FastDaoAttributes;
import com.fast.config.SqlLogLevel;
import com.fast.fast.FastCustomSqlDao;
import com.fast.fast.FastDao;

import java.util.Collection;
import java.util.Map;

public class FastSqlPrintLog {

    private static final String FIND_PAGE = "findPage";
    private static final String LEFT_PARENTHESIS = "(";
    private static final String RIGHT_PARENTHESIS = ")";
    private static final String APOSTROPHE = "'";
    private static final String DATA_STR = "Date";
    private static final String NULL_STR = "null";
    private static final String TRUE_STR = "true";
    private static final String FALSE_STR = "false";
    private static final String PARAM_PREFIX = "#{";
    private static final String PARAM_PREFIX_2 = "${";
    private static final String PARAM_SUFFIX = "}";
    private static final String SQL_REPORT = " SQL 执行 ↓ " + System.lineSeparator();
    private static final String PARAM = "参数: ";
    private static final String RESULT = "执行结果: ";
    private static final String TIME = "用时: ";
    private static final String TIME_TYPE = "毫秒" + System.lineSeparator();

    /**
     * SQL日志打印
     *
     * @param conditionPackages DAO执行参数
     */
    public static void printSql(ConditionPackages conditionPackages) {
        if (FastDaoAttributes.sqlLogLevel.equals(SqlLogLevel.OFF)) {
            return;
        }
        conditionPackages.setSqlTime(System.currentTimeMillis() - conditionPackages.getSqlTime());
        if (FastDaoAttributes.isSqlSimplePrint) {
            printSql(conditionPackages.getSql(), conditionPackages.getParamMap(), conditionPackages);
            return;
        }
        Map<String, Object> paramMap = conditionPackages.getParamMap();
        String sql = conditionPackages.getSql();
        if (CollUtil.isNotEmpty(paramMap)) {
            for (String key : paramMap.keySet()) {
                Object value = paramMap.get(key);
                String sqlValue = getValueString(value);
                if (sql.contains(PARAM_PREFIX)) {
                    sql = StrUtil.replace(sql, StrUtil.strBuilder(PARAM_PREFIX, key, PARAM_SUFFIX), sqlValue);
                }
                if (sql.contains(PARAM_PREFIX_2)) {
                    sql = StrUtil.replace(sql, StrUtil.strBuilder(PARAM_PREFIX_2, key, PARAM_SUFFIX), sqlValue);
                }
            }
        }
        printSql(sql, null, conditionPackages);
    }

    private static String getValueString(Object value) {
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
        } else if (value instanceof Collection) {
            StrBuilder inStr = StrUtil.strBuilder();
            ((Collection) value).forEach(v -> inStr.append(getValueString(v)).append(StrUtil.COMMA));
            if (inStr.length() > 0) {
                sqlValue = inStr.subString(0, inStr.length() - 1);
            } else {
                sqlValue = "";
            }
        } else {
            sqlValue = value.toString();
        }
        return sqlValue;
    }

    private static void printSql(String sql, Map<String, Object> sqlParams, ConditionPackages conditionPackages) {
        StackTraceElement stackTraceElement = null;
        StackTraceElement[] traceElements = ThreadUtil.getStackTrace();
        for (int i = 0; i < traceElements.length; i++) {
            if (StrUtil.equalsAny(traceElements[i].getClassName(), FastDao.class.getName(), FastCustomSqlDao.class.getName())
                    && !StrUtil.equalsAny(traceElements[i + 1].getClassName(), FastBaseDAO.class.getName(), FastBaseServiceImpl.class.getName())) {
                if (StrUtil.equals(FIND_PAGE, traceElements[i].getMethodName())
                        && traceElements.length > i + 2) {
                    stackTraceElement = traceElements[i + 2];
                    break;
                } else {
                    stackTraceElement = traceElements[i + 1];
                    break;
                }
            }
        }
        StrBuilder strBuilder;
        if (ObjectUtil.isNotNull(stackTraceElement)) {
            strBuilder = new StrBuilder(stackTraceElement.getClassName(), StrUtil.DOT, stackTraceElement.getMethodName(), LEFT_PARENTHESIS, StrUtil.toString(stackTraceElement.getLineNumber()), RIGHT_PARENTHESIS);
        } else {
            strBuilder = new StrBuilder();
        }
        StrBuilder printLog = StrUtil.strBuilder(strBuilder.toString(), SQL_REPORT, sql);
        if (sqlParams != null) {
            printLog.append(PARAM);
            printLog.append(JSONObject.toJSONString(sqlParams));
            printLog.append(System.lineSeparator());
        }
        if (FastDaoAttributes.isSqlPrintResult) {
            printLog = StrUtil.strBuilder(StrUtil.addSuffixIfNot(printLog,System.lineSeparator()));
            printLog.append(StrBuilder.create(TIME, conditionPackages.getSqlTime().toString(), TIME_TYPE, RESULT, JSONObject.toJSONString(conditionPackages.getReturnVal()), System.lineSeparator()));
        }
        logPrint(conditionPackages, printLog.toString());
    }


    private static void logPrint(ConditionPackages conditionPackages, String logInfo) {
        Log log = LogFactory.get(conditionPackages.getTableMapper().getTableName());
        switch (FastDaoAttributes.sqlLogLevel) {
            case INFO:
                log.info(logInfo);
                break;
            case DEBUG:
                log.debug(logInfo);
                break;
            default:
                break;
        }
    }
}

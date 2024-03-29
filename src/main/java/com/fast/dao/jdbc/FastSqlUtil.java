package com.fast.dao.jdbc;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.text.StrBuilder;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.ReUtil;
import cn.hutool.core.util.StrUtil;
import com.fast.condition.*;
import com.fast.config.FastDaoAttributes;
import com.fast.fast.JoinFastDao;
import com.fast.mapper.TableMapper;
import io.netty.util.concurrent.FastThreadLocal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * SQL拼接工具类
 *
 * @author 张亚伟 https://github.com/kaixinzyw
 */
public class FastSqlUtil {

    public static final String WHERE_PARAM_TYPE = "where_param_";
    public static final String LIMIT_PARAM_TYPE = "limit_param_";
    public static final String UPDATE_PARAM_TYPE = "update_param_";
    public static final String INSERT_PARAM_TYPE = "insert_param_";
    public static final String FROM = "FROM ";
    public static final String SET = " SET ";
    public static final String AND = FastCondition.Way.AND.expression;
    public static final String OR = FastCondition.Way.OR.expression;
    public static final String WHERE = "WHERE ";
    public static final String WHERE_CRLF = "WHERE " + System.lineSeparator();
    public static final Integer WHERE_CRLF_LENGTH = ("WHERE " + System.lineSeparator()).length();
    public static final String SELECT = "SELECT ";
    public static final String UPDATE = "UPDATE ";
    public static final String DELETE = "DELETE FROM ";
    public static final String INSERT = "INSERT INTO ";
    public static final String COUNT = "COUNT";
    public static final String LIMIT = "LIMIT ";
    public static final String DISTINCT = "DISTINCT";
    public static final String SUM = "SUM";
    public static final String AVG = "AVG";
    public static final String MIN = "MIN";
    public static final String MAX = "MAX";
    public static final String GROUP = "GROUP";
    public static final String ONE_COUNT = ">0";
    public static final String ORDER_BY = "ORDER BY ";
    public static final String DESC = " DESC ";
    public static final String ASC = " ASC ";
    public static final String CRLF = System.lineSeparator();
    public static final String COMMA = ", ";
    public static final String EQUAL = " = ";
    public static final String QUOTATION = "`";
    public static final String LEFT_BRACKETS = "(";
    public static final String RIGHT_BRACKETS = ")";
    public static final String VALUES = " VALUES ";
    public static final String WILDCARD = "*";
    public static final String PARAM_PREFIX = "#{";
    public static final String PARAM_PREFIX_2 = "${";
    public static final String MYBATIS_PARAM_PREFIX = "#{paramMap.";
    public static final String MYBATIS_PARAM_PREFIX_2 = "${paramMap.";
    public static final String PARAM_INDEX_PREFIX = "param_";
    public static final String PARAM_SUFFIX = "} ";
    public static final String JDBC_SQL_CONVERSION_RE_RULE = "[#][{](\\w*)[}]";
    public static final String JDBC_SQL_CONVERSION_RE_RULE_2 = "[$][{](\\w*)[}]";
    public static final String JDBC_SQL_CONVERSION_RE_RESULT = ":$1";
    public static final String JDBC_SQL_NEW_TIME_FUNCTION = "NOW()";
    public static String[] SPECIAL_COUNT_QUERY = {
            SUM + StrUtil.SPACE, SUM + LEFT_BRACKETS,
            AVG + StrUtil.SPACE, AVG + LEFT_BRACKETS,
            MIN + StrUtil.SPACE, MIN + LEFT_BRACKETS,
            MAX + StrUtil.SPACE, MAX + LEFT_BRACKETS};

    private static final FastThreadLocal<ParamIndex> PARAM_INDEX_THREAD_LOCAL = new FastThreadLocal<>();

    private static ParamIndex getParamIndex() {
        ParamIndex paramIndex = PARAM_INDEX_THREAD_LOCAL.get();
        if (paramIndex == null) {
            paramIndex = new ParamIndex();
            PARAM_INDEX_THREAD_LOCAL.set(paramIndex);
        }
        return paramIndex;
    }

    /**
     * 对封装SQL拼接时的参数信息
     *
     * @param paramMap   参数
     * @param value      值
     * @param paramIndex 索引
     * @return 封装后的SQL
     */
    private static StrBuilder packParam(StrBuilder sqlBuilder, Map<String, Object> paramMap, Object value, ParamIndex paramIndex) {
        String paramKey = PARAM_INDEX_PREFIX + paramIndex.addAndGet();
        paramMap.put(paramKey, value);
        return packJdbcParam(sqlBuilder, paramKey);
    }

    private static StrBuilder packJdbcParam(StrBuilder sqlBuilder, String paramKey) {
        sqlBuilder.append(PARAM_PREFIX_2).append(paramKey).append(PARAM_SUFFIX);
        return sqlBuilder;
    }


    /**
     * 参数索引
     */
    private static class ParamIndex {
        private int index = 0;

        public int addAndGet() {
            this.index++;
            return this.index;
        }
    }

    private static String getSqlTableName(boolean isAddTableName, ConditionPackages select) {
        return isAddTableName ? StrUtil.isNotBlank(select.getTableMapper().getTableAlias()) ? select.getTableAlias() + StrUtil.DOT : select.getTableMapper().getTableName() + StrUtil.DOT
                : StrUtil.EMPTY;
    }


    public static StrBuilder whereSql(ConditionPackages select, Map paramMap, boolean isAddDeleted, boolean isAddTableName) {
        StrBuilder sqlBuilder = new StrBuilder();
        TableMapper tableMapper = select.getTableMapper();
        boolean isFirstCondition = Boolean.TRUE;
        if (select.getLogicDeleteProtect() && tableMapper.getLogicDelete()) {
            if (isAddDeleted) {
                sqlBuilder.append(getSqlTableName(isAddTableName, select));
                sqlBuilder.append(!FastDaoAttributes.defaultDeleteValue ?
                        FastDaoAttributes.defaultSqlWhereDeleteValueTrue : FastDaoAttributes.defaultSqlWhereDeleteValueFalse);
                isFirstCondition = Boolean.FALSE;
                sqlBuilder.append(CRLF);
            }
        }
        ParamIndex paramIndex = getParamIndex();
        List<FastCondition> conditions = select.getConditions();
        if (CollUtil.isNotEmpty(conditions)) {
            int leftBracketNum = 0;
            int leftBracket = 0;
            for (int i = 0; i < conditions.size(); i++) {
                FastCondition condition = conditions.get(i);
                if (ObjectUtil.equal(condition.getExpression(), FastCondition.Expression.LeftBracket)) {
                    leftBracket++;
                    leftBracketNum = 2;
                    sqlBuilder.append(condition.getWay().expression);
                } else if (ObjectUtil.equal(condition.getExpression(), FastCondition.Expression.RightBracket)) {
                    leftBracket--;
                }
                if (!isFirstCondition && leftBracketNum == 0) {
                    if (condition.getWay() != null) {
                        sqlBuilder.append(condition.getWay().expression);
                    }
                }
                whereCondition(condition, sqlBuilder, paramMap, select, paramIndex, isAddTableName);
                if (isFirstCondition) {
                    if (sqlBuilder.length() > 0) {
                        isFirstCondition = Boolean.FALSE;
                    }
                }
                if (leftBracket == 0 && sqlBuilder.length() > 0 && i < conditions.size() - 1) {
                    sqlBuilder.append(CRLF);
                }
                if (leftBracketNum != 0) {
                    leftBracketNum--;
                }
            }
        }
        return sqlBuilder;
    }

    /**
     * where条件封装器
     *
     * @param condition  条件信息
     * @param sqlBuilder SQL信息
     * @param paramMap   参数信息
     * @param select     对象属性映射
     * @param paramIndex 参数角标
     */
    private static void whereCondition(FastCondition condition, StrBuilder sqlBuilder, Map<String, Object> paramMap,
                                       ConditionPackages select, ParamIndex paramIndex, boolean isAddTableName) {
        TableMapper tableMapper = select.getTableMapper();
        switch (condition.getExpression()) {
            case In:
            case NotIn:
                sqlBuilder.append(getSqlTableName(isAddTableName, select)).append(tableMapper.getShowTableNames().get(condition.getField()))
                        .append(condition.getExpression().expression).append(LEFT_BRACKETS);
                packParam(sqlBuilder, paramMap, condition.getValueList(), paramIndex);
//                for (Object value : condition.getValueList()) {
//                    packParam(sqlBuilder, paramMap, value, paramIndex).append(StrUtil.COMMA);
//                }
                sqlBuilder.del(sqlBuilder.length() - 1, sqlBuilder.length()).append(RIGHT_BRACKETS);
                break;
            case Match:
            case NotMatch:
                sqlBuilder.append(condition.getExpression().name).append(LEFT_BRACKETS).append(getSqlTableName(isAddTableName, select)).append(tableMapper.getShowTableNames().get(condition.getField()))
                        .append(RIGHT_BRACKETS).append(condition.getExpression().expression).append(LEFT_BRACKETS);
                packParam(sqlBuilder, paramMap, condition.getValue(), paramIndex).append(RIGHT_BRACKETS);
                break;
            case Between:
            case NotBetween:
                sqlBuilder.append(getSqlTableName(isAddTableName, select)).append(tableMapper.getShowTableNames()
                        .get(condition.getField())).append(condition.getExpression().expression);
                packParam(sqlBuilder, paramMap, condition.getBetweenMin(), paramIndex).append(AND);
                packParam(sqlBuilder, paramMap, condition.getBetweenMax(), paramIndex);
                break;
            case Null:
            case NotNull:
                sqlBuilder.append(getSqlTableName(isAddTableName, select)).append(tableMapper.getShowTableNames()
                        .get(condition.getField())).append(condition.getExpression().expression);
                break;
            case LeftBracket:
                sqlBuilder.append(condition.getExpression().expression);
                break;
            case RightBracket:
                sqlBuilder.append(condition.getExpression().expression);
                break;
            case SQL:
                if (CollUtil.isNotEmpty(condition.getParams())) {
                    paramMap.putAll(condition.getParams());
                }
                sqlBuilder.append(condition.getSql());
                break;
            case Obj:
                HashMap<String, String> showTableNames = tableMapper.getShowTableNames();
                Map<String, Object> fieldMap;
                if (condition.getObject() instanceof Map) {
                    fieldMap = (Map<String, Object>) condition.getObject();
                    if (CollUtil.isNotEmpty(fieldMap)) {
                        for (String fieldName : fieldMap.keySet()) {
                            String showTable = getSqlTableName(isAddTableName, select) + showTableNames.get(fieldName);
                            Object val = fieldMap.get(fieldName);
                            if (val != null) {
                                sqlBuilder.append(showTable).append(condition.getExpression().expression);
                                packParam(sqlBuilder, paramMap, fieldMap.get(fieldName), paramIndex);
                                sqlBuilder.append(AND);
                            }
                        }
                    }
                } else {
                    fieldMap = BeanUtil.beanToMap(condition.getObject(), false, true);
                    if (CollUtil.isNotEmpty(fieldMap)) {
                        for (String fieldName : fieldMap.keySet()) {
                            String showTable = showTableNames.get(fieldName);
                            if (showTable == null) {
                                continue;
                            }
                            sqlBuilder.append(getSqlTableName(isAddTableName, select)).append(showTable).append(condition.getExpression().expression);
                            packParam(sqlBuilder, paramMap, fieldMap.get(fieldName), paramIndex);
                            sqlBuilder.append(AND);
                        }
                    }
                }
                if (StrUtil.lastIndexOf(sqlBuilder, AND, sqlBuilder.length(), false) == sqlBuilder.length() - AND.length()) {
                    sqlBuilder.del(sqlBuilder.length() - AND.length(), sqlBuilder.length());
                }
                break;
            default:
                sqlBuilder.append(getSqlTableName(isAddTableName, select)).append(tableMapper.getShowTableNames()
                        .get(condition.getField())).append(condition.getExpression().expression);
                packParam(sqlBuilder, paramMap, condition.getValue(), paramIndex);
        }
    }

    /**
     * 查询排序
     *
     * @param sqlBuilder        SQL信息
     * @param conditionPackages Dao执行条件
     * @param <T>               操作对象类型
     */
    public static <T> void orderBy(StrBuilder sqlBuilder, ConditionPackages<T> conditionPackages) {
        TableMapper tableMapper = conditionPackages.getTableMapper();
        if (conditionPackages.getOrderByQuery() != null) {
            sqlBuilder.append(CRLF);
            sqlBuilder.append(ORDER_BY);
            for (int i = 0; i < conditionPackages.getOrderByQuery().size(); i++) {
                OrderByQuery orderByQuery = conditionPackages.getOrderByQuery().get(i);
                if (orderByQuery.getDesc()) {
                    sqlBuilder.append(tableMapper.getShowTableNames().get(orderByQuery.getOrderByName())).append(DESC);
                } else {
                    sqlBuilder.append(tableMapper.getShowTableNames().get(orderByQuery.getOrderByName())).append(ASC);
                }
                if (i < conditionPackages.getOrderByQuery().size() - 1) {
                    sqlBuilder.append(COMMA);
                }
            }
        }
    }

    /**
     * 分页
     *
     * @param sqlBuilder        SQL信息
     * @param conditionPackages Dao执行条件
     */
    public static void limit(StrBuilder sqlBuilder, ConditionPackages conditionPackages) {
        if (conditionPackages.getPage() != null && conditionPackages.getSize() != null) {
            sqlBuilder.append(CRLF);
            ParamIndex paramIndex = getParamIndex();
            Map<String, Object> paramMap = conditionPackages.getParamMap();
            sqlBuilder.append(LIMIT);
            packParam(sqlBuilder, paramMap, conditionPackages.getPage(), paramIndex).append(COMMA);
            packParam(sqlBuilder, paramMap, conditionPackages.getSize(), paramIndex).append(CRLF);
        } else if (conditionPackages.getLimit() != null) {
            sqlBuilder.append(CRLF);
            ParamIndex paramIndex = getParamIndex();
            Map<String, Object> paramMap = conditionPackages.getParamMap();
            sqlBuilder.append(LIMIT);
            packParam(sqlBuilder, paramMap, conditionPackages.getLimit(), paramIndex).append(CRLF);
        }
    }

    private static final String CITE = "`";

    private static final Map<String, String> JOIN_SHOW_PREFIX_ALL_MAP = new HashMap<>();

    /**
     * 封装select需要查询的字段信息,
     *
     * @param <T>               返回结果类型
     * @param conditionPackages Dao执行条件
     * @param resultCondition   结果条件
     * @param resultTableAlias  结果表别名
     * @param joinTableAlias    连接表别名
     * @return 封装后的SQL
     */
    public static <T> String joinSelectSql(ConditionPackages<T> resultCondition, String resultTableAlias, String joinTableAlias, ConditionPackages<T> conditionPackages) {
        StrBuilder sqlBuilder = StrUtil.strBuilder();
        TableMapper tableMapper = conditionPackages.getTableMapper();
        Map<String, String> fieldTableNames = tableMapper.getFieldTableNames();
        if (CollUtil.isNotEmpty(conditionPackages.getCustomQueryColumns())) {
            StrBuilder selectColumn = StrBuilder.create();
            for (String queryColumn : conditionPackages.getCustomQueryColumns()) {
                selectColumn.append(StrUtil.strBuilder(joinTableAlias, StrUtil.DOT, CITE, queryColumn, CITE, COMMA));
            }
            sqlBuilder.append(selectColumn.subString(0, selectColumn.length() - 2));
        } else if (CollUtil.isNotEmpty(conditionPackages.getSumFields()) || CollUtil.isNotEmpty(conditionPackages.getAvgFields())
                || CollUtil.isNotEmpty(conditionPackages.getMinFields()) || CollUtil.isNotEmpty(conditionPackages.getMaxFields())) {
            if (CollUtil.isNotEmpty(conditionPackages.getSumFields())) {
                for (String sumField : conditionPackages.getSumFields()) {
                    sqlBuilder.append(SUM).append(LEFT_BRACKETS).append(joinTableAlias)
                            .append(CITE).append(fieldTableNames.get(sumField)).append(CITE).append(RIGHT_BRACKETS)
                            .append(StrUtil.SPACE).append(fieldTableNames.get(sumField)).append(COMMA);
                }
            }
            if (CollUtil.isNotEmpty(conditionPackages.getAvgFields())) {
                for (String avgField : conditionPackages.getAvgFields()) {
                    sqlBuilder.append(AVG).append(LEFT_BRACKETS).append(joinTableAlias)
                            .append(CITE).append(fieldTableNames.get(avgField)).append(CITE).append(RIGHT_BRACKETS)
                            .append(StrUtil.SPACE).append(fieldTableNames.get(avgField)).append(COMMA);
                }
            }
            if (CollUtil.isNotEmpty(conditionPackages.getMinFields())) {
                for (String minField : conditionPackages.getMinFields()) {
                    sqlBuilder.append(MIN).append(LEFT_BRACKETS).append(joinTableAlias)
                            .append(CITE).append(fieldTableNames.get(minField)).append(CITE).append(RIGHT_BRACKETS)
                            .append(StrUtil.SPACE).append(fieldTableNames.get(minField)).append(COMMA);
                }
            }
            if (CollUtil.isNotEmpty(conditionPackages.getMaxFields())) {
                for (String maxField : conditionPackages.getMaxFields()) {
                    sqlBuilder.append(MAX).append(LEFT_BRACKETS).append(joinTableAlias)
                            .append(CITE).append(fieldTableNames.get(maxField)).append(CITE).append(RIGHT_BRACKETS)
                            .append(StrUtil.SPACE).append(fieldTableNames.get(maxField)).append(COMMA);
                }
            }
            sqlBuilder.del(sqlBuilder.length() - 2, sqlBuilder.length());
        } else if (CollUtil.isNotEmpty(conditionPackages.getDistinctFields())) {
            StrBuilder selectField = StrBuilder.create(DISTINCT, StrUtil.SPACE);
            for (String distinctField : conditionPackages.getDistinctFields()) {
                selectField.append(StrUtil.strBuilder(joinTableAlias, StrUtil.DOT, CITE, fieldTableNames.get(distinctField), CITE, COMMA));
            }
            sqlBuilder.append(selectField.subString(0, selectField.length() - 2));
        } else if (CollUtil.isNotEmpty(conditionPackages.getShowFields())) {
            StrBuilder selectField = StrBuilder.create();
            for (String showField : conditionPackages.getShowFields()) {
                selectField.append(StrUtil.strBuilder(joinTableAlias, StrUtil.DOT, CITE, fieldTableNames.get(showField), CITE, COMMA));
            }
            sqlBuilder.append(selectField.subString(0, selectField.length() - 2));
        } else if (CollUtil.isNotEmpty(conditionPackages.getHideFields())) {
            StrBuilder selectField = StrBuilder.create();
            List<String> fieldNames = tableMapper.getFieldNames();
            for (String fieldName : fieldNames) {
                if (!conditionPackages.getHideFields().contains(fieldName)) {
                    selectField.append(StrUtil.strBuilder(joinTableAlias, StrUtil.DOT, CITE, fieldTableNames.get(fieldName), CITE, COMMA));
                }
            }
            sqlBuilder.append(selectField.subString(0, selectField.length() - 2));
        } else {
            String joinKey = StrBuilder.create(resultTableAlias, StrUtil.UNDERLINE,
                    joinTableAlias).toString();
            String queryColumnNames = JOIN_SHOW_PREFIX_ALL_MAP.get(joinKey);
            if (queryColumnNames == null) {
                StrBuilder showPrefixAll = StrUtil.strBuilder();
                for (String tableFieldName : conditionPackages.getTableMapper().getTableFieldNames().keySet()) {
                    showPrefixAll.append(StrUtil.strBuilder(joinTableAlias, StrUtil.DOT, CITE, tableFieldName, CITE, COMMA));
                }
                queryColumnNames = showPrefixAll.del(showPrefixAll.length() - 2, showPrefixAll.length()).toString();
                JOIN_SHOW_PREFIX_ALL_MAP.put(joinKey, queryColumnNames);
            }
            return queryColumnNames;
        }
        return sqlBuilder.toString();
    }

    /**
     * 封装select需要查询的字段信息,
     *
     * @param conditionPackages Dao执行条件
     * @param <T>               操作对象类型
     * @return 封装后的SQL
     */
    public static <T> StrBuilder selectSql(ConditionPackages<T> conditionPackages) {
        StrBuilder sqlBuilder = StrUtil.strBuilder(SELECT);
        TableMapper tableMapper = conditionPackages.getTableMapper();
        Map<String, String> fieldTableNames = tableMapper.getShowTableNames();

        if (CollUtil.isNotEmpty(conditionPackages.getCustomQueryColumns())) {
            StrBuilder selectColumn = StrBuilder.create();
            for (String queryColumn : conditionPackages.getCustomQueryColumns()) {
                selectColumn.append(queryColumn).append(COMMA);
            }
            sqlBuilder.append(selectColumn.subString(0, selectColumn.length() - 2));
        } else if (CollUtil.isNotEmpty(conditionPackages.getSumFields()) || CollUtil.isNotEmpty(conditionPackages.getAvgFields())
                || CollUtil.isNotEmpty(conditionPackages.getMinFields()) || CollUtil.isNotEmpty(conditionPackages.getMaxFields())) {
            if (CollUtil.isNotEmpty(conditionPackages.getSumFields())) {
                for (String sumField : conditionPackages.getSumFields()) {
                    sqlBuilder.append(SUM).append(LEFT_BRACKETS)
                            .append(fieldTableNames.get(sumField)).append(RIGHT_BRACKETS)
                            .append(StrUtil.SPACE).append(fieldTableNames.get(sumField)).append(COMMA);
                }
            }
            if (CollUtil.isNotEmpty(conditionPackages.getAvgFields())) {
                for (String avgField : conditionPackages.getAvgFields()) {
                    sqlBuilder.append(AVG).append(LEFT_BRACKETS)
                            .append(fieldTableNames.get(avgField)).append(RIGHT_BRACKETS)
                            .append(StrUtil.SPACE).append(fieldTableNames.get(avgField)).append(COMMA);
                }
            }
            if (CollUtil.isNotEmpty(conditionPackages.getMinFields())) {
                for (String minField : conditionPackages.getMinFields()) {
                    sqlBuilder.append(MIN).append(LEFT_BRACKETS)
                            .append(fieldTableNames.get(minField)).append(RIGHT_BRACKETS)
                            .append(StrUtil.SPACE).append(fieldTableNames.get(minField)).append(COMMA);
                }
            }
            if (CollUtil.isNotEmpty(conditionPackages.getMaxFields())) {
                for (String maxField : conditionPackages.getMaxFields()) {
                    sqlBuilder.append(MAX).append(LEFT_BRACKETS)
                            .append(fieldTableNames.get(maxField)).append(RIGHT_BRACKETS)
                            .append(StrUtil.SPACE).append(fieldTableNames.get(maxField)).append(COMMA);
                }
            }
            sqlBuilder.del(sqlBuilder.length() - 2, sqlBuilder.length());
        } else if (CollUtil.isNotEmpty(conditionPackages.getDistinctFields())) {
            StrBuilder selectField = StrBuilder.create(DISTINCT, StrUtil.SPACE);
            for (String distinctField : conditionPackages.getDistinctFields()) {
                selectField.append(fieldTableNames.get(distinctField)).append(COMMA);
            }
            sqlBuilder.append(selectField.subString(0, selectField.length() - 2));
        } else if (CollUtil.isNotEmpty(conditionPackages.getShowFields())) {
            StrBuilder selectField = StrBuilder.create();
            for (String showField : conditionPackages.getShowFields()) {
                selectField.append(fieldTableNames.get(showField)).append(COMMA);
            }
            sqlBuilder.append(selectField.subString(0, selectField.length() - 2));
        } else if (CollUtil.isNotEmpty(conditionPackages.getHideFields())) {
            StrBuilder selectField = StrBuilder.create();
            List<String> fieldNames = tableMapper.getFieldNames();
            for (String fieldName : fieldNames) {
                if (!conditionPackages.getHideFields().contains(fieldName)) {
                    selectField.append(fieldTableNames.get(fieldName)).append(COMMA);
                }
            }
            sqlBuilder.append(selectField.subString(0, selectField.length() - 2));
        } else {
            sqlBuilder.append(tableMapper.getShowAllTableNames());
        }
        sqlBuilder.append(CRLF);
        sqlBuilder.append(FROM).append(tableMapper.getTableName()).append(StrUtil.SPACE);
        sqlBuilder.append(CRLF);
        return sqlBuilder;
    }

    public static String countQueryInfoReplace(String sql) {
        String queryInfo = StrUtil.sub(sql, StrUtil.indexOfIgnoreCase(sql, "SELECT ") + 6, StrUtil.indexOfIgnoreCase(sql, "FROM ")).replace(CRLF, "");
        StrBuilder replaceQueryInfo = StrUtil.strBuilder(COUNT, LEFT_BRACKETS);
        if (StrUtil.containsIgnoreCase(queryInfo, DISTINCT)) {
            replaceQueryInfo.append(queryInfo).append(RIGHT_BRACKETS);
        } else if (StrUtil.containsAnyIgnoreCase(queryInfo, SPECIAL_COUNT_QUERY)) {
            replaceQueryInfo.append(WILDCARD).append(RIGHT_BRACKETS).append(ONE_COUNT);
        } else {
            replaceQueryInfo.append(WILDCARD).append(RIGHT_BRACKETS);
        }
        return StrUtil.strBuilder(SELECT, replaceQueryInfo, CRLF) + StrUtil.sub(sql, StrUtil.indexOfIgnoreCase(sql, "FROM "), sql.length());
//        return StrUtil.replace(sql, StrUtil.sub(sql, 0, StrUtil.indexOfIgnoreCase(sql, "FROM")), StrUtil.strBuilder(SELECT, replaceQueryInfo, CRLF));
    }

    /**
     * 对更新部分SQL进行封装
     *
     * @param conditionPackages dao执行参数
     * @return 封装好更新部分SQL
     */
    public static StrBuilder updateSql(ConditionPackages conditionPackages) {
        TableMapper tableMapper = conditionPackages.getTableMapper();
        Map<String, Object> paramMap = conditionPackages.getParamMap();
        ParamIndex paramIndex = getParamIndex();
        StrBuilder sqlBuilder = StrUtil.strBuilder(UPDATE, tableMapper.getTableName(), SET).append(CRLF);

        List<String> fieldNames = tableMapper.getFieldNames();
        Map<String, String> fieldTableNames = tableMapper.getShowTableNames();
        Object updateData = conditionPackages.getUpdate();
        if (updateData != null) {
            for (String fieldName : fieldNames) {
                if (FastDaoAttributes.isAutoSetCreateTime && fieldName.equals(FastDaoAttributes.createTimeFieldName)) {
                    continue;
                }
                Object fieldValue = BeanUtil.getFieldValue(updateData, fieldName);
                if (fieldValue == null) {
                    if (conditionPackages.getUpdateSelective()) {
                        continue;
                    }
                    if (FastDaoAttributes.isOpenLogicDelete && fieldName.equals(FastDaoAttributes.deleteFieldName)) {
                        continue;
                    }
                }
                sqlBuilder.append(fieldTableNames.get(fieldName)).append(EQUAL);
                packParam(sqlBuilder, paramMap, fieldValue, paramIndex);
                sqlBuilder.append(COMMA);
            }
        }

        if (CollUtil.isNotEmpty(conditionPackages.getCustomUpdateColumns())) {
            Map<String, CustomizeUpdate.CustomizeUpdateData> customUpdateColumns = conditionPackages.getCustomUpdateColumns();
            for (String fieldName : customUpdateColumns.keySet()) {
                CustomizeUpdate.CustomizeUpdateData customizeUpdateData = customUpdateColumns.get(fieldName);
                if (CollUtil.isNotEmpty(customizeUpdateData.getData())) {
                    paramMap.putAll(customizeUpdateData.getData());
                }
                sqlBuilder.append(fieldTableNames.get(fieldName)).append(EQUAL).append(customizeUpdateData.getSql()).append(COMMA);
            }
        }

        if (updateData == null && FastDaoAttributes.isAutoSetUpdateTime && tableMapper.getAutoSetUpdateTime()) {
            if (!sqlBuilder.toString().contains(COMMA)) {
                throw new FastDaoParameterException(tableMapper.getTableName() + ": update未更新任何数据!!!");
            }
            sqlBuilder.append(QUOTATION).append(FastDaoAttributes.updateTimeTableColumnName).append(QUOTATION).append(EQUAL).append(JDBC_SQL_NEW_TIME_FUNCTION).append(COMMA);
        }

        return sqlBuilder.del(sqlBuilder.length() - 2, sqlBuilder.length()).append(CRLF);
    }


    /**
     * 对删除部分SQL进行封装
     *
     * @param conditionPackages dao执行参数
     * @return 封装好更新部分SQL
     */
    public static StrBuilder deleteSql(ConditionPackages conditionPackages) {
        return StrUtil.strBuilder(DELETE, conditionPackages.getTableMapper().getTableName()).append(CRLF);
    }

    /**
     * 对新增SQL进行封装
     *
     * @param conditionPackages dao执行参数
     * @return 封装好更新部分SQL
     */
    public static StrBuilder insertSql(ConditionPackages conditionPackages) {
        TableMapper tableMapper = conditionPackages.getTableMapper();
        List<String> fieldNames = tableMapper.getFieldNames();
        Map<String, String> fieldTableNames = tableMapper.getFieldTableNames();
        Map<String, Object> paramMap = conditionPackages.getParamMap();
        ParamIndex paramIndex = getParamIndex();
        StrBuilder fastSQL = StrUtil.strBuilder(INSERT, tableMapper.getTableName()).append(CRLF);
        List insertList = conditionPackages.getInsertList();
        if (insertList.size() == 1) {
            fastSQL.append(SET);
            Object in = insertList.get(0);
            for (String fieldName : fieldNames) {
                Object fieldValue = BeanUtil.getFieldValue(in, fieldName);
                if (fieldValue != null) {
                    fastSQL.append(fieldTableNames.get(fieldName)).append(EQUAL);
                    packParam(fastSQL, paramMap, fieldValue, paramIndex).append(COMMA);
                }
            }
            fastSQL.del(fastSQL.length() - 2, fastSQL.length());
            fastSQL.append(CRLF);
        } else {
            fastSQL.append(LEFT_BRACKETS);
            fastSQL.append(tableMapper.getShowAllTableNames());
            fastSQL.append(RIGHT_BRACKETS).append(VALUES).append(CRLF);

            for (int x = 0; x < insertList.size(); x++) {
                fastSQL.append(LEFT_BRACKETS);
                for (int i = 0; i < fieldNames.size(); i++) {
                    Object fieldValue = BeanUtil.getFieldValue(insertList.get(x), fieldNames.get(i));
                    packParam(fastSQL, paramMap, fieldValue, paramIndex);
                    if (i < fieldNames.size() - 1) {
                        fastSQL.append(COMMA);
                    }
                }
                fastSQL.append(RIGHT_BRACKETS);
                if (x < insertList.size() - 1) {
                    fastSQL.append(StrUtil.COMMA);
                }
                fastSQL.append(CRLF);
            }
        }
        return fastSQL;
    }

    public static String sqlConversion(String sql) {
        if (sql.contains(PARAM_PREFIX)) {
            sql = ReUtil.replaceAll(sql, JDBC_SQL_CONVERSION_RE_RULE, JDBC_SQL_CONVERSION_RE_RESULT);
        }
        if (sql.contains(PARAM_PREFIX_2)) {
            sql = ReUtil.replaceAll(sql, JDBC_SQL_CONVERSION_RE_RULE_2, JDBC_SQL_CONVERSION_RE_RESULT);
        }
        return sql;
    }
}

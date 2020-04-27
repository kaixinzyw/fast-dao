package com.fast.dao.utils;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.text.StrBuilder;
import cn.hutool.core.util.ReUtil;
import cn.hutool.core.util.StrUtil;
import com.fast.condition.ConditionPackages;
import com.fast.condition.FastCondition;
import com.fast.condition.FastDaoParameterException;
import com.fast.config.FastDaoAttributes;
import com.fast.fast.FastDaoParam;
import com.fast.mapper.TableMapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * SQL拼接工具类
 *
 * @author 张亚伟 https://github.com/kaixinzyw
 */
public class FastSqlUtil {

    private static final String WHERE_PARAM_TYPE = "where_param_";
    private static final String LIMIT_PARAM_TYPE = "limit_param_";
    private static final String UPDATE_PARAM_TYPE = "update_param_";
    private static final String INSERT_PARAM_TYPE = "insert_param_";
    private static final String FROM = "FROM ";
    private static final String SET = " SET ";
    private static final String AND = FastCondition.Way.AND.expression;
    private static final String OR = FastCondition.Way.OR.expression;
    private static final String WHERE = "WHERE ";
    private static final String WHERE_CRLF = "WHERE " + System.lineSeparator();
    private static final Integer WHERE_CRLF_LENGTH = ("WHERE " + System.lineSeparator()).length();
    private static final String SELECT = "SELECT ";
    private static final String UPDATE = "UPDATE ";
    private static final String DELETE = "DELETE FROM ";
    private static final String INSERT = "INSERT INTO ";
    private static final String COUNT = "COUNT";
    private static final String LIMIT = "limit ";
    private static final String DISTINCT = "DISTINCT";
    private static final String SUM = "SUM";
    private static final String AVG = "AVG";
    private static final String MIN = "MIN";
    private static final String MAX = "MAX";
    private static final String GROUP = "GROUP";
    private static final String ONE_COUNT = ">0";
    private static final String ORDER_BY = "ORDER BY ";
    private static final String DESC = " DESC ";
    private static final String ASC = " ASC ";
    private static final String CRLF = System.lineSeparator();
    private static final String COMMA = ", ";
    private static final String EQUAL = " = ";
    private static final String QUOTATION = "`";
    private static final String LEFT_BRACKETS = "(";
    private static final String RIGHT_BRACKETS = ")";
    private static final String VALUES = " VALUES ";
    private static final String WILDCARD = "*";
    private static final String PARAM_PREFIX = "#{";
    private static final String MYBATIS_PARAM_PREFIX = "#{paramMap.";
    private static final String PARAM_SUFFIX = "} ";
    private static final String JDBC_SQL_CONVERSION_RE_RULE = "[#][{](\\w*)[}]";
    private static final String JDBC_SQL_CONVERSION_RE_RESULT = ":$1";
    private static final String JDBC_SQL_NEW_TIME_FUNCTION = "NOW()";

    /**
     * 对封装SQL拼接时的参数信息
     *
     * @param paramMap   参数
     * @param value      值
     * @param paramIndex 索引
     * @return 封装后的SQL
     */
    private static StrBuilder packParam(StrBuilder sqlBuilder, Map<String, Object> paramMap, Object value, ParamIndex paramIndex) {
        String paramKey = paramIndex.getParamType() + paramIndex.get();
        paramMap.put(paramKey, value);
        paramIndex.add();
        return packJdbcParam(sqlBuilder, paramKey);
    }

    private static StrBuilder packJdbcParam(StrBuilder sqlBuilder, String paramKey) {
        sqlBuilder.append(PARAM_PREFIX).append(paramKey).append(PARAM_SUFFIX);
        return sqlBuilder;
    }


    /**
     * 参数索引
     */
    private static class ParamIndex {
        private int index = 0;
        private String paramType;

        public int get() {
            return index;
        }

        public void add() {
            this.index++;
        }

        public String getParamType() {
            return paramType;
        }

        public void setParamType(String paramType) {
            this.paramType = paramType;
        }
    }


    /**
     * 对SQL where条件进行封装
     *
     * @param sqlBuilder sql信息
     * @param param      Dao执行参数
     */
    public static void whereSql(StrBuilder sqlBuilder, FastDaoParam param) {
        ConditionPackages select = param.getFastExample().conditionPackages();
        Map paramMap = param.getParamMap();
        TableMapper tableMapper = param.getTableMapper();
        boolean isFirst = Boolean.TRUE;
        if (select.getLogicDeleteProtect()) {
            if (FastDaoAttributes.isOpenLogicDelete) {
                if (tableMapper.getLogicDelete()) {
                    sqlBuilder.append(WHERE);
                    sqlBuilder.append(!FastDaoAttributes.defaultDeleteValue ?
                            FastDaoAttributes.defaultSqlWhereDeleteValueTrue : FastDaoAttributes.defaultSqlWhereDeleteValueFalse);
                    sqlBuilder.append(CRLF);
                    isFirst = Boolean.FALSE;
                }
            }
        }
        boolean isWHERE = false;
        if (isFirst && select.getConditions().size() > 0) {
            sqlBuilder.append(WHERE);
            isWHERE = true;
        }

        ParamIndex paramIndex = new ParamIndex();
        paramIndex.setParamType(WHERE_PARAM_TYPE);
        List<FastCondition> conditions = select.getConditions();
        for (FastCondition condition : conditions) {
            if (!isFirst) {
                sqlBuilder.append(condition.getWay().expression);
            } else {
                isFirst = Boolean.FALSE;
            }
            whereCondition(condition, sqlBuilder, paramMap, tableMapper, paramIndex);
        }
        if (isWHERE && sqlBuilder.subSequence(sqlBuilder.length() - WHERE_CRLF_LENGTH,sqlBuilder.length()).equals(WHERE_CRLF)) {
            sqlBuilder.del(sqlBuilder.toString().indexOf(WHERE), sqlBuilder.length());
        }
    }

    /**
     * where条件封装器
     *
     * @param condition   条件信息
     * @param sqlBuilder  SQL信息
     * @param paramMap    参数信息
     * @param tableMapper 对象属性映射
     * @param paramIndex  参数角标
     */
    private static void whereCondition(FastCondition condition, StrBuilder sqlBuilder, Map<String, Object> paramMap,
                                       TableMapper tableMapper, ParamIndex paramIndex) {
        switch (condition.getExpression()) {
            case In:
            case NotIn:
                sqlBuilder.append(tableMapper.getShowTableNames().get(condition.getField())
                        .toString()).append(condition.getExpression().expression).append(LEFT_BRACKETS);
                for (Object value : condition.getValueList()) {
                    packParam(sqlBuilder, paramMap, value, paramIndex).append(StrUtil.COMMA);
                }
                sqlBuilder.del(sqlBuilder.length() - 1, sqlBuilder.length()).append(RIGHT_BRACKETS);
                sqlBuilder.append(CRLF);
                break;
            case Between:
            case NotBetween:
                sqlBuilder.append(tableMapper.getShowTableNames()
                        .get(condition.getField()).toString()).append(condition.getExpression().expression);
                packParam(sqlBuilder, paramMap, condition.getBetweenMin(), paramIndex).append(AND);
                packParam(sqlBuilder, paramMap, condition.getBetweenMax(), paramIndex);
                sqlBuilder.append(CRLF);
                break;
            case Null:
            case NotNull:
                sqlBuilder.append(tableMapper.getShowTableNames()
                        .get(condition.getField()).toString()).append(condition.getExpression().expression);
                sqlBuilder.append(CRLF);
                break;
            case SQL:
                if (CollUtil.isNotEmpty(condition.getParams())) {
                    paramMap.putAll(condition.getParams());
                }
                sqlBuilder.append(condition.getSql());
                sqlBuilder.append(CRLF);
                break;
            case Obj:
                Map<String, Object> fieldMap;
                if (condition.getObject() instanceof Map) {
                    fieldMap = (Map<String, Object>) condition.getObject();
                } else {
                    fieldMap = BeanUtil.beanToMap(condition.getObject(), false, true);
                }
                HashMap<String, String> showTableNames = tableMapper.getShowTableNames();
                for (String fieldName : fieldMap.keySet()) {
                    String showTable = showTableNames.get(fieldName);
                    if (showTable != null) {
                        sqlBuilder.append(showTable).append(condition.getExpression().expression);
                        packParam(sqlBuilder, paramMap, fieldMap.get(fieldName), paramIndex);
                    }
                }
                sqlBuilder.append(CRLF);
                break;
            default:
                sqlBuilder.append(tableMapper.getShowTableNames()
                        .get(condition.getField()).toString()).append(condition.getExpression().expression);
                packParam(sqlBuilder, paramMap, condition.getValue(), paramIndex);
                sqlBuilder.append(CRLF);
        }
    }

    /**
     * 查询排序
     *
     * @param sqlBuilder SQL信息
     * @param param      Dao执行条件
     */
    public static void orderBy(StrBuilder sqlBuilder, FastDaoParam param) {
        ConditionPackages conditionPackages = param.getFastExample().conditionPackages();
        TableMapper tableMapper = param.getTableMapper();
        if (conditionPackages != null && conditionPackages.getOrderByQuery() != null) {
            for (ConditionPackages.OrderByQuery orderByQuery : conditionPackages.getOrderByQuery()) {
                if (orderByQuery.getDesc()) {
                    sqlBuilder.append(ORDER_BY).append(tableMapper.getShowTableNames().get(orderByQuery.getOrderByName())).append(DESC);
                } else {
                    sqlBuilder.append(ORDER_BY).append(tableMapper.getShowTableNames().get(orderByQuery.getOrderByName())).append(ASC);
                }
            }
        }
    }

    /**
     * 分页
     *
     * @param sqlBuilder SQL信息
     * @param param      Dao执行条件
     */
    public static void limit(StrBuilder sqlBuilder, FastDaoParam param) {
        ConditionPackages conditionPackages = param.getFastExample().conditionPackages();
        if (conditionPackages != null) {
            if (conditionPackages.getPage() != null && conditionPackages.getSize() != null) {
                ParamIndex paramIndex = new ParamIndex();
                paramIndex.setParamType(LIMIT_PARAM_TYPE);
                Map<String, Object> paramMap = param.getParamMap();
                sqlBuilder.append(LIMIT);
                packParam(sqlBuilder, paramMap, conditionPackages.getPage(), paramIndex).append(COMMA);
                packParam(sqlBuilder, paramMap, conditionPackages.getSize(), paramIndex).append(CRLF);
            } else if (conditionPackages.getLimit() != null) {
                ParamIndex paramIndex = new ParamIndex();
                paramIndex.setParamType(LIMIT_PARAM_TYPE);
                Map<String, Object> paramMap = param.getParamMap();
                sqlBuilder.append(LIMIT);
                packParam(sqlBuilder, paramMap, conditionPackages.getLimit(), paramIndex).append(CRLF);
            }
        }
    }

    /**
     * 封装select需要查询的字段信息,
     *
     * @param param Dao执行条件
     * @return 封装后的SQL
     */
    public static StrBuilder selectSql(FastDaoParam param) {
        StrBuilder sqlBuilder = StrUtil.strBuilder(SELECT);
        ConditionPackages select = param.getFastExample().conditionPackages();
        TableMapper tableMapper = param.getTableMapper();
        Map<String, String> fieldTableNames = tableMapper.getShowTableNames();

        if (select == null) {
            sqlBuilder.append(tableMapper.getShowAllTableNames());
        } else if (CollUtil.isNotEmpty(select.getCustomQueryColumns())) {
            StrBuilder selectColumn = StrBuilder.create();
            for (String queryColumn : select.getCustomQueryColumns()) {
                selectColumn.append(queryColumn).append(COMMA);
            }
            sqlBuilder.append(selectColumn.subString(0, selectColumn.length() - 2));
        } else if (CollUtil.isNotEmpty(select.getSumFields()) || CollUtil.isNotEmpty(select.getAvgFields())
                || CollUtil.isNotEmpty(select.getMinFields()) || CollUtil.isNotEmpty(select.getMaxFields())) {
            if (CollUtil.isNotEmpty(select.getSumFields())) {
                for (String sumField : select.getSumFields()) {
                    sqlBuilder.append(SUM).append(LEFT_BRACKETS)
                            .append(fieldTableNames.get(sumField)).append(RIGHT_BRACKETS)
                            .append(StrUtil.SPACE).append(fieldTableNames.get(sumField)).append(COMMA);
                }
            }
            if (CollUtil.isNotEmpty(select.getAvgFields())) {
                for (String avgField : select.getAvgFields()) {
                    sqlBuilder.append(AVG).append(LEFT_BRACKETS)
                            .append(fieldTableNames.get(avgField)).append(RIGHT_BRACKETS)
                            .append(StrUtil.SPACE).append(fieldTableNames.get(avgField)).append(COMMA);
                }
            }
            if (CollUtil.isNotEmpty(select.getMinFields())) {
                for (String minField : select.getMinFields()) {
                    sqlBuilder.append(MIN).append(LEFT_BRACKETS)
                            .append(fieldTableNames.get(minField)).append(RIGHT_BRACKETS)
                            .append(StrUtil.SPACE).append(fieldTableNames.get(minField)).append(COMMA);
                }
            }
            if (CollUtil.isNotEmpty(select.getMaxFields())) {
                for (String maxField : select.getMaxFields()) {
                    sqlBuilder.append(MAX).append(LEFT_BRACKETS)
                            .append(fieldTableNames.get(maxField)).append(RIGHT_BRACKETS)
                            .append(StrUtil.SPACE).append(fieldTableNames.get(maxField)).append(COMMA);
                }
            }
            sqlBuilder.del(sqlBuilder.length() - 2, sqlBuilder.length());
        } else if (CollUtil.isNotEmpty(select.getDistinctFields())) {
            StrBuilder selectField = StrBuilder.create(DISTINCT, StrUtil.SPACE);
            for (String distinctField : select.getDistinctFields()) {
                selectField.append(fieldTableNames.get(distinctField)).append(COMMA);
            }
            sqlBuilder.append(selectField.subString(0, selectField.length() - 2));
        } else if (CollUtil.isNotEmpty(select.getShowFields())) {
            StrBuilder selectField = StrBuilder.create();
            for (String showField : select.getShowFields()) {
                selectField.append(fieldTableNames.get(showField)).append(COMMA);
            }
            sqlBuilder.append(selectField.subString(0, selectField.length() - 2));
        } else if (CollUtil.isNotEmpty(select.getHideFields())) {
            StrBuilder selectField = StrBuilder.create();
            List<String> fieldNames = tableMapper.getFieldNames();
            for (String fieldName : fieldNames) {
                if (!select.getHideFields().contains(fieldName)) {
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
        String queryInfo = StrUtil.sub(sql, StrUtil.indexOfIgnoreCase(sql, SELECT) + 7, StrUtil.indexOfIgnoreCase(sql, FROM)).replace(CRLF, "");

        StrBuilder replaceQueryInfo = StrUtil.strBuilder(COUNT, LEFT_BRACKETS);
        if (StrUtil.containsIgnoreCase(queryInfo, DISTINCT)) {
            replaceQueryInfo.append(queryInfo).append(RIGHT_BRACKETS);
        } else if (StrUtil.containsIgnoreCase(queryInfo, SUM) || StrUtil.containsIgnoreCase(queryInfo, AVG)
                || StrUtil.containsIgnoreCase(queryInfo, MIN) || StrUtil.containsIgnoreCase(queryInfo, MAX)) {
            replaceQueryInfo.append(WILDCARD).append(RIGHT_BRACKETS).append(ONE_COUNT);
        } else {
            replaceQueryInfo.append(WILDCARD).append(RIGHT_BRACKETS);
        }
        return StrUtil.replace(sql, StrUtil.sub(sql, 0, StrUtil.indexOfIgnoreCase(sql, FROM)), StrUtil.strBuilder(SELECT, replaceQueryInfo, CRLF));
    }

    /**
     * 对更新部分SQL进行封装
     *
     * @param param dao执行参数
     * @return 封装好更新部分SQL
     */
    public static StrBuilder updateSql(FastDaoParam param) {
        TableMapper tableMapper = param.getTableMapper();
        Map<String, Object> paramMap = param.getParamMap();
        ParamIndex paramIndex = new ParamIndex();
        paramIndex.setParamType(UPDATE_PARAM_TYPE);
        StrBuilder sqlBuilder = StrUtil.strBuilder(UPDATE, tableMapper.getTableName(), SET).append(CRLF);

        List<String> fieldNames = tableMapper.getFieldNames();
        Map<String, String> fieldTableNames = tableMapper.getShowTableNames();
        Object updateData = param.getUpdate();
        if (updateData != null) {
            for (int i = 0; i < fieldNames.size(); i++) {
                String fieldName = fieldNames.get(i);
                if (FastDaoAttributes.isAutoSetCreateTime && fieldName.equals(FastDaoAttributes.createTimeFieldName)) {
                    continue;
                }
                Object fieldValue = BeanUtil.getFieldValue(updateData, fieldName);
                if (fieldValue == null) {
                    if (param.getUpdateSelective()) {
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

        if (param.getFastExample() != null && CollUtil.isNotEmpty(param.getFastExample().conditionPackages().getCustomUpdateColumns())) {
            Map<String, String> customUpdateColumns = param.getFastExample().conditionPackages().getCustomUpdateColumns();
            for (String fieldName : customUpdateColumns.keySet()) {
                sqlBuilder.append(fieldTableNames.get(fieldName)).append(EQUAL).append(customUpdateColumns.get(fieldName)).append(COMMA);
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
     * @param param dao执行参数
     * @return 封装好更新部分SQL
     */
    public static StrBuilder deleteSql(FastDaoParam param) {
        return StrUtil.strBuilder(DELETE, param.getTableMapper().getTableName()).append(CRLF);
    }

    /**
     * 对新增SQL进行封装
     *
     * @param param dao执行参数
     * @return 封装好更新部分SQL
     */
    public static StrBuilder insertSql(FastDaoParam param) {
        TableMapper tableMapper = param.getTableMapper();
        List<String> fieldNames = tableMapper.getFieldNames();
        Map<String, String> fieldTableNames = tableMapper.getFieldTableNames();
        Map<String, Object> paramMap = param.getParamMap();
        ParamIndex paramIndex = new ParamIndex();
        paramIndex.setParamType(INSERT_PARAM_TYPE);
        StrBuilder fastSQL = StrUtil.strBuilder(INSERT, tableMapper.getTableName()).append(CRLF);
        List insertList = param.getInsertList();
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
        return ReUtil.replaceAll(sql, JDBC_SQL_CONVERSION_RE_RULE, JDBC_SQL_CONVERSION_RE_RESULT);
    }


}

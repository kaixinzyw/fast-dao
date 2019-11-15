package com.fast.dao.utils;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.text.StrBuilder;
import cn.hutool.core.util.StrUtil;
import com.fast.condition.ConditionPackages;
import com.fast.condition.FastCondition;
import com.fast.config.FastDaoAttributes;
import com.fast.dao.jdbc.JdbcImpl;
import com.fast.dao.mybatis.FastMyBatisImpl;
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

    private static final String WHERE_PARAM = "where_param_";
    private static final String UPDATE_PARAM = "update_param_";
    private static final String INSERT_PARAM = "insert_param_";
    private static final String FROM = "FROM ";
    private static final String SET = " SET ";
    private static final String AND = FastCondition.Way.AND.expression;
    private static final String OR = FastCondition.Way.OR.expression;
    private static final String WHERE = "WHERE ";
    private static final String SELECT = "SELECT ";
    private static final String UPDATE = "UPDATE ";
    private static final String DELETE = "DELETE FROM ";
    private static final String INSERT = "INSERT INTO ";
    private static final String COUNT = "COUNT";
    private static final String DISTINCT = "DISTINCT";
    private static final String ORDER_BY = "ORDER BY ";
    private static final String DESC = " DESC ";
    private static final String ASC = " ASC ";
    private static final String COMMA = ", ";
    private static final String EQUAL = " = ";
    private static final String QUOTATION = "`";
    private static final String LEFT_BRACKETS = "(";
    private static final String RIGHT_BRACKETS = ")";
    private static final String VALUES = " VALUES ";
    private static final String WILDCARD = "*";


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
        sqlBuilder.append(System.lineSeparator());
        sqlBuilder.append(FROM).append(tableMapper.getTableName()).append(StrUtil.SPACE);
        sqlBuilder.append(System.lineSeparator());
        return sqlBuilder;
    }

    public static String countQueryInfoReplace(String sql) {
        String queryInfo = StrUtil.sub(sql, StrUtil.indexOfIgnoreCase(sql, SELECT) + 7, StrUtil.indexOfIgnoreCase(sql, FROM)).replace(System.lineSeparator(), "");
        StrBuilder replaceQueryInfo = StrUtil.strBuilder(COUNT, LEFT_BRACKETS);
        if (StrUtil.containsIgnoreCase(queryInfo, DISTINCT)) {
            replaceQueryInfo.append(queryInfo);
        } else {
            replaceQueryInfo.append(WILDCARD);
        }
        replaceQueryInfo.append(RIGHT_BRACKETS);
        return StrUtil.replace(sql, queryInfo, replaceQueryInfo);
    }


    /**
     * 对封装SQL拼接时的参数信息
     *
     * @param paramMap   参数
     * @param value      值
     * @param paramIndex 索引
     * @return 封装后的SQL
     */
    private static StrBuilder pageParam(StrBuilder valsBuilder, Map<String, Object> paramMap, Object value, String paramType, ParamIndex paramIndex) {
        String paramKey = paramType + paramIndex.get();
        paramMap.put(paramKey, value);
        paramIndex.add();
        valsBuilder.append(StrUtil.COLON);
        valsBuilder.append(paramKey);
        valsBuilder.append(StrUtil.SPACE);
        return valsBuilder;
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
     * @param sqlBuilder sql信息
     * @param param      Dao执行参数
     */
    public static void whereSql(StrBuilder sqlBuilder, FastDaoParam param) {
        ConditionPackages select = param.getFastExample().conditionPackages();
        Map paramMap = param.getParamMap();
        TableMapper tableMapper = param.getTableMapper();
        sqlBuilder.append(WHERE);
        if (select == null) {
            if (FastDaoAttributes.isOpenLogicDelete) {
                sqlBuilder.append(!FastDaoAttributes.defaultDeleteValue ?
                        FastDaoAttributes.defaultSqlWhereDeleteValueTrue : FastDaoAttributes.defaultSqlWhereDeleteValueFalse);
            }
            return;
        }

        boolean isFirst = Boolean.TRUE;

        if (select.getLogicDeleteProtect()) {
            if (FastDaoAttributes.isOpenLogicDelete) {
                sqlBuilder.append(!FastDaoAttributes.defaultDeleteValue ?
                        FastDaoAttributes.defaultSqlWhereDeleteValueTrue : FastDaoAttributes.defaultSqlWhereDeleteValueFalse);
                sqlBuilder.append(System.lineSeparator());
                isFirst = Boolean.FALSE;
            }
        }

        ParamIndex paramIndex = new ParamIndex();
        List<FastCondition> conditions = select.getConditions();
        for (FastCondition condition : conditions) {
            if (!isFirst) {
                sqlBuilder.append(condition.getWay().expression);
            } else {
                isFirst = Boolean.FALSE;
            }
            whereCondition(condition, sqlBuilder, paramMap, tableMapper, paramIndex);
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
                    pageParam(sqlBuilder, paramMap, value, WHERE_PARAM, paramIndex).append(StrUtil.COMMA);
                }
                sqlBuilder.del(sqlBuilder.length() - 1, sqlBuilder.length()).append(RIGHT_BRACKETS);
                sqlBuilder.append(System.lineSeparator());
                break;
            case Between:
            case NotBetween:
                sqlBuilder.append(tableMapper.getShowTableNames()
                        .get(condition.getField()).toString()).append(condition.getExpression().expression);
                pageParam(sqlBuilder, paramMap, condition.getBetweenMin(), WHERE_PARAM, paramIndex).append(sqlBuilder.append(AND));
                pageParam(sqlBuilder, paramMap, condition.getBetweenMax(), WHERE_PARAM, paramIndex);
                sqlBuilder.append(System.lineSeparator());
                break;
            case Null:
            case NotNull:
                sqlBuilder.append(tableMapper.getShowTableNames()
                        .get(condition.getField()).toString()).append(condition.getExpression().expression);
                sqlBuilder.append(System.lineSeparator());
                break;
            case SQL:
                if (CollUtil.isNotEmpty(condition.getParams())) {
                    paramMap.putAll(condition.getParams());
                }
                sqlBuilder.append(condition.getSql());
                sqlBuilder.append(System.lineSeparator());
                break;
            case Obj:
                Map<String, Object> fieldMap;
                if (condition.getObject() instanceof Map) {
                    fieldMap = (Map<String, Object>) condition.getObject();
                } else {
                    fieldMap = BeanUtil.beanToMap(condition.getObject(), false, true);
                }
                HashMap<String,String> showTableNames = tableMapper.getShowTableNames();
                for (String fieldName : fieldMap.keySet()) {
                    String showTable = showTableNames.get(fieldName);
                    if (showTable != null) {
                        sqlBuilder.append(showTable).append(condition.getExpression().expression);
                        pageParam(sqlBuilder, paramMap, fieldMap.get(fieldName), WHERE_PARAM, paramIndex);
                    }
                }
                sqlBuilder.append(System.lineSeparator());
                break;
            default:
                sqlBuilder.append(tableMapper.getShowTableNames()
                        .get(condition.getField()).toString()).append(condition.getExpression().expression);
                pageParam(sqlBuilder, paramMap, condition.getValue(), WHERE_PARAM, paramIndex);
                sqlBuilder.append(System.lineSeparator());
        }
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
        StrBuilder sqlBuilder = StrUtil.strBuilder(UPDATE, tableMapper.getTableName(), SET).append(System.lineSeparator());

        List<String> fieldNames = tableMapper.getFieldNames();
        Map<String, String> fieldTableNames = tableMapper.getShowTableNames();
        for (int i = 0; i < fieldNames.size(); i++) {
            String fieldName = fieldNames.get(i);
            if (FastDaoAttributes.isAutoSetCreateTime && fieldName.equals(FastDaoAttributes.createTimeFieldName)) {
                continue;
            }
            Object fieldValue = BeanUtil.getFieldValue(param.getUpdate(), fieldName);
            if (fieldValue == null) {
                if (param.getUpdateSelective()) {
                    continue;
                }
                if (FastDaoAttributes.isOpenLogicDelete && fieldName.equals(FastDaoAttributes.deleteFieldName)) {
                    continue;
                }
            }
            sqlBuilder.append(fieldTableNames.get(fieldName)).append(EQUAL);
            pageParam(sqlBuilder, paramMap, fieldValue, UPDATE_PARAM, paramIndex);
            sqlBuilder.append(COMMA);
        }
        return sqlBuilder.del(sqlBuilder.length() - 2, sqlBuilder.length()).append(System.lineSeparator());
    }


    /**
     * 对删除部分SQL进行封装
     *
     * @param param dao执行参数
     * @return 封装好更新部分SQL
     */
    public static StrBuilder deleteSql(FastDaoParam param) {
        return StrUtil.strBuilder(DELETE, param.getTableMapper().getTableName()).append(System.lineSeparator());
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

        StrBuilder fastSQL = StrUtil.strBuilder(INSERT, tableMapper.getTableName()).append(System.lineSeparator()).append(LEFT_BRACKETS);

        for (int i = 0; i < fieldNames.size(); i++) {
            fastSQL.append(QUOTATION).append(fieldTableNames.get(fieldNames.get(i))).append(QUOTATION);
            if (i < fieldNames.size() - 1) {
                fastSQL.append(COMMA);
            }
        }
        fastSQL.append(RIGHT_BRACKETS).append(VALUES).append(System.lineSeparator());
        List insertList = param.getInsertList();
        for (int x = 0; x < insertList.size(); x++) {
            fastSQL.append(LEFT_BRACKETS);
            for (int i = 0; i < fieldNames.size(); i++) {
                Object fieldValue = BeanUtil.getFieldValue(insertList.get(x), fieldNames.get(i));
                pageParam(fastSQL, paramMap, fieldValue, INSERT_PARAM, paramIndex);
                if (i < fieldNames.size() - 1) {
                    fastSQL.append(COMMA);
                }
            }
            fastSQL.append(RIGHT_BRACKETS);
            if (x < insertList.size() - 1) {
                fastSQL.append(StrUtil.COMMA);
            }
            fastSQL.append(System.lineSeparator());
        }
        return fastSQL;

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
            Map<String, Object> paramMap = param.getParamMap();
            if (conditionPackages.getPage() != null && conditionPackages.getSize() != null) {
                sqlBuilder.append("limit :page , :size ").append(System.lineSeparator());
                paramMap.put("page", conditionPackages.getPage());
                paramMap.put("size", conditionPackages.getSize());
            } else if (conditionPackages.getLimit() != null) {
                sqlBuilder.append("limit :limit ").append(System.lineSeparator());
                paramMap.put("limit", conditionPackages.getLimit());
            }
        }
    }

    public static String conversionMyBatisSql(String sql) {
        return sql.replaceAll("[:](\\w*)[\\s]", "#{paramMap." + "$1" + "}");
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

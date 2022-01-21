package com.fast.fast;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.text.StrBuilder;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import com.fast.base.BaseFastDAO;
import com.fast.condition.ConditionPackages;
import com.fast.condition.FastExample;
import com.fast.condition.OrderByQuery;
import com.fast.dao.jdbc.FastSqlUtil;
import com.fast.dao.many.FastJoinQueryInfo;
import com.fast.utils.BeanCopyUtil;
import com.fast.utils.SqlTemplateUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 多表查询
 *
 * @author 张亚伟
 */
public class JoinFastDao<T> {
    private ConditionPackages main;
    private ConditionPackages<T> resultCondition;
    private final List<Join> joinList = new ArrayList<>();
    private final StrBuilder sql = new StrBuilder();
    private final Map<String, Object> params = new HashMap<>();
    private final List<OrderByQuery> orderByList = new ArrayList<>();
    private static final String EQUAL = " = ";
    private static final String FROM = "FROM ";
    private static final String SELECT = "SELECT ";
    private static final String ON = " ON ";
    private static final String AND = "AND ";
    private static final String WHERE = "WHERE ";
    private static final String AS = " AS ";
    private static final String CITE = "`";
    private static final String FAST_EXAMPLE = "fastExample";
    private static final String ORDER_BY = "ORDER BY ";
    private static final String ASC = "ASC";
    private static final String DESC = "DESC";
    private static final String CRLF = System.lineSeparator();


    public static <T> JoinFastDao<T> create(Class<T> resultClass, BaseFastDAO main) {
        JoinFastDao<T> joinFastDao = new JoinFastDao<>();
        joinFastDao.main = main.fastExample().conditionPackages();
        joinFastDao.resultCondition = new ConditionPackages<>(resultClass);
        joinFastDao.resultCondition.setTableAlias(joinFastDao.resultCondition.getTableMapper().getTableAlias());
        joinFastDao.main.setTableAlias(joinFastDao.resultCondition.getTableAlias());
        List<OrderByQuery> orderByQuery = joinFastDao.main.getOrderByQuery();
        if (CollUtil.isNotEmpty(orderByQuery)) {
            for (OrderByQuery byQuery : orderByQuery) {
                byQuery.setOrderByName(StrUtil.strBuilder(joinFastDao.main.getTableAlias(), StrUtil.DOT,
                        CITE, joinFastDao.main.getTableMapper().getFieldTableNames().get(byQuery.getOrderByName()), CITE).toString());
            }
            joinFastDao.orderByList.addAll(orderByQuery);
        }
        return joinFastDao;
    }

    public FastCustomSqlDao<T> dao() {
        StrBuilder joinTableSql = StrBuilder.create();
        StrBuilder joinConditionSql = FastSqlUtil.whereSql(main, params, Boolean.FALSE, Boolean.TRUE);
        StrBuilder showAllTableNames = StrBuilder.create(resultCondition.getTableMapper().getShowPrefixAllTableNames());
        if (CollUtil.isNotEmpty(joinList)) {
            for (Join join : joinList) {
                joinTableSql.append(System.lineSeparator());
                joinTableSql.append(join.joinSql);
                if (join.conditionSql.length() > 0) {
                    if (joinConditionSql.length() > 0) {
                        joinConditionSql.append(CRLF);
                        joinConditionSql.append(AND);
                    }
                    joinConditionSql.append(join.conditionSql);
                }
                params.putAll(join.paramsMap);
                if (join.queryColumnNames != null) {
                    showAllTableNames.append(StrUtil.COMMA).append(CRLF).append(join.queryColumnNames);
                }
            }
        }
        sql.append(SELECT).append(showAllTableNames)
                .append(System.lineSeparator()).append(FROM).append(main.getTableMapper().getTableName());
        sql.append(AS).append(resultCondition.getTableMapper().getTableAlias());
        sql.append(joinTableSql);
        if (joinConditionSql.length() > 0) {
            sql.append(System.lineSeparator()).append(WHERE).append(joinConditionSql).append(CRLF);
        }
        if (orderByList.size() > 0) {
            sql.append(ORDER_BY);
            for (int i = 0; i < orderByList.size(); i++) {
                OrderByQuery orderByQuery = orderByList.get(i);
                sql.append(orderByQuery.getOrderByName()).append(StrUtil.SPACE);
                if (orderByQuery.getDesc()) {
                    sql.append(DESC);
                } else {
                    sql.append(ASC);
                }
                if (i < orderByList.size() - 1) {
                    sql.append(StrUtil.COMMA);
                }
            }
        }
        return FastCustomSqlDao.create(resultCondition, sql.toString(), params);
    }

    public <P, F> Join<T, P, F> leftJoin(BaseFastDAO<P, F> joinCondition, String... tableAlias) {
        Join<T, P, F> join = new Join<>(this, JoinDirection.左连接, joinCondition.fastExample().conditionPackages(), tableAlias);
        return join;
    }

    public <P, F> Join<T, P, F> leftJoin(BaseFastDAO<P, F> joinCondition, FastExample.FieldCriteria<P, F> leftCondition, FastExample.FieldCriteria rightCondition, String... tableAlias) {
        Join<T, P, F> join = new Join<>(this, JoinDirection.左连接, joinCondition.fastExample().conditionPackages(), tableAlias);
        join.on(leftCondition, rightCondition);
        return join;
    }

    public <P, F> Join<T, P, F> leftJoin(String sql, String... tableAlias) {
        Join<T, P, F> join = new Join<>(this, JoinDirection.左连接, sql, tableAlias);
        return join;
    }

    public <P, F> Join<T, P, F> rightJoin(BaseFastDAO<P, F> joinCondition, String... tableAlias) {
        Join<T, P, F> join = new Join<>(this, JoinDirection.右连接, joinCondition.fastExample().conditionPackages(), tableAlias);
        return join;
    }

    public <P, F> Join<T, P, F> rightJoin(BaseFastDAO<P, F> joinCondition, FastExample.FieldCriteria<P, F> leftCondition, FastExample.FieldCriteria rightCondition, String... tableAlias) {
        Join<T, P, F> join = new Join<>(this, JoinDirection.右连接, joinCondition.fastExample().conditionPackages(), tableAlias);
        join.on(leftCondition, rightCondition);
        return join;
    }

    public <P, F> Join<T, P, F> innerJoin(BaseFastDAO<P, F> joinCondition, String tableAlias) {
        Join<T, P, F> join = new Join<>(this, JoinDirection.内连接, joinCondition.fastExample().conditionPackages(), new String[]{tableAlias});
        return join;
    }

    public <P, F> Join<T, P, F> innerJoin(BaseFastDAO<P, F> joinCondition, FastExample.FieldCriteria<P, F> leftCondition, FastExample.FieldCriteria rightCondition, String tableAlias) {
        Join<T, P, F> join = new Join<>(this, JoinDirection.内连接, joinCondition.fastExample().conditionPackages(), new String[]{tableAlias});
        join.on(leftCondition, rightCondition);
        return join;
    }

    public static class Join<T, P, F> {
        private final StrBuilder joinSql = StrBuilder.create();
        private final Map<String, Object> paramsMap = new HashMap<>();
        private final StrBuilder conditionSql = StrBuilder.create();
        private String sql;
        private String queryColumnNames;
        private final JoinFastDao<T> joinFastDao;
        private ConditionPackages<T> joinCondition;
        private final String joinTableAlias;
        private final JoinDirection direction;
        private Boolean isOn = Boolean.FALSE;
        private static final Map<String, String> JOIN_SHOW_PREFIX_ALL_MAP = new HashMap<>();

        public Join(JoinFastDao<T> joinFastDao, JoinDirection direction, String sql, String[] tableAlias) {
            this.direction = direction;
            this.joinFastDao = joinFastDao;
            this.joinTableAlias = tableAlias[0];
            this.sql = sql;
        }
        public Join(JoinFastDao<T> joinFastDao, JoinDirection direction, ConditionPackages joinCondition, String[] tableAlias) {
            this.direction = direction;
            this.joinCondition = joinCondition;
            this.joinFastDao = joinFastDao;
            if (ArrayUtil.isNotEmpty(tableAlias)) {
                this.joinTableAlias = tableAlias[0];
            } else {
                this.joinTableAlias = joinCondition.getTableMapper().getTableAlias();
            }
            this.joinCondition.setTableAlias(joinTableAlias);
        }

        public Join<T, P, F> on(String leftCondition) {
            joinSql.append(direction.direction).append(FastSqlUtil.LEFT_BRACKETS).append(sql).append(FastSqlUtil.RIGHT_BRACKETS)
                    .append(AS).append(joinTableAlias).append(ON).append(leftCondition);

            return null;
        }
        public Join<T, P, F> on(FastExample.FieldCriteria<P, F> leftCondition, FastExample.FieldCriteria rightCondition) {
            FastExample leftFastExample = BeanUtil.getProperty(leftCondition, FAST_EXAMPLE);
            FastExample rightFastExample = BeanUtil.getProperty(rightCondition, FAST_EXAMPLE);
            String rightTableAlias = rightFastExample.conditionPackages().getTableAlias() != null ?
                    rightFastExample.conditionPackages().getTableAlias() :
                    rightFastExample.conditionPackages().getTableMapper().getTableAlias();
            if (!isOn) {
                joinSql.append(direction.direction).append(joinCondition.getTableMapper().getTableName()).append(AS).append(joinTableAlias).append(ON);
                FastJoinQueryInfo fastJoinQueryInfo = joinFastDao.resultCondition.getTableMapper().getFastJoinQueryInfoMap().get(joinTableAlias);
                if (fastJoinQueryInfo != null) {
                    String joinKey = StrBuilder.create(joinFastDao.resultCondition.getTableAlias(), StrUtil.UNDERLINE,
                            joinTableAlias).toString();
                    queryColumnNames = JOIN_SHOW_PREFIX_ALL_MAP.get(joinKey);
                    if (queryColumnNames == null) {
                        StrBuilder showPrefixAll = StrUtil.strBuilder();
                        for (String tableFieldName : joinCondition.getTableMapper().getTableFieldNames().keySet()) {
                            showPrefixAll.append(StrUtil.strBuilder(joinTableAlias, StrUtil.DOT, CITE, tableFieldName, CITE, StrUtil.COMMA));
                        }
                        queryColumnNames = showPrefixAll.del(showPrefixAll.length() - 1, showPrefixAll.length()).toString();
                        JOIN_SHOW_PREFIX_ALL_MAP.put(joinKey, queryColumnNames);
                    }
                    FastJoinQueryInfo joinQueryInfo = BeanCopyUtil.copy(fastJoinQueryInfo, FastJoinQueryInfo.class);
                    if (StrUtil.isEmpty(joinQueryInfo.getThisTableAlias())) {
                        joinQueryInfo.setThisTableAlias(rightTableAlias);
                    }
                    if (StrUtil.isEmpty(joinQueryInfo.getThisColumnName())) {
                        joinQueryInfo.setThisColumnName(rightFastExample.conditionPackages().getTableMapper().getFieldTableNames().get(rightFastExample.getFieldName()));
                    }
                    if (StrUtil.isEmpty(joinQueryInfo.getJoinTableAlias())) {
                        joinQueryInfo.setJoinTableAlias(joinTableAlias);
                    }
                    if (StrUtil.isEmpty(joinQueryInfo.getJoinColumnName())) {
                        joinQueryInfo.setJoinColumnName(leftFastExample.conditionPackages().getTableMapper().getFieldTableNames().get(leftFastExample.getFieldName()));
                    }
                    joinFastDao.resultCondition.addFastJoinQueryInfo(joinQueryInfo);
                }

                List<OrderByQuery> orderByQuery = joinCondition.getOrderByQuery();
                if (CollUtil.isNotEmpty(orderByQuery)) {
                    for (OrderByQuery byQuery : orderByQuery) {
                        byQuery.setOrderByName(StrUtil.strBuilder(joinTableAlias, StrUtil.DOT,
                                CITE, joinCondition.getTableMapper().getFieldTableNames().get(byQuery.getOrderByName()), CITE).toString());
                    }
                    joinFastDao.orderByList.addAll(orderByQuery);
                }
                StrBuilder whereSql = FastSqlUtil.whereSql(joinCondition, paramsMap, Boolean.FALSE, Boolean.TRUE);
                conditionSql.append(whereSql);
                joinFastDao.joinList.add(this);
                this.isOn = true;
            } else {
                joinSql.append(StrUtil.SPACE).append(AND);
            }
            joinSql.append(StrUtil.strBuilder(joinTableAlias, StrUtil.DOT, CITE, leftFastExample.conditionPackages().getTableMapper().getFieldTableNames().get(leftFastExample.getFieldName()), CITE,
                    EQUAL, rightTableAlias, StrUtil.DOT, CITE, rightFastExample.conditionPackages().getTableMapper().getFieldTableNames().get(rightFastExample.getFieldName()), CITE
            ));
            return this;
        }

        public Join<T, P, F> and(FastExample.FieldCriteria<P, F> leftCondition, FastExample.FieldCriteria rightCondition) {
            return on(leftCondition, rightCondition);
        }

        public Join<T, P, F> and(String customSql, Object params) {
            if (StrUtil.isNotBlank(customSql)) {
                joinSql.append(StrUtil.SPACE).append(AND).append(customSql);
                paramsMap.putAll(SqlTemplateUtil.getMap(params));
            }
            return this;
        }

        public FastCustomSqlDao<T> dao() {
            return joinFastDao.dao();
        }

        public <P, F> Join<T, P, F> leftJoin(BaseFastDAO<P, F> joinCondition, String... tableAlias) {
            return joinFastDao.leftJoin(joinCondition, tableAlias);
        }

        public <P, F> Join<T, P, F> leftJoin(BaseFastDAO<P, F> joinCondition, FastExample.FieldCriteria<P, F> leftCondition, FastExample.FieldCriteria rightCondition, String... tableAlias) {
            return joinFastDao.leftJoin(joinCondition, leftCondition, rightCondition, tableAlias);
        }

        public <P, F> Join<T, P, F> rightJoin(BaseFastDAO<P, F> joinCondition, String... tableAlias) {
            return joinFastDao.rightJoin(joinCondition, tableAlias);
        }

        public <P, F> Join<T, P, F> rightJoin(BaseFastDAO<P, F> joinCondition, FastExample.FieldCriteria<P, F> leftCondition, FastExample.FieldCriteria rightCondition, String... tableAlias) {
            return joinFastDao.rightJoin(joinCondition, leftCondition, rightCondition, tableAlias);
        }

        public <P, F> Join<T, P, F> innerJoin(BaseFastDAO<P, F> joinCondition, String tableAlias) {
            return joinFastDao.innerJoin(joinCondition, tableAlias);
        }

        public <P, F> Join<T, P, F> innerJoin(BaseFastDAO<P, F> joinCondition, FastExample.FieldCriteria<P, F> leftCondition, FastExample.FieldCriteria rightCondition, String tableAlias) {
            return joinFastDao.innerJoin(joinCondition, leftCondition, rightCondition, tableAlias);
        }
    }

    private enum JoinDirection {
        /**
         * 连接方向
         */
        左连接("LEFT JOIN "), 右连接("RIGHT JOIN "), 内连接("INNER JOIN ");
        public String direction;

        JoinDirection(String direction) {
            this.direction = direction;
        }
    }
}

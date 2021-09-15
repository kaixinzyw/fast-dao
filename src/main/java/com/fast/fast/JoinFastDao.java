package com.fast.fast;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.text.StrBuilder;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.StrUtil;
import com.fast.base.BaseFastDAO;
import com.fast.condition.ConditionPackages;
import com.fast.condition.FastExample;
import com.fast.dao.many.FastJoinQueryInfo;
import com.fast.dao.utils.FastSqlUtil;
import com.fast.mapper.TableMapper;
import com.fast.mapper.TableMapperUtil;
import com.fast.utils.SqlTemplateUtil;

import javax.persistence.Table;
import javax.persistence.criteria.Join;
import java.util.*;

public class JoinFastDao<T> {
    private Class<T> resultClass;
    private ConditionPackages main;
    private TableMapper resultMapper;
    private final List<Join> joinList = new ArrayList<>();
    private final StrBuilder sql = new StrBuilder();
    private final Map<String, Object> params = new HashMap<>();
    private static final String EQUAL = " = ";
    private static final String FROM = "FROM ";
    private static final String SELECT = "SELECT ";
    private static final String ON = " ON ";
    private static final String AND = "AND ";
    private static final String WHERE = "WHERE ";
    private static final String AS = " AS ";
    private static final String CITE = "`";
    private static final String FAST_EXAMPLE = "fastExample";

    public static <T> JoinFastDao<T> create(Class<T> resultClass, BaseFastDAO main) {
        JoinFastDao<T> joinFastDao = new JoinFastDao<>();
        joinFastDao.main = main.fastExample().conditionPackages();
        joinFastDao.resultClass = resultClass;
        joinFastDao.resultMapper = TableMapperUtil.getTableMappers(resultClass);
        main.fastExample().conditionPackages().setTableAlias(joinFastDao.resultMapper.getTableAlias());
        return joinFastDao;
    }

    public FastCustomSqlDao<T> dao() {
        StrBuilder joinConditionSql = StrBuilder.create();
        StrBuilder joinTableSql = StrBuilder.create();
        FastSqlUtil.whereSql(joinConditionSql, main, params, Boolean.FALSE, Boolean.TRUE);
        StrBuilder showAllTableNames = StrBuilder.create(resultMapper.getShowPrefixAllTableNames());
        if (CollUtil.isNotEmpty(joinList)) {
            for (Join join : joinList) {
                joinTableSql.append(System.lineSeparator());
                joinTableSql.append(join.joinSql);
                if (join.conditionSql.length() > 0) {
                    if (joinConditionSql.length() > 0) {
                        joinConditionSql.append(AND);
                    }
                    joinConditionSql.append(join.conditionSql);
                }
                params.putAll(join.paramsMap);
                if (join.query != null) {
                    showAllTableNames.append(StrUtil.COMMA).append(join.query);
                }
            }
        }
        sql.append(SELECT).append(showAllTableNames)
                .append(System.lineSeparator()).append(FROM).append(main.getTableMapper().getTableName());
        if (StrUtil.isNotBlank(main.getTableAlias())) {
            sql.append(AS).append(main.getTableAlias());
        }
        sql.append(joinTableSql);
        if (joinConditionSql.length() > 0) {
            sql.append(System.lineSeparator()).append(WHERE).append(joinConditionSql);
        }
        return FastCustomSqlDao.create(resultClass, sql.toString(), params);
    }

    public <P, F> Join<T, P, F> leftJoinNotQuery(BaseFastDAO<P, F> joinCondition, String... tableAlias) {
        Join<T, P, F> join = new Join<>(this, JoinDirection.左连接, joinCondition.fastExample().conditionPackages(), Boolean.FALSE, tableAlias);
        joinList.add(join);
        return join;
    }

    public <P, F> Join<T, P, F> leftJoin(BaseFastDAO<P, F> joinCondition, String... tableAlias) {
        Join<T, P, F> join = new Join<>(this, JoinDirection.左连接, joinCondition.fastExample().conditionPackages(), Boolean.TRUE, tableAlias);
        joinList.add(join);
        return join;
    }

    public JoinFastDao<T> leftJoin(String customizeJoinCondition) {
        Join join = new Join(JoinDirection.左连接, customizeJoinCondition);
        joinList.add(join);
        return this;
    }

    public <P, F> Join<T, P, F> rightJoinNotQuery(BaseFastDAO<P, F> joinCondition, String... tableAlias) {
        Join<T, P, F> join = new Join<>(this, JoinDirection.右连接, joinCondition.fastExample().conditionPackages(), Boolean.FALSE, tableAlias);
        joinList.add(join);
        return join;
    }

    public <P, F> Join<T, P, F> rightJoin(BaseFastDAO<P, F> joinCondition, String... tableAlias) {
        Join<T, P, F> join = new Join<>(this, JoinDirection.右连接, joinCondition.fastExample().conditionPackages(), Boolean.TRUE, tableAlias);
        joinList.add(join);
        return join;
    }

    public JoinFastDao<T> rightJoin(String customizeJoinCondition) {
        Join join = new Join(JoinDirection.右连接, customizeJoinCondition);
        joinList.add(join);
        return this;
    }

    public <P, F> Join<T, P, F> innerJoinNotQuery(BaseFastDAO<P, F> joinCondition, String tableAlias) {
        Join<T, P, F> join = new Join<>(this, JoinDirection.内连接, joinCondition.fastExample().conditionPackages(), Boolean.FALSE, new String[]{tableAlias});
        joinList.add(join);
        return join;
    }

    public <P, F> Join<T, P, F> innerJoin(BaseFastDAO<P, F> joinCondition, String tableAlias) {
        Join<T, P, F> join = new Join<>(this, JoinDirection.内连接, joinCondition.fastExample().conditionPackages(), Boolean.TRUE, new String[]{tableAlias});
        joinList.add(join);
        return join;
    }

    public JoinFastDao<T> innerJoin(String customizeJoinCondition) {
        Join join = new Join(JoinDirection.内连接, customizeJoinCondition);
        joinList.add(join);
        return this;
    }


    public static class Join<T, P, F> {
        private final StrBuilder joinSql = StrBuilder.create();
        private final Map<String, Object> paramsMap = new HashMap<>();
        private final StrBuilder conditionSql = StrBuilder.create();
        private String query;
        private JoinFastDao<T> joinFastDao;
        private static Map<String, String> joinShowPrefixAllMap = new HashMap<>();

        public Join(JoinFastDao<T> joinFastDao, JoinDirection direction, ConditionPackages joinCondition, Boolean isJoinQuery, String[] tableAlias) {
            joinSql.append(direction.direction).append(joinCondition.getTableMapper().getTableName());
            String thisTableAlias = null;
            if (ArrayUtil.isNotEmpty(tableAlias)) {
                joinCondition.setTableAlias(tableAlias[0]);
                joinSql.append(AS).append(tableAlias[0]);
                thisTableAlias = tableAlias[0];
            } else {
                List<FastJoinQueryInfo> fastJoinQueryInfoList = joinFastDao.resultMapper
                        .getFastJoinQueryInfoMap().get(joinCondition.getTableMapper().getObjClass());
                if (fastJoinQueryInfoList != null && fastJoinQueryInfoList.size() == 1) {
                    if (StrUtil.isNotBlank(fastJoinQueryInfoList.get(0).getJoinTableAlias())) {
                        joinSql.append(AS).append(fastJoinQueryInfoList.get(0).getJoinTableAlias());
                        joinCondition.setTableAlias(fastJoinQueryInfoList.get(0).getJoinTableAlias());
                        thisTableAlias = fastJoinQueryInfoList.get(0).getJoinTableAlias();
                    }
                } else {
                    joinSql.append(AS).append(joinCondition.getTableMapper().getTableAlias());
                    joinCondition.setTableAlias(joinCondition.getTableMapper().getTableAlias());
                    thisTableAlias = joinCondition.getTableMapper().getTableAlias();
                }
            }
            if (BooleanUtil.isTrue(isJoinQuery)) {
                String joinKey = StrBuilder.create(joinCondition.getTableMapper().getTableAlias(), thisTableAlias).toString();
                query = joinShowPrefixAllMap.get(joinKey);
                if (query == null) {
                    StrBuilder showPrefixAll = StrUtil.strBuilder();
                    for (String tableFieldName : joinCondition.getTableMapper().getTableFieldNames().keySet()) {
                        showPrefixAll.append(StrUtil.strBuilder(thisTableAlias, StrUtil.DOT, CITE, tableFieldName, CITE, StrUtil.COMMA));
                    }
                    query = showPrefixAll.del(showPrefixAll.length() - 1, showPrefixAll.length()).toString();
                    joinShowPrefixAllMap.put(joinKey, query);
                }
            }

            FastSqlUtil.whereSql(conditionSql, joinCondition, paramsMap, Boolean.FALSE, Boolean.TRUE);
            this.joinFastDao = joinFastDao;
        }

        public Join(JoinDirection direction, String customizeJoinCondition) {
            joinSql.append(direction.direction).append(customizeJoinCondition);
        }

        public JoinFastDao<T> on(FastExample.FieldCriteria<P, F> leftCondition, FastExample.FieldCriteria rightCondition) {
            FastExample leftFastExample = BeanUtil.getProperty(leftCondition, FAST_EXAMPLE);
            FastExample rightFastExample = BeanUtil.getProperty(rightCondition, FAST_EXAMPLE);
            String leftTableName = StrUtil.isNotBlank(leftFastExample.conditionPackages().getTableAlias()) ?
                    leftFastExample.conditionPackages().getTableAlias() : leftFastExample.conditionPackages().getTableMapper().getTableName();
            String rightTableName = StrUtil.isNotBlank(rightFastExample.conditionPackages().getTableAlias()) ?
                    rightFastExample.conditionPackages().getTableAlias() : rightFastExample.conditionPackages().getTableMapper().getTableName();
            if (!StrUtil.equalsAnyIgnoreCase(joinSql, ON)) {
                joinSql.append(ON);
            } else {
                joinSql.append(StrUtil.SPACE).append(AND);
            }
            joinSql.append(StrUtil.strBuilder(leftTableName, StrUtil.DOT, leftFastExample.conditionPackages().getTableMapper().getFieldTableNames().get(leftFastExample.getFieldName()).toString(),
                    EQUAL, rightTableName, StrUtil.DOT, rightFastExample.conditionPackages().getTableMapper().getFieldTableNames().get(rightFastExample.getFieldName()).toString()
            ));
            return joinFastDao;
        }

        public JoinFastDao<T> on(String onConditionSql, Object params) {
            if (StrUtil.containsIgnoreCase(joinSql, ON)) {
                joinSql.append(AND);
            } else {
                joinSql.append(ON);
            }
            joinSql.append(onConditionSql);
            paramsMap.putAll(SqlTemplateUtil.getMap(params));
            return joinFastDao;
        }

    }

    private enum JoinDirection {
        /**
         * 连接方向
         */
        左连接("left join "), 右连接("right join "), 内连接("inner join ");
        public String direction;

        JoinDirection(String direction) {
            this.direction = direction;
        }
    }
}

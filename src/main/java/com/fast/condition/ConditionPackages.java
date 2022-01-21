package com.fast.condition;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.annotation.JSONField;
import com.fast.dao.many.FastJoinQueryInfo;
import com.fast.mapper.TableMapper;
import com.fast.mapper.TableMapperUtil;

import java.io.Serializable;
import java.util.*;

/**
 * 查询条件封装
 *
 * @author 张亚伟 https://github.com/kaixinzyw
 */
public class ConditionPackages<POJO> implements Serializable {

    private static final long serialVersionUID = -3640643704263216648L;

    /**
     * 表映射器
     */
    @JSONField(serialize = false)
    private final TableMapper tableMapper;
    /**
     * 表别名
     */
    private String tableAlias;

    /**
     * 表名
     */
    private final String tableName;
    /**
     * 条件封装
     */
    private List<FastCondition> conditions = new ArrayList<>();
    /**
     * 条件拼接方式,默认AND
     */
    @JSONField(serialize = false)
    private FastCondition.Way way = FastCondition.Way.AND;

    /**
     * 查询特定字段
     */
    private Set<String> showFields;
    /**
     * 屏蔽特定字段
     */
    private Set<String> hideFields;
    /**
     * 去重字段
     */
    private Set<String> distinctFields;

    /**
     * 字段求和
     */
    private Set<String> sumFields;

    /**
     * 字段求平均值
     */
    private Set<String> avgFields;

    /**
     * 字段求最小值
     */
    private Set<String> minFields;

    /**
     * 字段求最大值
     */
    private Set<String> maxFields;

    /**
     * 自定义查询列
     */
    private Set<String> customQueryColumns;

    /**
     * 自定义更新列
     */
    private Map<String, CustomizeUpdate.CustomizeUpdateData> customUpdateColumns;

    /**
     * 逻辑删除保护,默认开启
     */
    private Boolean logicDeleteProtect = Boolean.TRUE;


    /**
     * 排序
     */
    private List<OrderByQuery> orderByQuery;

    /**
     * 分页信息
     */
    private Integer page;
    private Integer size;
    private Integer limit;

    /**
     * 自定义SQL
     */
    private String customSql;
    /**
     * 自定义SQL参数
     */
    private Map<String, Object> customSqlParams;

    /**
     * 多表查询信息
     */
    private List<FastJoinQueryInfo> fastJoinQueryInfoList;

    /**
     * SQL执行时间
     */
    private Long sqlTime;

    /**
     * 操作所用到的对象信息
     */
    private POJO update;

    /**
     * 操作所用到的对象集合信息
     */
    private List<POJO> insertList;

    /**
     * 返回结果
     */
    private Object returnVal;

    /**
     * 拼接后的SQL语句
     */
    private String sql;
    /**
     * SQL参数
     */
    private Map<String, Object> paramMap = new HashMap<>();
    /**
     * 更新操作是否对不进行参数为null的字段进行操作
     */
    private Boolean updateSelective;
    /**
     * 是否为逻辑删除
     */
    private Boolean logicDelete;


    public ConditionPackages(Class<POJO> pojoClass) {
        this.tableMapper = TableMapperUtil.getTableMappers(pojoClass);
        this.tableName = this.getTableMapper().getTableName();
    }

    public void addEqualFieldQuery(String fieldName, Object value) {
        conditions.add(FastCondition.equal(fieldName, value, way));
    }

    public void addNotEqualFieldQuery(String fieldName, Object value) {
        conditions.add(FastCondition.notEqual(fieldName, value, way));
    }

    public void leftBracket() {
        conditions.add(FastCondition.leftBracket(way));
    }

    public void rightBracket() {
        conditions.add(FastCondition.rightBracket());
    }

    public void addLikeQuery(String fieldName, Object value) {
        conditions.add(FastCondition.like(fieldName, value, way));
    }

    public void addNotLikeQuery(String fieldName, Object value) {
        conditions.add(FastCondition.notLike(fieldName, value, way));
    }

    public void addMatchQuery(String matchName, Object againstValue) {
        conditions.add(FastCondition.match(matchName, againstValue, way));
    }

    public void addNotMatchQuery(String matchName, Object againstValue) {
        conditions.add(FastCondition.notMatch(matchName, againstValue, way));
    }

    public void addInQuery(String inName, Object... inValues) {
        conditions.add(FastCondition.in(inName, new ArrayList<>(Arrays.asList(inValues)), way));
    }

    public void addInQuery(String inName, Collection inValues) {
        conditions.add(FastCondition.in(inName, inValues, way));
    }

    public void addNotInQuery(String inName, Object... inValues) {
        conditions.add(FastCondition.notIn(inName, new ArrayList<>(Arrays.asList(inValues)), way));
    }

    public void addNotInQuery(String inName, Collection inValues) {
        conditions.add(FastCondition.notIn(inName, inValues, way));
    }

    public void addBetweenQuery(String betweenName, Object betweenMin, Object betweenMax) {
        conditions.add(FastCondition.betweenQuery(betweenName, betweenMin, betweenMax, way));
    }

    public void addNotBetweenQuery(String betweenName, Object betweenMin, Object betweenMax) {
        conditions.add(FastCondition.notBetweenQuery(betweenName, betweenMin, betweenMax, way));
    }

    public void addNullFieldsQuery(String nullField) {
        conditions.add(FastCondition.isNullFields(nullField, way));
    }


    public void addNotNullFieldsQuery(String notNullField) {
        conditions.add(FastCondition.notNullFields(notNullField, way));
    }

    public void addGreaterFieldsQuery(String fieldName, Object value) {
        conditions.add(FastCondition.greater(fieldName, value, way));
    }


    public void addGreaterOrEqualFieldsQuery(String fieldName, Object value) {
        conditions.add(FastCondition.greaterOrEqual(fieldName, value, way));
    }

    public void addLessFieldsQuery(String fieldName, Object value) {
        conditions.add(FastCondition.less(fieldName, value, way));
    }

    public void addLessOrEqualFieldsQuery(String fieldName, Object value) {
        conditions.add(FastCondition.lessOrEqual(fieldName, value, way));
    }


    public void setEqualObject(Object object) {
        conditions.add(FastCondition.equalObject(object));
    }

    public void addSql(String sql, Map<String, Object> params) {
        conditions.add(FastCondition.sql(sql, params, way));
    }


    public Set<String> getShowFields() {
        return showFields;
    }

    public void addShowField(String showField) {
        if (this.showFields == null) {
            this.showFields = new HashSet<>();
        }
        this.showFields.add(showField);
    }

    public Set<String> getHideFields() {
        return hideFields;
    }

    public void addHideField(String hideField) {
        if (this.hideFields == null) {
            this.hideFields = new HashSet<>();
        }
        this.hideFields.add(hideField);
    }

    public Set<String> getDistinctFields() {
        return distinctFields;
    }

    public void addDistinctField(String distinctField) {
        if (this.distinctFields == null) {
            this.distinctFields = new HashSet<>();
        }
        this.distinctFields.add(distinctField);
    }

    public Set<String> getSumFields() {
        return sumFields;
    }

    public void addSumFields(String sumField) {
        if (this.sumFields == null) {
            this.sumFields = new HashSet<>();
        }
        this.sumFields.add(sumField);
    }

    public Set<String> getAvgFields() {
        return avgFields;
    }

    public void addAvgFields(String avgField) {
        if (this.avgFields == null) {
            this.avgFields = new HashSet<>();
        }
        this.avgFields.add(avgField);
    }

    public Set<String> getMinFields() {
        return minFields;
    }

    public void addMinFields(String minField) {
        if (this.minFields == null) {
            this.minFields = new HashSet<>();
        }
        this.minFields.add(minField);
    }

    public Set<String> getMaxFields() {
        return maxFields;
    }

    public void addMaxFields(String maxField) {
        if (this.maxFields == null) {
            this.maxFields = new HashSet<>();
        }
        this.maxFields.add(maxField);
    }

    public Set<String> getCustomQueryColumns() {
        return customQueryColumns;
    }

    public void addCustomQueryColumn(String customField) {
        if (this.customQueryColumns == null) {
            this.customQueryColumns = new HashSet<String>();
        }
        this.customQueryColumns.add(customField);
    }

    public Map<String, CustomizeUpdate.CustomizeUpdateData> getCustomUpdateColumns() {
        return customUpdateColumns;
    }

    public void addCustomUpdateColumns(String fieldName, CustomizeUpdate.CustomizeUpdateData value) {
        if (this.customUpdateColumns == null) {
            this.customUpdateColumns = new HashMap<>();
        }
        this.customUpdateColumns.put(fieldName, value);
    }

    public List<OrderByQuery> getOrderByQuery() {
        return orderByQuery;
    }

    public void addOrderByQuery(String orderByName, Boolean isDesc) {
        if (this.orderByQuery == null) {
            this.orderByQuery = new ArrayList<>();
        }
        this.orderByQuery.add(new OrderByQuery(orderByName, isDesc));
    }

    public void customSQL(String customSQL, Map<String, Object> customSQLParams) {
        this.customSql = customSQL;
        this.customSqlParams = customSQLParams;
        this.paramMap.putAll(customSQLParams);
    }

    public void closeLogicDeleteProtect() {
        this.logicDeleteProtect = Boolean.FALSE;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    public void setWay(FastCondition.Way way) {
        this.way = way;
    }

    public List<FastCondition> getConditions() {
        return conditions;
    }

    public void setConditions(List<FastCondition> conditions) {
        this.conditions=conditions;
    }

    public Boolean getLogicDeleteProtect() {
        return logicDeleteProtect;
    }

    public String getCustomSql() {
        return customSql;
    }

    public Map<String, Object> getCustomSqlParams() {
        return customSqlParams;
    }

    public TableMapper getTableMapper() {
        return tableMapper;
    }

    public Long getSqlTime() {
        return sqlTime;
    }

    public void setSqlTime(Long sqlTime) {
        this.sqlTime = sqlTime;
    }

    public POJO getUpdate() {
        return update;
    }

    public void setUpdate(POJO update) {
        this.update = update;
    }

    public List<POJO> getInsertList() {
        return insertList;
    }

    public void setInsertList(List<POJO> insertList) {
        this.insertList = insertList;
    }

    public Object getReturnVal() {
        return returnVal;
    }

    public void setReturnVal(Object returnVal) {
        this.returnVal = returnVal;
    }

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    public Map<String, Object> getParamMap() {
        return paramMap;
    }

    public void setParamMap(Map<String, Object> paramMap) {
        this.paramMap = paramMap;
    }

    public Boolean getUpdateSelective() {
        return updateSelective;
    }

    public void setUpdateSelective(Boolean updateSelective) {
        this.updateSelective = updateSelective;
    }

    public Boolean getLogicDelete() {
        return logicDelete;
    }

    public void setLogicDelete(Boolean logicDelete) {
        this.logicDelete = logicDelete;
    }

    public List<FastJoinQueryInfo> getFastJoinQueryInfoList() {
        return fastJoinQueryInfoList;
    }

    public void addFastJoinQueryInfo(FastJoinQueryInfo fastJoinQueryInfo) {
        if (fastJoinQueryInfoList == null) {
            fastJoinQueryInfoList = new ArrayList<>();
        }
        fastJoinQueryInfoList.add(fastJoinQueryInfo);
    }


    public String getTableAlias() {
        return tableAlias;
    }

    public void setTableAlias(String tableAlias) {
        this.tableAlias = tableAlias;
    }

    public String getTableName() {
        return tableName;
    }
}

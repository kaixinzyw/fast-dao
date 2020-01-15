package com.fast.condition;

import cn.hutool.core.util.StrUtil;

import java.util.*;

/**
 * 查询条件封装
 *
 * @author 张亚伟 https://github.com/kaixinzyw
 */
public class ConditionPackages {

    /**
     * 条件封装
     */
    private List<FastCondition> conditions = new ArrayList<>();
    /**
     * 条件拼接方式,默认AND
     */
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

    private Set<String> customQueryColumns;

    private Map<String, String> customUpdateColumns;

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


    public void addEqualFieldQuery(String fieldName, Object value) {
        conditions.add(FastCondition.equal(fieldName, value, way));
    }

    public void addNotEqualFieldQuery(String fieldName, Object value) {
        conditions.add(FastCondition.notEqual(fieldName, value, way));
    }

    public void addLikeQuery(String fieldName, Object value) {
        conditions.add(FastCondition.like(fieldName, value, way));
    }

    public void addNotLikeQuery(String fieldName, Object value) {
        conditions.add(FastCondition.notLike(fieldName, value, way));
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

    public Map<String, String> getCustomUpdateColumns() {
        return customUpdateColumns;
    }

    public void addCustomUpdateColumns(String fieldName, String value) {
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
        this.customSql = customSQL + StrUtil.SPACE;
        this.customSqlParams = customSQLParams;
    }

    public static class OrderByQuery {

        /**
         * 排序字段名
         */
        private String orderByName;

        /**
         * 是否降序
         */
        private Boolean isDesc;

        public OrderByQuery(String orderByName, Boolean isDesc) {
            this.orderByName = orderByName;
            this.isDesc = isDesc;
        }

        public String getOrderByName() {
            return orderByName;
        }

        public void setOrderByName(String orderByName) {
            this.orderByName = orderByName;
        }

        public Boolean getDesc() {
            return isDesc;
        }

        public void setDesc(Boolean desc) {
            isDesc = desc;
        }
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

    public Boolean getLogicDeleteProtect() {
        return logicDeleteProtect;
    }

    public String getCustomSql() {
        return customSql;
    }

    public Map<String, Object> getCustomSqlParams() {
        return customSqlParams;
    }

}

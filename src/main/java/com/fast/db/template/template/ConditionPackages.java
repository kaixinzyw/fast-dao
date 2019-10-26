package com.fast.db.template.template;

import java.util.*;

/**
 * 查询条件封装
 *
 * @author 张亚伟 https://github.com/kaixinzyw/fast-db-template
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

    public void setEqualObject(Object object) {
        conditions.add(FastCondition.equalObject(object));
    }

    public void addEqualFieldQuery(String fieldName, Object value) {
        conditions.add(FastCondition.equal(fieldName, value, way));
    }

    public void addBetweenQuery(String betweenName, Object betweenMin, Object betweenMax) {
        conditions.add(FastCondition.betweenQuery(betweenName, betweenMin, betweenMax, way));
    }

    public void addInQuery(String inName, Collection inValues) {
        conditions.add(FastCondition.in(inName, inValues, way));
    }

    public void addInQuery(String inName, Object... inValues) {
        conditions.add(FastCondition.in(inName, new ArrayList<>(Arrays.asList(inValues)), way));
    }


    public void addNotNullFieldsQuery(String notNullField) {
        conditions.add(FastCondition.notNullFields(notNullField, way));
    }

    public void addNullFieldsQuery(String nullField) {
        conditions.add(FastCondition.isNullFields(nullField, way));
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

    public void addLikeQuery(String fieldName, Object value) {
        conditions.add(FastCondition.like(fieldName, value, way));
    }

    public void addSql(String sql, Map<String, Object> params) {
        conditions.add(FastCondition.sql(sql, params, way));
    }


    public Set<String> getShowFields() {
        return showFields;
    }

    public void addShowField(String showField) {
        if (this.showFields != null) {
            this.showFields.add(showField);
        } else {
            this.showFields = new HashSet<>();
            this.showFields.add(showField);
        }
    }

    public Set<String> getHideFields() {
        return hideFields;
    }

    public void addHideField(String hideField) {
        if (this.hideFields != null) {
            this.hideFields.add(hideField);
        } else {
            this.hideFields = new HashSet<>();
            this.hideFields.add(hideField);
        }
    }

    public Set<String> getDistinctFields() {
        return distinctFields;
    }

    public void addDistinctField(String distinctField) {
        if (this.distinctFields != null) {
            this.distinctFields.add(distinctField);
        } else {
            this.distinctFields = new HashSet<>();
            this.distinctFields.add(distinctField);
        }
    }

    public List<OrderByQuery> getOrderByQuery() {
        return orderByQuery;
    }

    public void addOrderByQuery(String orderByName, Boolean isDesc) {
        if (this.orderByQuery != null) {
            this.orderByQuery.add(new OrderByQuery(orderByName, isDesc));
        } else {
            this.orderByQuery = new ArrayList<>(Arrays.asList(new OrderByQuery(orderByName, isDesc)));
        }
    }

    public void customSQL(String customSQL, Map<String, Object> customSQLParams){
        this.customSql = customSQL;
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

package com.fast.db.template.template;


import java.util.Collection;
import java.util.List;

/**
 * @author 张亚伟 398850094@qq.com
 * @version 1.0
 */
public class MapperQueryObject {

    public static class ExampleUpdate<Pojo> {
        /**
         * 查询条件
         */
        private Pojo select;

        /**
         * 更新内容
         */
        private Pojo update;

        public ExampleUpdate(Pojo select, Pojo update) {
            this.select = select;
            this.update = update;
        }

        public Pojo getSelect() {
            return select;
        }

        public void setSelect(Pojo select) {
            this.select = select;
        }

        public Pojo getUpdate() {
            return update;
        }

        public void setUpdate(Pojo update) {
            this.update = update;
        }
    }

    public static class UpdateCompoundQuery<T> {
        /**
         * 更新条件
         */
        private CompoundQuery compoundQuery;
        /**
         * 更新内容
         */
        private T entity;

        public UpdateCompoundQuery(CompoundQuery compoundQuery, T entity) {
            this.compoundQuery = compoundQuery;
            this.entity = entity;
        }

        public CompoundQuery getCompoundQuery() {
            return compoundQuery;
        }

        public void setCompoundQuery(CompoundQuery compoundQuery) {
            this.compoundQuery = compoundQuery;
        }

        public T getEntity() {
            return entity;
        }

        public void setEntity(T entity) {
            this.entity = entity;
        }
    }


    public static class UpdateByIn<T> {

        /**
         * 更新内容
         */
        private T update;

        /**
         * in操作的字段
         */
        private String parameter;

        /**
         * in操作的值
         */
        private Collection<Object> ins;

        public UpdateByIn(T update, String parameter, Collection<Object> ins) {
            this.update = update;
            this.parameter = parameter;
            this.ins = ins;
        }

        public T getUpdate() {
            return update;
        }

        public void setUpdate(T update) {
            this.update = update;
        }

        public String getParameter() {
            return parameter;
        }

        public void setParameter(String parameter) {
            this.parameter = parameter;
        }

        public Collection<Object> getIns() {
            return ins;
        }

        public void setIns(Collection<Object> ins) {
            this.ins = ins;
        }
    }


    public static class RangeQuery {
        /**
         * 查询范围字段
         */
        private String rangeName;

        /**
         * 查询范围最小值
         */
        private Object rangeMin;

        /**
         * 查询范围最大值
         */
        private Object rangeMax;


        public RangeQuery(String rangeName, Object rangeMin, Object rangeMax) {
            this.rangeName = rangeName;
            this.rangeMin = rangeMin;
            this.rangeMax = rangeMax;
        }

        public String getRangeName() {
            return rangeName;
        }

        public void setRangeName(String rangeName) {
            this.rangeName = rangeName;
        }

        public Object getRangeMin() {
            return rangeMin;
        }

        public void setRangeMin(Object rangeMin) {
            this.rangeMin = rangeMin;
        }

        public Object getRangeMax() {
            return rangeMax;
        }

        public void setRangeMax(Object rangeMax) {
            this.rangeMax = rangeMax;
        }
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

    public static class InQuery {

        /**
         * 包含查询的字段名
         */
        private String inName;

        /**
         * 包含查询的值
         */
        private Collection<Object> inValues;

        public InQuery(String inName, Collection<Object> inValues) {
            this.inName = inName;
            this.inValues = inValues;
        }

        public String getInName() {
            return inName;
        }

        public void setInName(String inName) {
            this.inName = inName;
        }

        public Collection<Object> getInValues() {
            return inValues;
        }

        public void setInValues(List<Object> inValues) {
            this.inValues = inValues;
        }
    }

    public static class FieldQuery {

        /**
         * 匹配查询的字段名
         */
        private String fieldName;

        /**
         * 匹配查询的值
         */
        private Object value;


        public FieldQuery(String fieldName, Object value) {
            this.fieldName = fieldName;
            this.value = value;
        }

        public String getFieldName() {
            return fieldName;
        }

        public void setFieldName(String fieldName) {
            this.fieldName = fieldName;
        }

        public Object getValue() {
            return value;
        }

        public void setValue(Object value) {
            this.value = value;
        }
    }

    public static class SelectOrderByQuery<T> {

        /**
         * 查询对象
         */
        private T entity;

        /**
         * 排序字段名
         */
        private String orderByName;

        /**
         * 是否降序
         */
        private Boolean isDesc;

        public SelectOrderByQuery(T entity, String orderByName, Boolean isDesc) {
            this.entity = entity;
            this.orderByName = orderByName;
            this.isDesc = isDesc;
        }

        public T getEntity() {
            return entity;
        }

        public void setEntity(T entity) {
            this.entity = entity;
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


}

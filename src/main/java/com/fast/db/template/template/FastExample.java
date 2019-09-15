package com.fast.db.template.template;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class FastExample {

    public final Criteria criteria = new Criteria();

    public Criteria field(String fieldName) {
        criteria.fieldName = fieldName;
        return criteria;
    }

    /**
     * 对象中字段非空字段AND查询
     *
     * @return
     */
    public void equalObject(Object obj) {
        criteria.compoundQuery.setEqualObject(obj);
    }


    public class Criteria {

        private String fieldName;

        public final CompoundQuery compoundQuery;


        public Criteria(CompoundQuery compoundQuery) {
            this.compoundQuery = compoundQuery;
        }

        public Criteria() {
            this.compoundQuery = new CompoundQuery();
        }


        /**
         * 查询特定字段
         *
         * @return
         */
        public void showField() {
            compoundQuery.addShowField(fieldName);
        }

        /**
         * 屏蔽特定字段
         *
         * @return
         */
        public void hideField() {
            compoundQuery.addHideField(fieldName);
        }

        public void distinctField() {
            compoundQuery.addDistinctField(fieldName);
        }

        /**
         * 或条件
         *
         * @return
         */
        public Criteria or() {
            if (compoundQuery.getOr() == null) {
                compoundQuery.setOr(new CompoundQuery());
            }
            Criteria criteria = new Criteria(compoundQuery.getOr());
            criteria.fieldName = fieldName;
            return criteria;
        }

        /**
         * 范围条件
         *
         * @param rangeMin 最小值
         * @param rangeMax 最大值
         * @return
         */
        public Criteria range(Object rangeMin, Object rangeMax) {
            compoundQuery.addRangeQuery(fieldName, rangeMin, rangeMax);
            return this;
        }

        /**
         * 排序-降序 查询时有用
         *
         * @return
         */
        public Criteria orderByDesc() {
            compoundQuery.addOrderByQuery(fieldName, true);
            return this;

        }

        /**
         * 排序-升序 查询时有用
         *
         * @return
         */
        public Criteria orderByAsc() {
            compoundQuery.addOrderByQuery(fieldName, false);
            return this;
        }

        /**
         * 包含条件
         *
         * @param inValues 所包含的值(a,b,c)
         * @return
         */
        public Criteria in(Object... inValues) {
            compoundQuery.addInQuery(fieldName, inValues);
            return this;
        }

        /**
         * 包含查询
         *
         * @param inValues 所包含的值([a,b,c])
         * @return
         */
        public Criteria in(Collection inValues) {
            compoundQuery.addInQuery(fieldName, inValues);
            return this;
        }

        /**
         * 值不为空条件
         *
         * @return
         */
        public Criteria notNull() {
            compoundQuery.addNotNullFieldsQuery(fieldName);
            return this;
        }

        /**
         * 值为空条件
         *
         * @return
         */
        public Criteria isNull() {
            compoundQuery.addNullFieldsQuery(fieldName);
            return this;
        }

        /**
         * 值等于条件
         *
         * @param value
         * @return
         */
        public Criteria equal(Object value) {
            compoundQuery.addEqualFieldQuery(fieldName, value);
            return this;
        }

        /**
         * 值大于等于条件
         *
         * @param value
         * @return
         */
        public Criteria greaterOrEqual(Object value) {
            compoundQuery.addGreaterOrEqualFieldsQuery(fieldName, value);
            return this;
        }

        /**
         * 值小于等于条件
         *
         * @param value
         * @return
         */
        public Criteria lessOrEqual(Object value) {
            compoundQuery.addLessOrEqualFieldsQuery(fieldName, value);
            return this;
        }


        /**
         * 值大于条件
         *
         * @param value
         * @return
         */
        public Criteria greater(Object value) {
            compoundQuery.addGreaterFieldsQuery(fieldName, value);
            return this;
        }

        /**
         * 值小于条件
         *
         * @param value
         * @return
         */
        public Criteria less(Object value) {
            compoundQuery.addLessFieldsQuery(fieldName, value);
            return this;
        }

        /**
         * 值模糊查询条件
         *
         * @param value
         * @return
         */
        public Criteria like(Object value) {
            compoundQuery.addLikeQuery(fieldName, value);
            return this;
        }


    }

}

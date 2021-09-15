package com.fast.condition;

import cn.hutool.core.util.StrUtil;
import com.fast.fast.FastDao;
import com.fast.utils.FastValueUtil;

import java.io.Serializable;
import java.util.Collection;
import java.util.Map;

/**
 * SQL条件操作
 *
 * @param <P> 操作的类泛型
 * @author 张亚伟 https://github.com/kaixinzyw
 */
public class FastExample<P, T> implements Serializable {

    private static final long serialVersionUID = 756400131596569134L;
    private ConditionPackages<P> conditionPackages;
    private FieldCriteria<P, T> fieldCriteria;
    private GlobalCriteria<P, T> globalCriteria;
    private T operationObject;
    private String fieldName;
    /**
     * 操作的类信息
     */
    private Class<P> pojoClass;

    private FastExample() {
    }

    public Class<P> getPojoClass() {
        return pojoClass;
    }

    /**
     * 初始化创建
     * @param pojoClass       操作类信息
     * @param operationObject 操作对象
     */
    public FastExample(Class<P> pojoClass, T operationObject) {
        if (pojoClass == null) {
            throw new RuntimeException("FastExample初始化失败 pojoClass不能为null");
        }
        this.pojoClass = pojoClass;
        this.operationObject = operationObject;
        this.conditionPackages = ConditionPackages.create(pojoClass);

    }

    public GlobalCriteria<P, T> global(){
        if (globalCriteria == null) {
            globalCriteria = new GlobalCriteria<>(this);
        }
        return globalCriteria;
    }

    public String getFieldName() {
        return fieldName;
    }

    /**
     * 设置操作字段
     *
     * @param fieldName 字段名
     * @return 条件操作工具
     */
    public FieldCriteria<P, T> field(String fieldName) {
        this.fieldName = fieldName;
        this.conditionPackages.setWay(FastCondition.Way.AND);
        if (fieldCriteria == null) {
            fieldCriteria = new FieldCriteria<>(this);
        }
        return fieldCriteria;
    }

    /**
     * 条件封装类
     */

    public static class GlobalCriteria<P,T> {

        private final FastExample<P,T> fastExample;

        public GlobalCriteria(FastExample<P,T> fastExample) {
            this.fastExample = fastExample;
        }

        /**
         * 自定义查询列,可使用SQL函数,只有在查询时候生效
         * 警告! 此方法有SQL注入风险,请严格检查所传参数
         *
         * @param queryColumn SELECT查询时自定义查询列
         * @return {@link T}
         */
        public T customQueryColumn(String queryColumn) {
            if (StrUtil.isBlank(queryColumn)) {
                return fastExample.operationObject;
            }
            fastExample.conditionPackages.addCustomQueryColumn(queryColumn);
            return fastExample.operationObject;
        }

        /**
         * 对象查询
         *
         * @param o 对象在不为空的字段作为AND条件
         * @return {@link T}
         */
        public T equalObject(Object o) {
            if (o == null) {
                return fastExample.operationObject;
            }
            fastExample.conditionPackages.setEqualObject(o);
            return fastExample.operationObject;
        }

        /**
         * 自定义SQL查询,使用AND进行拼接
         *
         * @param sql    自定义SQL语句,如果有参数需要使用#{参数名}进行占位
         * @param params 参数值MAP集合
         * @return {@link T}
         */
        public T andSql(String sql, Map<String, Object> params) {
            if (StrUtil.isBlank(sql)) {
                return fastExample.operationObject;
            }
            fastExample.conditionPackages.setWay(FastCondition.Way.AND);
            fastExample.conditionPackages.addSql(sql, params);
            return fastExample.operationObject;
        }

        /**
         * 自定义SQL查询
         *
         * @param sql    自定义SQL语句,如果有参数需要使用#{参数名}进行占位
         * @param params 参数值MAP集合
         * @return {@link T}
         */
        public T sql(String sql, Map<String, Object> params) {
            if (StrUtil.isBlank(sql)) {
                return fastExample.operationObject;
            }
            fastExample.conditionPackages.setWay(FastCondition.Way.CUSTOM);
            fastExample.conditionPackages.addSql(sql, params);
            return fastExample.operationObject;
        }

        /**
         * 自定义SQL查询,使用OR进行拼接
         *
         * @param sql    自定义SQL语句,如果有参数需要使用#{参数名}进行占位
         * @param params 参数值MAP集合
         * @return {@link T}
         */
        public T orSql(String sql, Map<String, Object> params) {
            if (StrUtil.isBlank(sql)) {
                return fastExample.operationObject;
            }
            fastExample.conditionPackages.setWay(FastCondition.Way.OR);
            fastExample.conditionPackages.addSql(sql, params);
            return fastExample.operationObject;
        }

        /**
         * 左括号
         *
         * @return {@link T}
         */
        public T orLeftBracket() {
            fastExample.conditionPackages.setWay(FastCondition.Way.OR);
            fastExample.conditionPackages.leftBracket();
            return fastExample.operationObject;
        }

        /**
         * 左括号
         *
         * @return {@link T}
         */
        public T andLeftBracket() {
            fastExample.conditionPackages.setWay(FastCondition.Way.AND);
            fastExample.conditionPackages.leftBracket();
            return fastExample.operationObject;
        }

        /**
         * 右括号
         *
         * @return {@link T}
         */
        public T rightBracket() {
            fastExample.conditionPackages.rightBracket();
            return fastExample.operationObject;
        }

        /**
         * 关闭逻辑删除保护,关闭后所有操作会影响到被逻辑删除标记的数据
         *
         * @return {@link T}
         */
        public T closeLogicDeleteProtect() {
            fastExample.conditionPackages.closeLogicDeleteProtect();
            return fastExample.operationObject;
        }

        /**
         * 打开相关查询
         *
         * @return {@link T}
         */
        public T openRelatedQuery() {
            fastExample.conditionPackages.openRelatedQuery();
            return fastExample.operationObject;
        }

        public FastDao<P> dao() {
            return fastExample.dao();
        }
    }

    /**
     * 获取条件封装
     *
     * @return 条件封装
     */
    public ConditionPackages<P> conditionPackages() {
        return this.conditionPackages;
    }

    /**
     * 获取执行器
     *
     * @return Dao执行器
     */
    public FastDao<P> dao() {
        return FastDao.init(conditionPackages);
    }


    public static class FieldCriteria<P, T> implements Serializable {

        private static final long serialVersionUID = 2676504598415330839L;

        /**
         * SQL封装操作器
         */
        private final FastExample<P, T> fastExample;

        public FieldCriteria(FastExample<P, T> fastExample) {
            this.fastExample = fastExample;
        }

        /**
         * @param value 值等于条件,如果参数为数组并且长度大于1,使用in查询
         * @return 条件操作工具
         */
        public T valEqual(Object value) {
            if (FastValueUtil.valueIsNullVerify(value)) {
                return fastExample.operationObject;
            }
            fastExample.conditionPackages.addEqualFieldQuery(fastExample.fieldName, value);
            return fastExample.operationObject;
        }

        /**
         * @param value 值等不于条件,如果参数为数组并且长度大于1,使用not in查询
         * @return 条件操作工具
         */
        public T notValEqual(Object value) {
            if (FastValueUtil.valueIsNullVerify(value)) {
                return fastExample.operationObject;
            }
            fastExample.conditionPackages.addNotEqualFieldQuery(fastExample.fieldName, value);
            return fastExample.operationObject;
        }

        /**
         * 值模糊查询条件
         *
         * @param value 值
         * @return 条件操作工具
         */
        public T like(String value) {
            if (FastValueUtil.valueIsNullVerify(value)) {
                return fastExample.operationObject;
            }
            fastExample.conditionPackages.addLikeQuery(fastExample.fieldName, "%" + value + "%");
            return fastExample.operationObject;
        }

        /**
         * 值模糊查询条件
         *
         * @param value 值
         * @return 条件操作工具
         */
        public T notLike(String value) {
            if (FastValueUtil.valueIsNullVerify(value)) {
                return fastExample.operationObject;
            }
            fastExample.conditionPackages.addNotLikeQuery(fastExample.fieldName, "%" + value + "%");
            return fastExample.operationObject;
        }

        /**
         * 值模糊查询条件
         *
         * @param value 值
         * @return 条件操作工具
         */
        public T likeLeft(String value) {
            if (FastValueUtil.valueIsNullVerify(value)) {
                return fastExample.operationObject;
            }
            fastExample.conditionPackages.addLikeQuery(fastExample.fieldName, "%" + value);
            return fastExample.operationObject;
        }

        /**
         * 值模糊查询条件
         *
         * @param value 值
         * @return 条件操作工具
         */
        public T notLikeLeft(String value) {
            if (FastValueUtil.valueIsNullVerify(value)) {
                return fastExample.operationObject;
            }
            fastExample.conditionPackages.addNotLikeQuery(fastExample.fieldName, "%" + value);
            return fastExample.operationObject;
        }

        /**
         * 值模糊查询条件
         *
         * @param value 值
         * @return 条件操作工具
         */
        public T likeRight(String value) {
            if (FastValueUtil.valueIsNullVerify(value)) {
                return fastExample.operationObject;
            }
            fastExample.conditionPackages.addLikeQuery(fastExample.fieldName, value + "%");
            return fastExample.operationObject;
        }

        /**
         * 值模糊查询条件
         *
         * @param value 值
         * @return 条件操作工具
         */
        public T notLikeRight(String value) {
            if (FastValueUtil.valueIsNullVerify(value)) {
                return fastExample.operationObject;
            }
            fastExample.conditionPackages.addNotLikeQuery(fastExample.fieldName, value + "%");
            return fastExample.operationObject;
        }

        /**
         * 全文检索条件
         *
         * @param againstValue 全文检索条件
         * @return 条件操作工具
         */
        public T match(Object againstValue) {
            if (FastValueUtil.valueIsNullVerify(againstValue)) {
                return fastExample.operationObject;
            }
            fastExample.conditionPackages.addMatchQuery(fastExample.fieldName, againstValue);
            return fastExample.operationObject;
        }

        /**
         * 全文检索条件 Not
         *
         * @param againstValue 全文检索条件
         * @return 条件操作工具
         */
        public T notMatch(Object againstValue) {
            if (FastValueUtil.valueIsNullVerify(againstValue)) {
                return fastExample.operationObject;
            }
            fastExample.conditionPackages.addNotMatchQuery(fastExample.fieldName, againstValue);
            return fastExample.operationObject;
        }


        /**
         * 包含条件
         *
         * @param inValues 所包含的值(a,b,c)
         * @return 条件操作工具
         */
        public T in(Object... inValues) {
            if (FastValueUtil.arrayIsNullVerify(inValues)) {
                return fastExample.operationObject;
            }
            fastExample.conditionPackages.addInQuery(fastExample.fieldName, inValues);
            return fastExample.operationObject;
        }

        /**
         * 不包含条件
         *
         * @param inValues 所包含的值(a,b,c)
         * @return 条件操作工具
         */
        public T notIn(Object... inValues) {
            if (FastValueUtil.arrayIsNullVerify(inValues)) {
                return fastExample.operationObject;
            }
            fastExample.conditionPackages.addNotInQuery(fastExample.fieldName, inValues);
            return fastExample.operationObject;
        }

        /**
         * 包含查询
         *
         * @param inValues 所包含的值([a,b,c])
         * @return 条件操作工具
         */
        public T in(Collection inValues) {
            if (FastValueUtil.collectionIsNullVerify(inValues)) {
                return fastExample.operationObject;
            }
            fastExample.conditionPackages.addInQuery(fastExample.fieldName, inValues);
            return fastExample.operationObject;
        }

        /**
         * 不包含查询
         *
         * @param inValues 所包含的值([a,b,c])
         * @return 条件操作工具
         */
        public T notIn(Collection inValues) {
            if (FastValueUtil.collectionIsNullVerify(inValues)) {
                return fastExample.operationObject;
            }
            fastExample.conditionPackages.addNotInQuery(fastExample.fieldName, inValues);
            return fastExample.operationObject;
        }

        /**
         * 范围条件
         *
         * @param betweenMin 最小值
         * @param betweenMax 最大值
         * @return 条件操作工具
         */
        public T between(Object betweenMin, Object betweenMax) {
            if (FastValueUtil.valueIsNullVerify(betweenMin) || FastValueUtil.valueIsNullVerify(betweenMax)) {
                return fastExample.operationObject;
            }
            fastExample.conditionPackages.addBetweenQuery(fastExample.fieldName, betweenMin, betweenMax);
            return fastExample.operationObject;
        }

        /**
         * 排除范围条件
         *
         * @param betweenMin 最小值
         * @param betweenMax 最大值
         * @return 条件操作工具
         */
        public T notBetween(Object betweenMin, Object betweenMax) {
            if (FastValueUtil.valueIsNullVerify(betweenMin) || FastValueUtil.valueIsNullVerify(betweenMax)) {
                return fastExample.operationObject;
            }
            fastExample.conditionPackages.addNotBetweenQuery(fastExample.fieldName, betweenMin, betweenMax);
            return fastExample.operationObject;
        }

        /**
         * 值不为空条件
         *
         * @return 条件操作工具
         */
        public T notNull() {
            fastExample.conditionPackages.addNotNullFieldsQuery(fastExample.fieldName);
            return fastExample.operationObject;
        }

        /**
         * 值为空条件
         *
         * @return 条件操作工具
         */
        public T isNull() {
            fastExample.conditionPackages.addNullFieldsQuery(fastExample.fieldName);
            return fastExample.operationObject;
        }

        /**
         * 值大于等于条件
         *
         * @param value 值等于条件
         * @return 条件操作工具
         */
        public T greaterOrEqual(Object value) {
            if (FastValueUtil.valueIsNullVerify(value)) {
                return fastExample.operationObject;
            }
            fastExample.conditionPackages.addGreaterOrEqualFieldsQuery(fastExample.fieldName, value);
            return fastExample.operationObject;
        }

        /**
         * 值小于等于条件
         *
         * @param value 值等于条件
         * @return 条件操作工具
         */
        public T lessOrEqual(Object value) {
            if (FastValueUtil.valueIsNullVerify(value)) {
                return fastExample.operationObject;
            }
            fastExample.conditionPackages.addLessOrEqualFieldsQuery(fastExample.fieldName, value);
            return fastExample.operationObject;
        }

        /**
         * 值大于条件
         *
         * @param value 值等于条件
         * @return 条件操作工具
         */
        public T greater(Object value) {
            if (FastValueUtil.valueIsNullVerify(value)) {
                return fastExample.operationObject;
            }
            fastExample.conditionPackages.addGreaterFieldsQuery(fastExample.fieldName, value);
            return fastExample.operationObject;
        }

        /**
         * 值小于条件
         *
         * @param value 值等于条件
         * @return 条件操作工具
         */
        public T less(Object value) {
            if (FastValueUtil.valueIsNullVerify(value)) {
                return fastExample.operationObject;
            }
            fastExample.conditionPackages.addLessFieldsQuery(fastExample.fieldName, value);
            return fastExample.operationObject;
        }

        /**
         * 对后续条件使用OR封装
         *
         * @return 条件操作工具
         */
        public FieldCriteria<P,T> or() {
            fastExample.conditionPackages.setWay(FastCondition.Way.OR);
            return this;
        }


        /**
         * 查询特定字段
         *
         * @return 条件操作工具
         */
        public T showField() {
            fastExample.conditionPackages.addShowField(fastExample.fieldName);
            return fastExample.operationObject;
        }

        /**
         * 屏蔽特定字段
         *
         * @return 条件操作工具
         */
        public T hideField() {
            fastExample.conditionPackages.addHideField(fastExample.fieldName);
            return fastExample.operationObject;
        }

        /**
         * 字段求和
         *
         * @return 条件操作工具
         */
        public T sumField() {
            fastExample.conditionPackages.addSumFields(fastExample.fieldName);
            return fastExample.operationObject;
        }

        /**
         * 字段求平均值
         *
         * @return 条件操作工具
         */
        public T avgField() {
            fastExample.conditionPackages.addAvgFields(fastExample.fieldName);
            return fastExample.operationObject;
        }

        /**
         * 字段求平均值
         *
         * @return 条件操作工具
         */
        public T minField() {
            fastExample.conditionPackages.addMinFields(fastExample.fieldName);
            return fastExample.operationObject;
        }

        /**
         * 字段求平均值
         *
         * @return 条件操作工具
         */
        public T maxField() {
            fastExample.conditionPackages.addMaxFields(fastExample.fieldName);
            return fastExample.operationObject;
        }

        /**
         * 字段去重
         *
         * @return 条件操作工具
         */
        public T distinctField() {
            fastExample.conditionPackages.addDistinctField(fastExample.fieldName);
            return fastExample.operationObject;
        }

        /**
         * 排序-降序 查询时有用
         *
         * @return 条件操作工具
         */
        public T orderByDesc() {
            fastExample.conditionPackages.addOrderByQuery(fastExample.fieldName, true);
            return fastExample.operationObject;

        }

        /**
         * 排序-升序 查询时有用
         *
         * @return 条件操作工具
         */
        public T orderByAsc() {
            fastExample.conditionPackages.addOrderByQuery(fastExample.fieldName, false);
            return fastExample.operationObject;
        }

        /**
         * 自定义更新,可使用SQL函数,只有在更新时候生效
         *
         * @return 查询封装
         */
        public CustomizeUpdate<P,T> customizeUpdateValue() {
            return new CustomizeUpdate<>(fastExample.pojoClass, fastExample, fastExample.fieldName);
        }

        /**
         * 开始Dao操作
         *
         * @return Dao执行器
         */
        public FastDao<P> dao() {
            return fastExample.dao();
        }

    }

}

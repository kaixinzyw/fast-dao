package com.fast.condition;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import com.fast.fast.FastDao;

import java.util.Collection;
import java.util.Map;

/**
 * SQL条件操作
 *
 * @param <T> 操作的类泛型
 * @author 张亚伟 https://github.com/kaixinzyw
 */
public class FastExample<T> {


    private FastExample() {
    }

    /**
     * 初始化创建
     *
     * @param pojoClass 操作类信息
     */
    public FastExample(Class<T> pojoClass) {
        if (pojoClass == null) {
            throw new RuntimeException("FastExample初始化失败 pojoClass不能为null");
        }
        criteria = new Criteria<>(pojoClass, this);
    }

    public Class<T> getPojoClass() {
        return criteria.pojoClass;
    }

    /**
     * 条件拼接工具
     */
    private Criteria<T> criteria;

    /**
     * 设置操作字段
     *
     * @param fieldName 字段名
     * @return 条件操作工具
     */
    public Criteria<T> field(String fieldName) {
        this.criteria.fieldName = fieldName;
        this.criteria.conditionPackages.setWay(FastCondition.Way.AND);
        return this.criteria;
    }

    /**
     * 获取条件封装
     *
     * @return 条件封装
     */
    public ConditionPackages conditionPackages() {
        return this.criteria.conditionPackages;
    }

    /**
     * 开始Dao操作
     *
     * @return Dao执行器
     */
    public FastDao<T> dao() {
        return criteria.dao();
    }

    /**
     * 自定义查询列,可使用SQL函数,只有在查询时候生效
     * 警告! 此方法有SQL注入风险,请严格检查所传参数
     * @param queryColumn SELECT查询时自定义查询列
     */
    public void customQueryColumn(String queryColumn) {
        if (StrUtil.isBlank(queryColumn)) {
            return;
        }
        criteria.conditionPackages.addCustomQueryColumn(queryColumn);
    }

    /**
     * 对象查询
     *
     * @param o 对象在不为空的字段作为AND条件
     */
    public void equalObject(Object o) {
        if (o == null) {
            return;
        }
        criteria.conditionPackages.setEqualObject(o);
    }

    /**
     * 自定义SQL查询,使用AND进行拼接
     *
     * @param sql    自定义SQL语句,如果有参数需要使用#{参数名}进行占位
     * @param params 参数值MAP集合
     */
    public void andSql(String sql, Map<String, Object> params) {
        if (StrUtil.isBlank(sql)) {
            return;
        }
        criteria.conditionPackages.setWay(FastCondition.Way.AND);
        criteria.conditionPackages.addSql(sql, params);
    }

    /**
     * 自定义SQL查询
     *
     * @param sql    自定义SQL语句,如果有参数需要使用#{参数名}进行占位
     * @param params 参数值MAP集合
     */
    public void sql(String sql, Map<String, Object> params) {
        if (StrUtil.isBlank(sql)) {
            return;
        }
        criteria.conditionPackages.setWay(FastCondition.Way.CUSTOM);
        criteria.conditionPackages.addSql(sql, params);
    }

    /**
     * 自定义SQL查询,使用OR进行拼接
     *
     * @param sql    自定义SQL语句,如果有参数需要使用#{参数名}进行占位
     * @param params 参数值MAP集合
     */
    public void orSql(String sql, Map<String, Object> params) {
        if (StrUtil.isBlank(sql)) {
            return;
        }
        criteria.conditionPackages.setWay(FastCondition.Way.OR);
        criteria.conditionPackages.addSql(sql, params);
    }

    /**
     * 关闭逻辑删除保护,关闭后所有操作会影响到被逻辑删除标记的数据
     */
    public void closeLogicDeleteProtect() {
        criteria.conditionPackages.closeLogicDeleteProtect();
    }


    public static class Criteria<P> {

        /**
         * 操作的类信息
         */
        private Class<P> pojoClass;

        /**
         * SQL封装操作器
         */
        private FastExample<P> fastExample;

        /**
         * 操作的字段
         */
        private String fieldName;

        public Criteria(Class<P> pojoClass, FastExample<P> fastExample) {
            this.pojoClass = pojoClass;
            this.fastExample = fastExample;
        }

        /**
         * 条件封装类
         */
        public final ConditionPackages conditionPackages = new ConditionPackages();

        /**
         * @param value 值等于条件,如果参数为数组并且长度大于1,使用in查询
         * @return 条件操作工具
         */
        public Criteria<P> valEqual(Object value) {
            if (value == null) {
                return this;
            }
            if (ArrayUtil.isArray(value)) {
                if (ArrayUtil.isEmpty(value)) {
                    return this;
                }
                Object[] vs = ArrayUtil.wrap(value);
                if (vs.length == 1) {
                    if (vs[0] != null) {
                        conditionPackages.addEqualFieldQuery(fieldName, vs[0]);
                    }
                    return this;
                } else {
                    in(vs);
                    return this;
                }
            }
            conditionPackages.addEqualFieldQuery(fieldName, value);
            return this;
        }

        /**
         * @param value 值等不于条件,如果参数为数组并且长度大于1,使用not in查询
         * @return 条件操作工具
         */
        public Criteria<P> notValEqual(Object value) {
            if (value == null) {
                return this;
            }
            if (ArrayUtil.isArray(value)) {
                if (ArrayUtil.isEmpty(value)) {
                    return this;
                }
                Object[] vs = ArrayUtil.wrap(value);
                if (vs.length == 1) {
                    if (vs[0] != null) {
                        conditionPackages.addNotEqualFieldQuery(fieldName, vs[0]);
                    }
                    return this;
                } else {
                    in(vs);
                    return this;
                }
            }
            conditionPackages.addNotEqualFieldQuery(fieldName, value);
            return this;
        }

        /**
         * 值模糊查询条件
         *
         * @param value 值
         * @return 条件操作工具
         */
        public Criteria<P> like(String value) {
            if (value == null) {
                return this;
            }
            conditionPackages.addLikeQuery(fieldName, "%" + value + "%");
            return this;
        }

        /**
         * 值模糊查询条件
         *
         * @param value 值
         * @return 条件操作工具
         */
        public Criteria<P> notLike(String value) {
            if (value == null) {
                return this;
            }
            conditionPackages.addNotLikeQuery(fieldName, "%" + value + "%");
            return this;
        }

        /**
         * 值模糊查询条件
         *
         * @param value 值
         * @return 条件操作工具
         */
        public Criteria<P> likeLeft(String value) {
            if (value == null) {
                return this;
            }
            conditionPackages.addLikeQuery(fieldName, "%" + value);
            return this;
        }

        /**
         * 值模糊查询条件
         *
         * @param value 值
         * @return 条件操作工具
         */
        public Criteria<P> notLikeLeft(String value) {
            if (value == null) {
                return this;
            }
            conditionPackages.addNotLikeQuery(fieldName, "%" + value);
            return this;
        }

        /**
         * 值模糊查询条件
         *
         * @param value 值
         * @return 条件操作工具
         */
        public Criteria<P> likeRight(String value) {
            if (value == null) {
                return this;
            }
            conditionPackages.addLikeQuery(fieldName, value + "%");
            return this;
        }

        /**
         * 值模糊查询条件
         *
         * @param value 值
         * @return 条件操作工具
         */
        public Criteria<P> notLikeRight(String value) {
            if (value == null) {
                return this;
            }
            conditionPackages.addNotLikeQuery(fieldName, value + "%");
            return this;
        }

        /**
         * 包含条件
         *
         * @param inValues 所包含的值(a,b,c)
         * @return 条件操作工具
         */
        public Criteria<P> in(Object... inValues) {
            if (ArrayUtil.isEmpty(inValues)) {
                return this;
            }
            conditionPackages.addInQuery(fieldName, inValues);
            return this;
        }

        /**
         * 不包含条件
         *
         * @param inValues 所包含的值(a,b,c)
         * @return 条件操作工具
         */
        public Criteria<P> notIn(Object... inValues) {
            if (ArrayUtil.isEmpty(inValues)) {
                return this;
            }
            conditionPackages.addNotInQuery(fieldName, inValues);
            return this;
        }

        /**
         * 包含查询
         *
         * @param inValues 所包含的值([a,b,c])
         * @return 条件操作工具
         */
        public Criteria<P> in(Collection inValues) {
            if (CollUtil.isEmpty(inValues)) {
                return this;
            }
            conditionPackages.addInQuery(fieldName, inValues);
            return this;
        }

        /**
         * 不包含查询
         *
         * @param inValues 所包含的值([a,b,c])
         * @return 条件操作工具
         */
        public Criteria<P> notIn(Collection inValues) {
            if (CollUtil.isEmpty(inValues)) {
                return this;
            }
            conditionPackages.addNotInQuery(fieldName, inValues);
            return this;
        }

        /**
         * 范围条件
         *
         * @param betweenMin 最小值
         * @param betweenMax 最大值
         * @return 条件操作工具
         */
        public Criteria<P> between(Object betweenMin, Object betweenMax) {
            if (betweenMin == null || betweenMax == null) {
                return this;
            }
            conditionPackages.addBetweenQuery(fieldName, betweenMin, betweenMax);
            return this;
        }

        /**
         * 排除范围条件
         *
         * @param betweenMin 最小值
         * @param betweenMax 最大值
         * @return 条件操作工具
         */
        public Criteria<P> notBetween(Object betweenMin, Object betweenMax) {
            if (betweenMin == null || betweenMax == null) {
                return this;
            }
            conditionPackages.addNotBetweenQuery(fieldName, betweenMin, betweenMax);
            return this;
        }

        /**
         * 值不为空条件
         *
         * @return 条件操作工具
         */
        public Criteria<P> notNull() {
            conditionPackages.addNotNullFieldsQuery(fieldName);
            return this;
        }

        /**
         * 值为空条件
         *
         * @return 条件操作工具
         */
        public Criteria<P> isNull() {
            conditionPackages.addNullFieldsQuery(fieldName);
            return this;
        }

        /**
         * 值大于等于条件
         *
         * @param value 值等于条件
         * @return 条件操作工具
         */
        public Criteria<P> greaterOrEqual(Object value) {
            if (value == null) {
                return this;
            }
            conditionPackages.addGreaterOrEqualFieldsQuery(fieldName, value);
            return this;
        }

        /**
         * 值小于等于条件
         *
         * @param value 值等于条件
         * @return 条件操作工具
         */
        public Criteria<P> lessOrEqual(Object value) {
            if (value == null) {
                return this;
            }
            conditionPackages.addLessOrEqualFieldsQuery(fieldName, value);
            return this;
        }

        /**
         * 值大于条件
         *
         * @param value 值等于条件
         * @return 条件操作工具
         */
        public Criteria<P> greater(Object value) {
            if (value == null) {
                return this;
            }
            conditionPackages.addGreaterFieldsQuery(fieldName, value);
            return this;
        }

        /**
         * 值小于条件
         *
         * @param value 值等于条件
         * @return 条件操作工具
         */
        public Criteria<P> less(Object value) {
            if (value == null) {
                return this;
            }
            conditionPackages.addLessFieldsQuery(fieldName, value);
            return this;
        }

        /**
         * 对后续条件使用OR封装
         *
         * @return 条件操作工具
         */
        public Criteria<P> or() {
            this.conditionPackages.setWay(FastCondition.Way.OR);
            return this;
        }


        /**
         * 查询特定字段
         *
         * @return 条件操作工具
         */
        public Criteria<P> showField() {
            conditionPackages.addShowField(fieldName);
            return this;
        }

        /**
         * 屏蔽特定字段
         *
         * @return 条件操作工具
         */
        public Criteria<P> hideField() {
            conditionPackages.addHideField(fieldName);
            return this;
        }

        /**
         * 字段求和
         *
         * @return 条件操作工具
         */
        public Criteria<P> sumField() {
            conditionPackages.addSumFields(fieldName);
            return this;
        }

        /**
         * 字段求平均值
         *
         * @return 条件操作工具
         */
        public Criteria<P> avgField() {
            conditionPackages.addAvgFields(fieldName);
            return this;
        }

        /**
         * 字段求平均值
         *
         * @return 条件操作工具
         */
        public Criteria<P> minField() {
            conditionPackages.addMinFields(fieldName);
            return this;
        }

        /**
         * 字段求平均值
         *
         * @return 条件操作工具
         */
        public Criteria<P> maxField() {
            conditionPackages.addMaxFields(fieldName);
            return this;
        }

        /**
         * 字段去重
         *
         * @return 条件操作工具
         */
        public Criteria<P> distinctField() {
            conditionPackages.addDistinctField(fieldName);
            return this;
        }

        /**
         * 排序-降序 查询时有用
         *
         * @return 条件操作工具
         */
        public Criteria<P> orderByDesc() {
            conditionPackages.addOrderByQuery(fieldName, true);
            return this;

        }

        /**
         * 排序-升序 查询时有用
         *
         * @return 条件操作工具
         */
        public Criteria<P> orderByAsc() {
            conditionPackages.addOrderByQuery(fieldName, false);
            return this;
        }

        /**
         * 自定义更新,可使用SQL函数,只有在更新时候生效
         * @return 查询封装
         */
        public CustomizeUpdate<P> customizeUpdateValue() {
            return new CustomizeUpdate<P>(pojoClass,fastExample,fieldName);
        }

        /**
         * 开始Dao操作
         *
         * @return Dao执行器
         */
        public FastDao<P> dao() {
            return FastDao.<P>init(pojoClass, fastExample);
        }

    }

}

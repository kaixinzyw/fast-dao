package com.fast.condition;

import java.util.Collection;
import java.util.Map;

/**
 * 条件封装
 */
public class FastCondition {

    /**
     * 条件类型
     */
    private Expression expression;
    /**
     * 条件拼接类型
     */
    private Way way;
    /**
     * 字段名
     */
    private String field;
    /**
     * 条件参数
     */
    private Object value;
    /**
     * 对象条件,对象中参数不为空的属性作为AND条件
     */
    private Object object;
    /**
     * 区间查询使用 区间最小值参数
     */
    private Object betweenMin;
    /**
     * 区间查询使用 区间最打值参数
     */
    private Object betweenMax;

    /**
     * 条件参数如有多个使用
     */
    private Collection<Object> valueList;

    /**
     * 自定义where sql语句,如果有占位符,使用#{参数名}进行描述 例:userName=${userName}
     */
    private String sql;

    /**
     * 占位符参数,如果使用占位符进行条件参数封装,必须传入条件参数 如上,需要使用Map put("userName","XXX")
     */
    private Map<String, Object> params;


    public static FastCondition equal(String field, Object value, Way way) {
        FastCondition conditions = new FastCondition();
        conditions.setExpression(Expression.Equal);
        conditions.setWay(way);
        conditions.setField(field);
        conditions.setValue(value);
        return conditions;
    }
    public static FastCondition notEqual(String field, Object value, Way way) {
        FastCondition conditions = new FastCondition();
        conditions.setExpression(Expression.NotEqual);
        conditions.setWay(way);
        conditions.setField(field);
        conditions.setValue(value);
        return conditions;
    }

    public static FastCondition like(String field, Object value, Way way) {
        FastCondition conditions = new FastCondition();
        conditions.setExpression(Expression.Like);
        conditions.setWay(way);
        conditions.setField(field);
        conditions.setValue(value);
        return conditions;
    }
    public static FastCondition notLike(String field, Object value, Way way) {
        FastCondition conditions = new FastCondition();
        conditions.setExpression(Expression.NotLike);
        conditions.setWay(way);
        conditions.setField(field);
        conditions.setValue(value);
        return conditions;
    }

    public static FastCondition in(String inName, Collection inValues, Way way) {
        FastCondition conditions = new FastCondition();
        conditions.setExpression(Expression.In);
        conditions.setWay(way);
        conditions.setField(inName);
        conditions.setValueList(inValues);
        return conditions;
    }
    public static FastCondition notIn(String inName, Collection inValues, Way way) {
        FastCondition conditions = new FastCondition();
        conditions.setExpression(Expression.NotIn);
        conditions.setWay(way);
        conditions.setField(inName);
        conditions.setValueList(inValues);
        return conditions;
    }

    public static FastCondition betweenQuery(String betweenName, Object betweenMin, Object betweenMax, Way way) {
        FastCondition conditions = new FastCondition();
        conditions.setExpression(Expression.Between);
        conditions.setWay(way);
        conditions.setField(betweenName);
        conditions.setBetweenMin(betweenMin);
        conditions.setBetweenMax(betweenMax);
        return conditions;
    }
    public static FastCondition notBetweenQuery(String betweenName, Object betweenMin, Object betweenMax, Way way) {
        FastCondition conditions = new FastCondition();
        conditions.setExpression(Expression.NotBetween);
        conditions.setWay(way);
        conditions.setField(betweenName);
        conditions.setBetweenMin(betweenMin);
        conditions.setBetweenMax(betweenMax);
        return conditions;
    }

    public static FastCondition isNullFields(String isNullField, Way way) {
        FastCondition conditions = new FastCondition();
        conditions.setExpression(Expression.Null);
        conditions.setWay(way);
        conditions.setField(isNullField);
        return conditions;
    }

    public static FastCondition notNullFields(String notNullField, Way way) {
        FastCondition conditions = new FastCondition();
        conditions.setExpression(Expression.NotNull);
        conditions.setWay(way);
        conditions.setField(notNullField);
        return conditions;
    }

    public static FastCondition greater(String field, Object value, Way way) {
        FastCondition conditions = new FastCondition();
        conditions.setExpression(Expression.Greater);
        conditions.setWay(way);
        conditions.setField(field);
        conditions.setValue(value);
        return conditions;
    }

    public static FastCondition greaterOrEqual(String field, Object value, Way way) {
        FastCondition conditions = new FastCondition();
        conditions.setExpression(Expression.GreaterOrEqual);
        conditions.setWay(way);
        conditions.setField(field);
        conditions.setValue(value);
        return conditions;
    }

    public static FastCondition less(String field, Object value, Way way) {
        FastCondition conditions = new FastCondition();
        conditions.setExpression(Expression.Less);
        conditions.setWay(way);
        conditions.setField(field);
        conditions.setValue(value);
        return conditions;
    }

    public static FastCondition lessOrEqual(String field, Object value, Way way) {
        FastCondition conditions = new FastCondition();
        conditions.setExpression(Expression.LessOrEqual);
        conditions.setWay(way);
        conditions.setField(field);
        conditions.setValue(value);
        return conditions;
    }


    public static FastCondition equalObject(Object object) {
        FastCondition conditions = new FastCondition();
        conditions.setWay(Way.AND);
        conditions.setExpression(Expression.Obj);
        conditions.setObject(object);
        return conditions;
    }

    public static FastCondition sql(String sql, Map<String, Object> params, Way way) {
        FastCondition conditions = new FastCondition();
        conditions.setExpression(Expression.SQL);
        conditions.setWay(way);
        conditions.setSql(sql);
        conditions.setParams(params);
        return conditions;
    }

    public enum Way {
        /**
         * 条件方式
         */
        OR("or", "OR "),
        AND("and", "AND ");
        public String name;
        public String expression;

        Way(String name, String expression) {
            this.name = name;
            this.expression = expression;
        }
    }

    public enum Expression {
        /**
         * 条件表达式
         */
        Equal("equal", " = "),
        NotEqual("notEqual", " != "),
        Like("like", " LIKE "),
        NotLike("notLike", " NOT LIKE "),
        In("in", " IN "),
        NotIn("notIn", " NOT IN "),
        Between("between", " BETWEEN "),
        NotBetween("notBetween", " NOT BETWEEN "),
        Null("null", " IS NULL "),
        NotNull("notNull", " IS NOT NULL "),
        Greater("greater", " > "),
        GreaterOrEqual("greaterOrEqual", " >= "),
        Less("less", " < "),
        LessOrEqual("lessOrEqual", " <= "),

        SQL("sql", "自定义SQL添加"),
        Obj("object", " = ");



        public String name;
        public String expression;

        Expression(String name, String expression) {
            this.name = name;
            this.expression = expression;
        }
    }

    public Expression getExpression() {
        return expression;
    }

    public void setExpression(Expression expression) {
        this.expression = expression;
    }

    public Way getWay() {
        return way;
    }

    public void setWay(Way way) {
        this.way = way;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }

    public Object getBetweenMin() {
        return betweenMin;
    }

    public void setBetweenMin(Object betweenMin) {
        this.betweenMin = betweenMin;
    }

    public Object getBetweenMax() {
        return betweenMax;
    }

    public void setBetweenMax(Object betweenMax) {
        this.betweenMax = betweenMax;
    }

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    public Collection<Object> getValueList() {
        return valueList;
    }

    public void setValueList(Collection<Object> valueList) {
        this.valueList = valueList;
    }

    public Map<String, Object> getParams() {
        return params;
    }

    public void setParams(Map<String, Object> params) {
        this.params = params;
    }
}

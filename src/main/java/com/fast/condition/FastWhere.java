package com.fast.condition;

import java.lang.annotation.Retention;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * @author kaixi
 */
@Retention(RUNTIME)
public @interface FastWhere {

    WhereCondition condition() default WhereCondition.Equal;
    WhereWay way() default WhereWay.AND;
    String sql() default "";
    String fieldName() default "";
    enum WhereWay {
        /**
         * 条件方式
         */
        OR("or", "OR "),
        AND("and", "AND ");
        public String name;
        public String expression;

        WhereWay(String name, String expression) {
            this.name = name;
            this.expression = expression;
        }
    }

    enum WhereCondition{
        /**
         * 条件
         */
        Equal("equal", " = "),
        NotEqual("notEqual", " != "),
        Like("like", " LIKE "),
        NotLike("notLike", " NOT LIKE "),
        Greater("greater", " > "),
        GreaterOrEqual("greaterOrEqual", " >= "),
        Less("less", " < "),
        LessOrEqual("lessOrEqual", " <= "),
        SQL("SQL", "自定义SQL");

        public String name;
        public String expression;

        WhereCondition(String name, String expression) {
            this.name = name;
            this.expression = expression;
        }

    }
}

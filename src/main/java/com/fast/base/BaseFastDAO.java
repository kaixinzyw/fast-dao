package com.fast.base;

import cn.hutool.core.util.ClassUtil;
import com.fast.condition.FastExample;
import com.fast.fast.FastDao;

import java.io.Serializable;
import java.util.Map;

/**
 * 查询模板公共方法,每个查询模板都应该继承此方法
 *
 * @param <T> 需要操作的对象泛型
 * @author 张亚伟 https://github.com/kaixinzyw
 */
public class BaseFastDAO<T> implements Serializable {

    private static final long serialVersionUID = 5126897802718795886L;

    /**
     * 条件封装器
     */
    protected FastExample<T> fastExample;

    protected BaseFastDAO() {
        fastExample = new FastExample<>((Class<T>) ClassUtil.getTypeArgument(this.getClass()));
    }

    public FastDao<T> dao() {
        return fastExample.dao();
    }

    /**
     * 对象有参属性匹配
     *
     * @param o 传入对象中参数不为空的属性会作为AND条件
     */
    public void equalObject(Object o) {
        fastExample.equalObject(o);
    }

    /**
     * 自定义查询列
     *
     * @param queryColumn SELECT查询时自定义列
     */
    public void customQueryColumn(String queryColumn) {
        fastExample.customQueryColumn(queryColumn);
    }

    /**
     * 自定义sql条件,会将传入的参数进行AND条件进行拼接
     *
     * @param sql    自定义sql语句,如果有占位符,使用#{参数名}进行描述 例:userName=${userName}
     * @param params 占位符参数,如果使用占位符进行条件参数封装,必须传入条件参数 如上,需要使用Map put("userName","XXX")
     */
    public void andSql(String sql, Map<String, Object> params) {
        fastExample.andSql(sql, params);
    }

    /**
     * 自定义sql条件,WHERE后拼接
     *
     * @param sql    自定义sql语句,如果有占位符,使用#{参数名}进行描述 例:userName=${userName}
     * @param params 占位符参数,如果使用占位符进行条件参数封装,必须传入条件参数 如上,需要使用Map put("userName","XXX")
     */
    public void sql(String sql, Map<String, Object> params) {
        fastExample.sql(sql, params);
    }

    /**
     * 自定义字段的操作
     *
     * @param fieldName 字段名称
     * @return 操作对象
     */
    public FastExample.Criteria<T> customFieldOperation(String fieldName) {
        return fastExample.field(fieldName);
    }

    /**
     * 自定义sql条件,会将传入的参数进行OR条件进行拼接
     *
     * @param sql    自定义sql语句,如果有占位符,使用#{参数名}进行描述 例:userName=${userName}
     * @param params 占位符参数,如果使用占位符进行条件参数封装,必须传入条件参数 如上,需要使用Map put("userName","XXX")
     */
    public void orSQL(String sql, Map<String, Object> params) {
        fastExample.orSql(sql, params);
    }


    /**
     * 左括号
     * @return 条件操作工具
     */
    public void OrLeftBracket() {
        fastExample.OrLeftBracket();
    }

    /**
     * 左括号
     * @return 条件操作工具
     */
    public void AndLeftBracket() {
        fastExample.AndLeftBracket();
    }

    /**
     * 右括号
     * @return 条件操作工具
     */
    public void rightBracket() {
        fastExample.rightBracket();
    }

    /**
     * 关闭逻辑删除条件保护,如果开启了逻辑删除功能,需要进行删除数据的操作,需要使用此方法进行关闭逻辑条件过滤
     */
    public void closeLogicDeleteProtect() {
        fastExample.closeLogicDeleteProtect();
    }

    public void openRelatedQuery() {
        fastExample.openRelatedQuery();
    }

}

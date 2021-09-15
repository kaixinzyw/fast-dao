package com.fast.base;

import cn.hutool.core.util.ObjectUtil;
import com.fast.condition.FastExample;
import com.fast.fast.FastDao;
import com.fast.fast.JoinFastDao;

import java.io.Serializable;
import java.util.Map;

/**
 * 查询模板公共方法,每个查询模板都应该继承此方法
 *
 * @param <T> 需要操作的对象泛型
 * @author 张亚伟 https://github.com/kaixinzyw
 */
public class BaseFastDAO<P,T> implements Serializable {

    private static final long serialVersionUID = 5126897802718795886L;
    private JoinFastDao<P> joinFastDao;

    /**
     * 条件封装器
     */
    protected FastExample<P,T> fastExample;

    protected BaseFastDAO() {
    }

    public FastDao<P> dao() {
        return fastExample.dao();
    }


    public <P2,T2>JoinFastDao<P> leftJoin(BaseFastDAO<P2,T2> fastDAO,FastExample.FieldCriteria<P2,T2> leftCondition, FastExample.FieldCriteria<P,T> rightCondition){
        getJoinFastDao().leftJoinNotQuery(fastDAO).on(leftCondition,rightCondition);
        if (fastExample.conditionPackages().getJoinFastDao() == null) {
            fastExample.conditionPackages().setJoinFastDao(getJoinFastDao());
        }
        return getJoinFastDao();
    }
    public <P2,T2>JoinFastDao<P> rightJoin(BaseFastDAO<P2,T2> fastDAO,FastExample.FieldCriteria<P2,T2> leftCondition, FastExample.FieldCriteria<P,T> rightCondition){
        getJoinFastDao().rightJoinNotQuery(fastDAO).on(leftCondition,rightCondition);
        if (fastExample.conditionPackages().getJoinFastDao() == null) {
            fastExample.conditionPackages().setJoinFastDao(getJoinFastDao());
        }
        return getJoinFastDao();
    }
    private JoinFastDao<P> getJoinFastDao(){
        return joinFastDao==null?joinFastDao = (JoinFastDao<P>) JoinFastDao.create(ObjectUtil.getTypeArgument(this),this):joinFastDao;
    }


    /**
     * 对象有参属性匹配
     *
     * @param o 传入对象中参数不为空的属性会作为AND条件
     * @return {@link T}
     */
    public T equalObject(Object o) {
        return fastExample.global().equalObject(o);
    }


    /**
     * 自定义sql条件,会将传入的参数进行AND条件进行拼接
     *
     * @param sql    自定义sql语句,如果有占位符,使用#{参数名}进行描述 例:userName=${userName}
     * @param params 占位符参数,如果使用占位符进行条件参数封装,必须传入条件参数 如上,需要使用Map put("userName","XXX")
     * @return {@link T}
     */
    public T andSql(String sql, Map<String, Object> params) {
        return fastExample.global().andSql(sql, params);
    }

    /**
     * 自定义sql条件,WHERE后拼接
     *
     * @param sql    自定义sql语句,如果有占位符,使用#{参数名}进行描述 例:userName=${userName}
     * @param params 占位符参数,如果使用占位符进行条件参数封装,必须传入条件参数 如上,需要使用Map put("userName","XXX")
     * @return {@link T}
     */
    public T sql(String sql, Map<String, Object> params) {
        return fastExample.global().sql(sql, params);
    }

    /**
     * 自定义字段的操作
     *
     * @param fieldName 字段名称
     * @return 操作对象
     */
    public FastExample.FieldCriteria<P, T> customFieldOperation(String fieldName) {
        return fastExample.field(fieldName);
    }

    /**
     * 自定义sql条件,会将传入的参数进行OR条件进行拼接
     *
     * @param sql    自定义sql语句,如果有占位符,使用#{参数名}进行描述 例:userName=${userName}
     * @param params 占位符参数,如果使用占位符进行条件参数封装,必须传入条件参数 如上,需要使用Map put("userName","XXX")
     * @return {@link T}
     */
    public T orSQL(String sql, Map<String, Object> params) {
        return fastExample.global().orSql(sql, params);
    }


    /**
     * 左括号
     *
     * @return {@link T}
     */
    public T orLeftBracket() {
        return fastExample.global().orLeftBracket();
    }

    /**
     * 左括号
     *
     * @return {@link T}
     */
    public T andLeftBracket() {
        return fastExample.global().andLeftBracket();
    }

    /**
     * 右括号
     *
     * @return {@link T}
     */
    public T rightBracket() {
        return fastExample.global().rightBracket();
    }

    /**
     * 关闭逻辑删除条件保护,如果开启了逻辑删除功能,需要进行删除数据的操作,需要使用此方法进行关闭逻辑条件过滤
     *
     * @return {@link T}
     */
    public T closeLogicDeleteProtect() {
        return fastExample.global().closeLogicDeleteProtect();
    }

    /**
     * 打开相关查询
     *
     * @return {@link T}
     */
    public T openRelatedQuery() {
        return fastExample.global().openRelatedQuery();
    }

    public FastExample<P, T> fastExample(){
        return fastExample;
    }
}

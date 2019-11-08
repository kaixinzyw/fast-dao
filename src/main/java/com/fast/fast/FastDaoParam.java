package com.fast.fast;

import cn.hutool.core.util.StrUtil;
import com.fast.condition.FastExample;
import com.fast.mapper.TableMapper;
import io.netty.util.concurrent.FastThreadLocal;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ORM实现参数封装类
 *
 * @author 张亚伟 https://github.com/kaixinzyw
 */
public class FastDaoParam<T> {

    /**
     * 操作对象的属性映射
     */
    private TableMapper<T> tableMapper;
    /**
     * SQL条件封装
     */
    private FastExample<T> fastExample;
    /**
     * SQL执行时间
     */
    private Long sqlTime;
    /**
     * 操作所用到的对象信息
     */
    private T update;

    /**
     * 操作所用到的对象集合信息
     */
    private List<T> insertList;

    /**
     * 返回结果
     */
    private Object returnVal;

    /**
     * 拼接后的SQL语句
     */
    private String sql;
    /**
     * SQL参数
     */
    private Map<String, Object> paramMap;
    /**
     * 更新操作是否对不进行参数为null的字段进行操作
     */
    private Boolean updateSelective;
    /**
     * 是否为逻辑删除
     */
    private Boolean logicDelete;

    private FastDaoParam() {
    }

    /**
     * ORM实现参数封装类初始化
     * 如果使用自定义SQL,会直接进行SQL语句的封装,不进行模板条件拼接
     *
     * @param <T> 操作类
     * @return 初始化结果
     */
    private static final FastThreadLocal<FastDaoParam> fastDaoParamThreadLocal = new FastThreadLocal<>();

    public static <T> FastDaoParam<T> init(TableMapper<T> mapper, FastExample<T> example) {
        FastDaoParam<T> daoParam = fastDaoParamThreadLocal.get();
        if (daoParam == null) {
            daoParam = new FastDaoParam<>();
            fastDaoParamThreadLocal.set(daoParam);
        }

        daoParam.update = null;
        daoParam.insertList = null;
        daoParam.returnVal = null;
        daoParam.logicDelete = Boolean.FALSE;
        daoParam.updateSelective = Boolean.FALSE;

        daoParam.tableMapper = mapper;
        daoParam.fastExample = example;
        if (StrUtil.isNotEmpty(daoParam.fastExample.conditionPackages().getCustomSql())) {
            daoParam.sql = daoParam.fastExample.conditionPackages().getCustomSql();
            daoParam.paramMap = daoParam.fastExample.conditionPackages().getCustomSqlParams();
            return daoParam;
        }
        daoParam.sql = null;
        daoParam.paramMap = new HashMap<>();

        return daoParam;
    }

    public static <T> FastDaoParam<T> get() {
        return fastDaoParamThreadLocal.get();
    }

    public TableMapper<T> getTableMapper() {
        return tableMapper;
    }

    public T getUpdate() {
        return update;
    }

    public void setUpdate(T update) {
        this.update = update;
    }

    public List<T> getInsertList() {
        return insertList;
    }

    public void setInsertList(List<T> insertList) {
        this.insertList = insertList;
    }


    public Map<String, Object> getParamMap() {
        return paramMap;
    }

    public void setParamMap(Map<String, Object> paramMap) {
        this.paramMap = paramMap;
    }

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    public Long getSqlTime() {
        return sqlTime;
    }

    public void setSqlTime(Long sqlTime) {
        this.sqlTime = sqlTime;
    }


    public void setTableMapper(TableMapper<T> tableMapper) {
        this.tableMapper = tableMapper;
    }

    public FastExample<T> getFastExample() {
        return fastExample;
    }

    public void setFastExample(FastExample<T> fastExample) {
        this.fastExample = fastExample;
    }

    public Object getReturnVal() {
        return returnVal;
    }

    public void setReturnVal(Object returnVal) {
        this.returnVal = returnVal;
    }

    public Boolean getUpdateSelective() {
        return updateSelective;
    }

    public void setUpdateSelective(Boolean updateSelective) {
        this.updateSelective = updateSelective;
    }

    public Boolean getLogicDelete() {
        return logicDelete;
    }

    public void setLogicDelete(Boolean logicDelete) {
        this.logicDelete = logicDelete;
    }
}

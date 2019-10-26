package com.fast.db.template.template;

import cn.hutool.core.util.StrUtil;
import com.fast.db.template.mapper.FastMapperUtil;
import com.fast.db.template.mapper.TableMapper;

import java.util.HashMap;
import java.util.Map;

/**
 * ORM实现参数封装类
 *
 * @author 张亚伟 https://github.com/kaixinzyw/fast-db-template
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
     * Dao调用的开始时间
     */
    private Long sqlStartTime;
    /**
     * 操作所用到的对象信息
     */
    private T pojo;
    /**
     * 主键信息
     */
    private Object primaryKey;
    /**
     * 主键值
     */
    private Long primaryKeyValue;
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
    private Boolean selective;
    /**
     * 查询条数信息及分页信息
     */
    private Integer limit;
    private Integer page;
    private Integer size;

    /**
     * ORM实现参数封装类初始化
     * 如果使用自定义SQL,会直接进行SQL语句的封装,不进行模板条件拼接
     * @param <T> 操作类
     * @return 初始化结果
     */
    public static <T> FastDaoParam<T> init() {
        FastMapperUtil<T> dataUtil = FastMapperUtil.get();
        FastDaoParam<T> daoParam = dataUtil.getDaoParam();
        daoParam.sqlStartTime = System.currentTimeMillis();
        daoParam.tableMapper = dataUtil.getTableMapper();
        daoParam.fastExample = dataUtil.getFastExample();
        if (StrUtil.isNotEmpty(daoParam.fastExample.conditionPackages().getCustomSql())) {
            daoParam.sql = daoParam.fastExample.conditionPackages().getCustomSql();
            daoParam.paramMap = daoParam.fastExample.conditionPackages().getCustomSqlParams();
            return daoParam;
        }
        daoParam.sql = null;
        daoParam.pojo = null;
        daoParam.primaryKey = null;
        daoParam.paramMap = new HashMap<>();
        daoParam.limit = null;
        daoParam.page = null;
        daoParam.size = null;
        daoParam.selective = Boolean.FALSE;
        return daoParam;
    }

    public TableMapper<T> getTableMapper() {
        return tableMapper;
    }

    public T getPojo() {
        return pojo;
    }

    public void setPojo(T pojo) {
        this.pojo = pojo;
    }

    public Object getPrimaryKey() {
        return primaryKey;
    }

    public void setPrimaryKey(Object primaryKey) {
        this.primaryKey = primaryKey;
    }

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
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

    public Boolean getSelective() {
        return selective;
    }

    public void setSelective(Boolean selective) {
        this.selective = selective;
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

    public Long getSqlStartTime() {
        return sqlStartTime;
    }

    public Long getPrimaryKeyValue() {
        return primaryKeyValue;
    }

    public void setPrimaryKeyValue(Long primaryKeyValue) {
        this.primaryKeyValue = primaryKeyValue;
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
}

package com.fast.db.template.implement.mybatis;

import com.fast.db.template.mapper.FastClassTableMapper;
import com.fast.db.template.template.CompoundQuery;

import java.util.ArrayList;
import java.util.List;

/**
 * MyBatis参数封装类
 *
 * @author 张亚伟 398850094@qq.com
 */
public class FastMybatisParam {
    private FastClassTableMapper fastClassTableMapper;
    private Object pojo;
    private Object primaryKey;
    private CompoundQuery compoundQuery;
    private List<Object> paramList;
    private Integer page;
    private Integer size;
    private Boolean selective;


    public FastMybatisParam() {
    }

    public FastMybatisParam(FastClassTableMapper fastClassTableMapper) {
        this.fastClassTableMapper = fastClassTableMapper;
    }

    private void init() {
        this.pojo = null;
        this.primaryKey = null;
        this.compoundQuery = null;
        this.paramList = new ArrayList<>();
        this.page = null;
        this.size = null;
        this.selective = Boolean.FALSE;
    }

    public FastClassTableMapper getFastClassTableMapper() {
        return fastClassTableMapper;
    }

    public void setFastClassTableMapper(FastClassTableMapper fastClassTableMapper) {
        init();
        this.fastClassTableMapper = fastClassTableMapper;
    }

    public Object getPojo() {
        return pojo;
    }

    public void setPojo(Object pojo) {
        this.pojo = pojo;
    }

    public Object getPrimaryKey() {
        return primaryKey;
    }

    public void setPrimaryKey(Object primaryKey) {
        this.primaryKey = primaryKey;
    }

    public CompoundQuery getCompoundQuery() {
        return compoundQuery;
    }

    public void setCompoundQuery(CompoundQuery compoundQuery) {
        this.compoundQuery = compoundQuery;
    }

    public List<Object> getParamList() {
        return paramList;
    }

    public void setParamList(List<Object> paramList) {
        this.paramList = paramList;
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
}

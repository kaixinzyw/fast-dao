package com.fast.dao.many;

import com.fast.mapper.TableMapper;

/**
 * 多对多操作信息表
 *
 * @author zyw
 */
public class ManyToManyInfo {

    /**
     * 数据存放字段
     */
    private String dataFieldName;
    
    /**
     * 目标映射
     */
    private TableMapper joinMapper;

    /**
     * 目标表映射列名
     */
    private String joinMapperFieldName;

    /**
     * 关系表映射
     */
    private TableMapper relationMapper;

    /**
     * 关系表映射列名
     */
    private String relationMapperFieldName;

    public TableMapper getJoinMapper() {
        return joinMapper;
    }

    public void setJoinMapper(TableMapper joinMapper) {
        this.joinMapper = joinMapper;
    }

    public String getJoinMapperFieldName() {
        return joinMapperFieldName;
    }

    public void setJoinMapperFieldName(String joinMapperFieldName) {
        this.joinMapperFieldName = joinMapperFieldName;
    }

    public TableMapper getRelationMapper() {
        return relationMapper;
    }

    public void setRelationMapper(TableMapper relationMapper) {
        this.relationMapper = relationMapper;
    }

    public String getRelationMapperFieldName() {
        return relationMapperFieldName;
    }

    public void setRelationMapperFieldName(String relationMapperFieldName) {
        this.relationMapperFieldName = relationMapperFieldName;
    }

    public String getDataFieldName() {
        return dataFieldName;
    }

    public void setDataFieldName(String dataFieldName) {
        this.dataFieldName = dataFieldName;
    }
}

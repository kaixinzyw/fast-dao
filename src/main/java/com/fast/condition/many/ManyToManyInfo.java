package com.fast.condition.many;

import com.fast.mapper.TableMapper;

/**
 * 多对多操作信息表
 *
 * @author zyw
 * @date 2021/01/13
 */
public class ManyToManyInfo {

    private String dataFieldName;
    
    /**
     * 目标映射
     */
    private TableMapper joinMapper;

    /**
     * 目标表映射列名
     */
    private String joinMapperColumnName;

    /**
     * 关系表映射
     */
    private TableMapper relationMapper;

    /**
     * 关系表映射列名
     */
    private String relationMapperColumnName;

    public TableMapper getJoinMapper() {
        return joinMapper;
    }

    public void setJoinMapper(TableMapper joinMapper) {
        this.joinMapper = joinMapper;
    }

    public String getJoinMapperColumnName() {
        return joinMapperColumnName;
    }

    public void setJoinMapperColumnName(String joinMapperColumnName) {
        this.joinMapperColumnName = joinMapperColumnName;
    }

    public TableMapper getRelationMapper() {
        return relationMapper;
    }

    public void setRelationMapper(TableMapper relationMapper) {
        this.relationMapper = relationMapper;
    }

    public String getRelationMapperColumnName() {
        return relationMapperColumnName;
    }

    public void setRelationMapperColumnName(String relationMapperColumnName) {
        this.relationMapperColumnName = relationMapperColumnName;
    }

    public String getDataFieldName() {
        return dataFieldName;
    }

    public void setDataFieldName(String dataFieldName) {
        this.dataFieldName = dataFieldName;
    }
}

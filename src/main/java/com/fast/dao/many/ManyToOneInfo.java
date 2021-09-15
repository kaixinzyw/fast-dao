package com.fast.dao.many;

import com.fast.mapper.TableMapper;

/**
 * 多对多操作信息表
 *
 * @author zyw
 */
public class ManyToOneInfo {

    /**
     * 数据存放字段
     */
    private String dataFieldName;
    
    /**
     * 目标映射
     */
    private TableMapper joinMapper;

    /**
     * 目标表映射字段名
     */
    private String joinMapperFieldName;


    public String getDataFieldName() {
        return dataFieldName;
    }

    public void setDataFieldName(String dataFieldName) {
        this.dataFieldName = dataFieldName;
    }

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
}

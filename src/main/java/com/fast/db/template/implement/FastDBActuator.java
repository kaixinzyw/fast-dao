package com.fast.db.template.implement;

import com.fast.db.template.mapper.FastClassTableMapper;
import com.fast.db.template.template.CompoundQuery;

import java.util.List;
/**
 * FastDB 执行器接口
 * @author 张亚伟 398850094@qq.com
 */
public abstract class FastDBActuator<Pojo> {
    protected FastClassTableMapper fastClassTableMapper;
    public void setFastClassTableMapper(FastClassTableMapper fastClassTableMapper){
        this.fastClassTableMapper = fastClassTableMapper;
    }
    public abstract Integer insert(Pojo pojo);
    public abstract Pojo findByPrimaryKey(Object primaryKey);
    public abstract Pojo findOne(CompoundQuery compoundQuery);
    public abstract List<Pojo> findAll(CompoundQuery compoundQuery);
    public abstract Integer findCount(CompoundQuery compoundQuery);
    public abstract Integer update(Pojo pojo, CompoundQuery compoundQuery, boolean isSelective);
    public abstract Integer updateByPrimaryKey(Pojo pojo, boolean isSelective);
    public abstract Integer delete(CompoundQuery compoundQuery);
    public abstract Integer deleteByPrimaryKey(Object primaryKey);
}

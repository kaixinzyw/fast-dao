package com.fast.db.template.implement.mybatis;

import cn.hutool.core.collection.CollUtil;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.fast.db.template.implement.FastDBActuator;
import com.fast.db.template.template.CompoundQuery;

import java.util.List;
import java.util.Map;

/**
 * 扩展MyBatis FastDB执行器实现
 *
 * @author 张亚伟 398850094@qq.com
 */
public class FastMyBatisImpl<Pojo> extends FastDBActuator<Pojo> {
    private FastMybatisParam param = new FastMybatisParam();

    @Override
    public Integer insert(Pojo pojo) {
        param.setFastClassTableMapper(fastClassTableMapper);
        param.setPojo(pojo);
        Integer insert = FastMyBatisUtil.getMapper().insert(param);
        return insert;
    }

    @Override
    public Pojo findByPrimaryKey(Object primaryKey) {
        param.setFastClassTableMapper(fastClassTableMapper);
        param.setPrimaryKey(primaryKey);
        Map<String, Object> pojoMap = FastMyBatisUtil.getMapper().findByPrimaryKey(param);
        if (CollUtil.isEmpty(pojoMap)) {
            return null;
        }
        return (Pojo) new JSONObject(pojoMap).toJavaObject(fastClassTableMapper.getObjClass());
    }

    @Override
    public Pojo findOne(CompoundQuery compoundQuery) {
        param.setFastClassTableMapper(fastClassTableMapper);
        param.setCompoundQuery(compoundQuery);
        Map<String, Object> pojoMap = FastMyBatisUtil.getMapper().findOne(param);
        if (CollUtil.isEmpty(pojoMap)) {
            return null;
        }
        return (Pojo) new JSONObject(pojoMap).toJavaObject(fastClassTableMapper.getObjClass());
    }


    @Override
    public List<Pojo> findAll(CompoundQuery compoundQuery) {
        param.setFastClassTableMapper(fastClassTableMapper);
        param.setCompoundQuery(compoundQuery);
        List<Map<String, Object>> pojoMap = FastMyBatisUtil.getMapper().findAll(param);
        if (CollUtil.isEmpty(pojoMap)) {
            return null;
        }
        List<Pojo> pojos = JSONObject.parseObject(JSONObject.toJSONString(pojoMap), new TypeReference<List<Pojo>>() {
        });
        return pojos;
    }

    @Override
    public Integer findCount(CompoundQuery compoundQuery) {
        param.setFastClassTableMapper(fastClassTableMapper);
        param.setCompoundQuery(compoundQuery);
        return FastMyBatisUtil.getMapper().findCount(param);
    }


    @Override
    public Integer update(Pojo pojo, CompoundQuery compoundQuery, boolean isSelective) {
        param.setFastClassTableMapper(fastClassTableMapper);
        param.setCompoundQuery(compoundQuery);
        param.setPojo(pojo);
        param.setSelective(isSelective);
        return FastMyBatisUtil.getMapper().update(param);
    }

    @Override
    public Integer updateByPrimaryKey(Pojo pojo, boolean isSelective) {
        param.setFastClassTableMapper(fastClassTableMapper);
        param.setPojo(pojo);
        param.setSelective(isSelective);
        return FastMyBatisUtil.getMapper().updateByPrimaryKey(param);
    }

    @Override
    public Integer delete(CompoundQuery compoundQuery) {
        param.setFastClassTableMapper(fastClassTableMapper);
        param.setCompoundQuery(compoundQuery);
        return FastMyBatisUtil.getMapper().delete(param);
    }

    @Override
    public Integer deleteByPrimaryKey(Object primaryKey) {
        param.setFastClassTableMapper(fastClassTableMapper);
        param.setPrimaryKey(primaryKey);
        return FastMyBatisUtil.getMapper().deleteByPrimaryKey(param);
    }
}

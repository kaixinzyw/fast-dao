package com.fast.dao.mybatis;

import cn.hutool.core.collection.CollUtil;
import com.alibaba.fastjson.JSONObject;
import com.fast.config.PrimaryKeyType;
import com.fast.dao.DaoActuator;
import com.fast.dao.utils.FastSqlUtil;
import com.fast.fast.FastDaoParam;
import com.fast.utils.FastValueUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 扩展MyBatis FastDB执行器实现
 *
 * @author 张亚伟 https://github.com/kaixinzyw
 */
public class FastMyBatisImpl<T> implements DaoActuator<T> {

    @Override
    public Integer insert() {
        FastDaoParam<T> param = FastDaoParam.get();
        Integer insertCount;
        if (param.getTableMapper().getPrimaryKeyType().equals(PrimaryKeyType.AUTO)) {
            insertCount = FastMyBatisConnection.getMapper().insertPrimaryKeyAuto(param);
            if (insertCount > 0) {
                FastValueUtil.setPrimaryKey(param.getInsert(), param.getPrimaryKeyValue(), param.getTableMapper());
            }
        } else {
            insertCount = FastMyBatisConnection.getMapper().insert(param);
        }
        return insertCount;
    }

    @Override
    public Integer insertList() {
        FastDaoParam<T> param = FastDaoParam.get();
        return FastMyBatisConnection.getMapper().insert(param);
    }


    @Override
    public List<T> findAll() {
        FastDaoParam<T> param = FastDaoParam.get();
        List<Map<String, Object>> pojoMap = FastMyBatisConnection.getMapper().findAll(param);
        if (CollUtil.isEmpty(pojoMap)) {
            FastSqlUtil.printSql(param, new ArrayList<>());
            return new ArrayList<>();
        }
        return JSONObject.parseArray(JSONObject.toJSONString(pojoMap), param.getTableMapper().getObjClass());
    }

    @Override
    public Integer findCount() {
        return FastMyBatisConnection.getMapper().findCount(FastDaoParam.get());
    }


    @Override
    public Integer update() {
        return FastMyBatisConnection.getMapper().update(FastDaoParam.get());
    }


    @Override
    public Integer delete() {
        return FastMyBatisConnection.getMapper().delete(FastDaoParam.get());
    }

}

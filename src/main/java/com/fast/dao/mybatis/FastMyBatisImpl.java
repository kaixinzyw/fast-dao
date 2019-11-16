package com.fast.dao.mybatis;

import cn.hutool.core.collection.CollUtil;
import com.alibaba.fastjson.JSONObject;
import com.fast.config.PrimaryKeyType;
import com.fast.dao.DaoActuator;
import com.fast.dao.utils.*;
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
    public List<T> insert() {
        FastDaoParam<T> param = FastDaoParam.get();
        FastInsertProvider.insert(param);
        if (CollUtil.isNotEmpty(param.getInsertList()) && param.getTableMapper().getPrimaryKeyType() != null && param.getTableMapper().getPrimaryKeyType().equals(PrimaryKeyType.AUTO) && param.getInsertList().size() == 1) {
            FastMyBatisConnection.getMapper().insertPrimaryKeyAuto(param);
            FastValueUtil.setPrimaryKey(param.getInsertList().get(0), param.getReturnVal(), param.getTableMapper());
        } else {
            FastMyBatisConnection.getMapper().insert(param);
        }
        return param.getInsertList();
    }


    @Override
    public List<T> select() {
        FastDaoParam<T> param = FastDaoParam.get();
        FastSelectProvider.findAll(param);
        List<Map<String, Object>> pojoMap = FastMyBatisConnection.getMapper().findAll(param);
        if (CollUtil.isEmpty(pojoMap)) {
            return new ArrayList<>();
        }
        return JSONObject.parseArray(JSONObject.toJSONString(pojoMap), param.getTableMapper().getObjClass());
    }

    @Override
    public Integer count() {
        FastDaoParam<T> param = FastDaoParam.get();
        FastSelectProvider.findCount(param);
        return FastMyBatisConnection.getMapper().findCount(FastDaoParam.get());
    }


    @Override
    public Integer update() {
        FastDaoParam<T> param = FastDaoParam.get();
        FastUpdateProvider.update(param);
        return FastMyBatisConnection.getMapper().update(FastDaoParam.get());
    }


    @Override
    public Integer delete() {
        FastDaoParam<T> param = FastDaoParam.get();
        FastDeleteProvider.delete(param);
        return FastMyBatisConnection.getMapper().delete(FastDaoParam.get());
    }

}

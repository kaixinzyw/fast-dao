package com.fast.dao.mybatis;

import cn.hutool.core.collection.CollUtil;
import com.alibaba.fastjson.JSONObject;
import com.fast.config.PrimaryKeyType;
import com.fast.dao.DaoActuator;
import com.fast.dao.utils.FastDeleteProvider;
import com.fast.dao.utils.FastInsertProvider;
import com.fast.dao.utils.FastSelectProvider;
import com.fast.dao.utils.FastUpdateProvider;
import com.fast.fast.FastDaoParam;
import com.fast.utils.FastValueUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 扩展MyBatis FastDB执行器实现
 *
 * MyBatis插件模组
 *
 * @author 张亚伟 https://github.com/kaixinzyw
 */
public class MyBatisImpl<T> implements DaoActuator<T> {

    @Override
    public List<T> insert() {
        FastDaoParam<T> param = FastDaoParam.get();
        FastInsertProvider.insert(param);
        if (CollUtil.isNotEmpty(param.getInsertList()) && param.getTableMapper().getPrimaryKeyType() != null && param.getTableMapper().getPrimaryKeyType().equals(PrimaryKeyType.AUTO) && param.getInsertList().size() == 1) {
            MyBatisConnection.getMapper().insertPrimaryKeyAuto(param);
            FastValueUtil.setPrimaryKey(param.getInsertList().get(0), param.getReturnVal(), param.getTableMapper());
        } else {
            MyBatisConnection.getMapper().insert(param);
        }
        return param.getInsertList();
    }


    @Override
    public List<T> select() {
        FastDaoParam<T> param = FastDaoParam.get();
        FastSelectProvider.findAll(param);
        List<Map<String, Object>> pojoMap = MyBatisConnection.getMapper().findAll(param);
        if (CollUtil.isEmpty(pojoMap)) {
            return new ArrayList<>();
        }
        return JSONObject.parseArray(JSONObject.toJSONString(pojoMap), param.getTableMapper().getObjClass());
    }

    @Override
    public Integer count() {
        FastDaoParam<T> param = FastDaoParam.get();
        FastSelectProvider.findCount(param);
        return MyBatisConnection.getMapper().findCount(param);
    }


    @Override
    public Integer update() {
        FastDaoParam<T> param = FastDaoParam.get();
        FastUpdateProvider.update(param);
        return MyBatisConnection.getMapper().update(param);
    }


    @Override
    public Integer delete() {
        FastDaoParam<T> param = FastDaoParam.get();
        FastDeleteProvider.delete(param);
        return MyBatisConnection.getMapper().delete(param);
    }

}

package com.fast.dao.mybatis;

import cn.hutool.core.collection.CollUtil;
import com.alibaba.fastjson.JSONObject;
import com.fast.config.PrimaryKeyType;
import com.fast.dao.DaoActuator;
import com.fast.dao.utils.*;
import com.fast.example.FastDaoParam;
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
    public Integer insert(T pojo) {
        FastDaoParam<T> param = FastDaoParam.get();
        param.setPojo(pojo);
        FastInsertProvider.insert(param);
        Integer insertCount;
        if (param.getTableMapper().getPrimaryKeyType().equals(PrimaryKeyType.AUTO)) {
            insertCount = FastMyBatisConnection.getMapper().insertPrimaryKeyAuto(param);
            if (insertCount > 0) {
                FastValueUtil.setPrimaryKey(pojo, param.getPrimaryKeyValue(), param.getTableMapper());
            }
        } else {
            insertCount = FastMyBatisConnection.getMapper().insert(param);
        }
        FastSqlUtil.printSql(param, pojo);
        return insertCount;
    }


    @Override
    public List<T> findAll() {
        FastDaoParam<T> param = FastDaoParam.get();
        FastSelectProvider.findAll(param);
        List<Map<String, Object>> pojoMap = FastMyBatisConnection.getMapper().findAll(param);
        if (CollUtil.isEmpty(pojoMap)) {
            FastSqlUtil.printSql(param, new ArrayList<>());
            return new ArrayList<>();
        }
        List<T> pojos = JSONObject.parseArray(JSONObject.toJSONString(pojoMap), param.getTableMapper().getObjClass());
        FastSqlUtil.printSql(param, pojos);
        return pojos;
    }

    @Override
    public Integer findCount() {
        FastDaoParam param = FastDaoParam.get();
        FastSelectProvider.findCount(param);
        Integer count = FastMyBatisConnection.getMapper().findCount(param);
        FastSqlUtil.printSql(param, count);
        return count;
    }


    @Override
    public Integer update(T pojo, boolean isSelective) {
        FastDaoParam<T> param = FastDaoParam.get();
        param.setPojo(pojo);
        param.setSelective(isSelective);
        FastUpdateProvider.update(param);
        Integer updateCount = FastMyBatisConnection.getMapper().update(param);
        FastSqlUtil.printSql(param, updateCount);
        return updateCount;
    }


    @Override
    public Integer delete() {
        FastDaoParam param = FastDaoParam.get();
        FastDeleteProvider.delete(param);
        Integer delCount = FastMyBatisConnection.getMapper().delete(param);
        FastSqlUtil.printSql(param, delCount);
        return delCount;
    }

}

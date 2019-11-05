package com.fast.dao.utils;

import cn.hutool.core.util.StrUtil;
import com.fast.mapper.TableMapper;
import com.fast.fast.FastDaoParam;
import com.fast.utils.FastSQL;

/**
 * 更新方法Sql语句拼接
 *
 * @author 张亚伟 https://github.com/kaixinzyw
 */
public class FastUpdateProvider {

    public static void update(FastDaoParam param) {
        if (StrUtil.isNotEmpty(param.getSql())) {
            return;
        }
        TableMapper tableMapper = param.getTableMapper();
        FastSQL fastSQL = new FastSQL();
        fastSQL.UPDATE(tableMapper.getTableName());
        FastSqlUtil.updateSql(fastSQL, param.getUpdate(), param.getSelective(), param.getParamMap(), tableMapper);
        FastSqlUtil.whereSql(param.getFastExample().conditionPackages(), fastSQL, param.getParamMap(), tableMapper);
        param.setSql(fastSQL.toString());
    }
}

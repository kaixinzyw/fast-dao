package com.fast.db.template.dao.utils;

import cn.hutool.core.util.StrUtil;
import com.fast.db.template.mapper.TableMapper;
import com.fast.db.template.template.ConditionPackages;
import com.fast.db.template.template.FastDaoParam;
import com.fast.db.template.utils.FastSQL;

/**
 * 更新方法Sql语句拼接
 *
 * @author 张亚伟 https://github.com/kaixinzyw/fast-db-template
 */
public class FastUpdateProvider {

    public static void update(FastDaoParam param) {
        if (StrUtil.isNotEmpty(param.getSql())) {
            param.setSql(param.getSql().replaceAll("[#][{]", "#{paramMap."));
            return;
        }
        TableMapper tableMapper = param.getTableMapper();
        FastSQL fastSQL = new FastSQL();
        fastSQL.UPDATE(tableMapper.getTableName());
        FastSqlUtil.updateSql(fastSQL, param.getPojo(), param.getSelective(), param.getParamMap(), tableMapper);
        FastSqlUtil.whereSql(param.getFastExample().conditionPackages(), fastSQL, param.getParamMap(), tableMapper);
        param.setSql(fastSQL.toString()+ ";");
    }
}

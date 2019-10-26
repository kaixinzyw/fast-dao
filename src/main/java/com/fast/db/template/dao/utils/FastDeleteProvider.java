package com.fast.db.template.dao.utils;

import cn.hutool.core.util.StrUtil;
import com.fast.db.template.mapper.TableMapper;
import com.fast.db.template.template.ConditionPackages;
import com.fast.db.template.template.FastDaoParam;
import com.fast.db.template.utils.FastSQL;

/**
 * 删除方法Sql语句拼接
 *
 * @author 张亚伟 https://github.com/kaixinzyw/fast-db-template
 */
public class FastDeleteProvider {

    public static void delete(FastDaoParam param) {
        if (StrUtil.isNotEmpty(param.getSql())) {
            param.setSql(param.getSql().replaceAll("[#][{]", "#{paramMap."));
            return;
        }
        TableMapper tableMapper = param.getTableMapper();
        FastSQL fastSQL = new FastSQL();
        fastSQL.DELETE_FROM(tableMapper.getTableName());
        FastSqlUtil.whereSql(param.getFastExample().conditionPackages(), fastSQL, param.getParamMap(), tableMapper);
        param.setSql(fastSQL.toString() + ";");

    }
}

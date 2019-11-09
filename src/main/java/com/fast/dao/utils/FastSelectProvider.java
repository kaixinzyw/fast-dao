package com.fast.dao.utils;

import cn.hutool.core.text.StrBuilder;
import cn.hutool.core.util.StrUtil;
import com.fast.fast.FastDaoParam;

/**
 * 查询方法Sql语句拼接
 *
 * @author 张亚伟 https://github.com/kaixinzyw
 */
public class FastSelectProvider {

    public static void findAll(FastDaoParam param) {
        StrBuilder sqlBuilder;
        if (StrUtil.isNotEmpty(param.getSql())) {
            sqlBuilder = StrUtil.strBuilder(param.getSql().replace(";", " "));
        } else {
            sqlBuilder = FastSqlUtil.selectSql(param);
            FastSqlUtil.whereSql(sqlBuilder, param);
            FastSqlUtil.orderBy(sqlBuilder, param);
        }
        FastSqlUtil.limit(sqlBuilder, param);
        param.setSql(sqlBuilder.toString());
    }

    public static void findCount(FastDaoParam param) {
        if (StrUtil.isNotEmpty(param.getSql())) {
            param.setSql(FastSqlUtil.countQueryInfoReplace(param.getSql()));
            return;
        }
        StrBuilder sqlBuilder = FastSqlUtil.selectSql(param);
        FastSqlUtil.whereSql(sqlBuilder, param);
        param.setSql(FastSqlUtil.countQueryInfoReplace(sqlBuilder.toString()));
    }



}

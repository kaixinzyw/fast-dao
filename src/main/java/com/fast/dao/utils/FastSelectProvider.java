package com.fast.dao.utils;

import cn.hutool.core.text.StrBuilder;
import cn.hutool.core.util.StrUtil;
import com.fast.condition.ConditionPackages;
import com.fast.fast.FastDaoParam;

import java.util.Map;

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
            param.setSql(countQueryInfoReplace(param.getSql()));
            return;
        }
        StrBuilder sqlBuilder = FastSqlUtil.selectSql(param);
        FastSqlUtil.whereSql(sqlBuilder, param);
        param.setSql(countQueryInfoReplace(sqlBuilder.toString()));
    }

    private static String countQueryInfoReplace(String sql) {
        String queryInfo = StrUtil.sub(sql, StrUtil.indexOfIgnoreCase(sql, "SELECT") + 7, StrUtil.indexOfIgnoreCase(sql, "FROM"));
        String replaceQueryInfo;
        if (StrUtil.containsIgnoreCase(queryInfo, "DISTINCT")) {
            replaceQueryInfo = "COUNT(" + queryInfo + ") ";
        } else {
            replaceQueryInfo = "COUNT(*) ";
        }
        return StrUtil.replace(sql, queryInfo, replaceQueryInfo);
    }

}

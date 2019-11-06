package com.fast.dao.utils;

import cn.hutool.core.util.StrUtil;
import com.fast.condition.ConditionPackages;
import com.fast.fast.FastDaoParam;
import com.fast.utils.FastSQL;

import java.util.Map;

/**
 * 查询方法Sql语句拼接
 *
 * @author 张亚伟 https://github.com/kaixinzyw
 */
public class FastSelectProvider {

    public static void findAll(FastDaoParam param) {
        String sql;
        ConditionPackages conditionPackages = param.getFastExample().conditionPackages();
        if (StrUtil.isNotEmpty(param.getSql())) {
            sql = param.getSql();
        } else {
            FastSQL fastSQL = FastSqlUtil.selectSql(param);
            FastSqlUtil.whereSql(fastSQL, param);
            FastSqlUtil.orderBy(fastSQL, param);
            sql = fastSQL.toString();
        }
        sql = StrUtil.replace(sql, ";", " ");
        if (conditionPackages != null) {
            Map<String, Object> paramMap = param.getParamMap();
            if (conditionPackages.getPage() != null && conditionPackages.getSize() != null) {
                sql += " limit #{page} , #{size} ";
                paramMap.put("page", conditionPackages.getPage());
                paramMap.put("size", conditionPackages.getSize());
            } else if (conditionPackages.getLimit() != null) {
                sql += " limit #{limit} ";
                paramMap.put("limit", conditionPackages.getLimit());
            }
        }
        param.setSql(sql);
    }

    public static void findCount(FastDaoParam param) {
        if (StrUtil.isNotEmpty(param.getSql())) {
            param.setSql(countQueryInfoReplace(param.getSql()));
            return;
        }
        FastSQL fastSQL = FastSqlUtil.selectSql(param);
        FastSqlUtil.whereSql(fastSQL, param);
        param.setSql(countQueryInfoReplace(fastSQL.toString()));
    }

    private static String countQueryInfoReplace(String sql) {
        String queryInfo = StrUtil.sub(sql, StrUtil.indexOfIgnoreCase(sql, "SELECT") + 6, StrUtil.indexOfIgnoreCase(sql, "FROM") - 1);
        String replaceQueryInfo;
        if (StrUtil.containsIgnoreCase(queryInfo, "distinct")) {
            replaceQueryInfo = " COUNT( " + queryInfo + " ) ";
        } else {
            replaceQueryInfo = " COUNT( * ) ";
        }
        return StrUtil.replace(sql, queryInfo, replaceQueryInfo);
    }

}

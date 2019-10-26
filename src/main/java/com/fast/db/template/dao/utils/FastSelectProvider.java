package com.fast.db.template.dao.utils;

import cn.hutool.core.util.StrUtil;
import com.fast.db.template.config.AutomaticParameterAttributes;
import com.fast.db.template.mapper.TableMapper;
import com.fast.db.template.template.ConditionPackages;
import com.fast.db.template.template.FastDaoParam;
import com.fast.db.template.utils.FastSQL;

import java.util.Map;

/**
 * 查询方法Sql语句拼接
 *
 * @author 张亚伟 https://github.com/kaixinzyw/fast-db-template
 */
public class FastSelectProvider {

    public static void findAll(FastDaoParam param) {
        String sql;
        ConditionPackages conditionPackages = param.getFastExample().conditionPackages();
        if (StrUtil.isNotEmpty(param.getSql())) {
            param.setSql(param.getSql().replaceAll("[#][{]", "#{paramMap."));
            sql = param.getSql();
        } else {
            TableMapper tableMapper = param.getTableMapper();
            FastSQL fastSQL = new FastSQL();
            fastSQL.SELECT(FastSqlUtil.selectShowField(conditionPackages, tableMapper))
                    .FROM(tableMapper.getTableName());
            FastSqlUtil.whereSql(conditionPackages, fastSQL, param.getParamMap(), tableMapper);
            FastSqlUtil.orderBy(conditionPackages, fastSQL, tableMapper);
            sql = fastSQL.toString();
        }
        sql = StrUtil.replace(sql,";"," ");
        if (conditionPackages != null) {
            Map<String, Object> paramMap = param.getParamMap();
            if (conditionPackages.getPage() != null && conditionPackages.getSize() != null) {
                sql += " limit #{paramMap.page} , #{paramMap.size} ";
                paramMap.put("page", conditionPackages.getPage());
                paramMap.put("size", conditionPackages.getSize());
            } else if (conditionPackages.getLimit() != null) {
                sql += " limit #{paramMap.limit} ";
                paramMap.put("limit", conditionPackages.getLimit());
            }
        }
        param.setSql(sql + ";");
    }

    public static void findCount(FastDaoParam param) {
        if (StrUtil.isNotEmpty(param.getSql())) {
            param.setSql(countQueryInfoReplace(param.getSql().replaceAll("[#][{]", "#{paramMap.")));
            return;
        }
        TableMapper tableMapper = param.getTableMapper();
        FastSQL fastSQL = new FastSQL();
        fastSQL.SELECT(FastSqlUtil.selectShowField(param.getFastExample().conditionPackages(), tableMapper))
                .FROM(tableMapper.getTableName());
        FastSqlUtil.whereSql(param.getFastExample().conditionPackages(), fastSQL, param.getParamMap(), tableMapper);
        param.setSql(countQueryInfoReplace(fastSQL.toString()));
    }

    private static String countQueryInfoReplace(String sql){
        String queryInfo = StrUtil.sub(sql,StrUtil.indexOfIgnoreCase(sql, "SELECT") + 6,StrUtil.indexOfIgnoreCase(sql, "FROM"));
        String replaceQueryInfo;
        if (StrUtil.containsIgnoreCase(queryInfo,"distinct")) {
            replaceQueryInfo = " COUNT( " + queryInfo +" ) ";
        }else {
            replaceQueryInfo = " COUNT( * ) ";
        }
        return StrUtil.replace(sql, queryInfo , replaceQueryInfo);
    }

}

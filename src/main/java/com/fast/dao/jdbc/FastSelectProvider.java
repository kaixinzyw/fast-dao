package com.fast.dao.jdbc;

import cn.hutool.core.text.StrBuilder;
import cn.hutool.core.util.StrUtil;
import com.fast.condition.ConditionPackages;

/**
 * 查询方法Sql语句拼接
 *
 * @author 张亚伟 https://github.com/kaixinzyw
 */
public class FastSelectProvider {

    public static void findAll(ConditionPackages conditionPackages) {
        StrBuilder fastSQL;
        if (StrUtil.isNotEmpty(conditionPackages.getCustomSql())) {
            fastSQL = StrUtil.strBuilder(conditionPackages.getCustomSql().replace(";", " "));
        } else {
            fastSQL = FastSqlUtil.selectSql(conditionPackages);
            StrBuilder conditionBuilder = FastSqlUtil.whereSql(conditionPackages, conditionPackages.getParamMap(), Boolean.TRUE, Boolean.FALSE);
            if (StrUtil.isNotBlank(conditionBuilder)) {
                fastSQL.append(FastSqlUtil.WHERE).append(conditionBuilder);
            }
            FastSqlUtil.orderBy(fastSQL, conditionPackages);
        }
        FastSqlUtil.limit(fastSQL, conditionPackages);
        conditionPackages.setSql(fastSQL.toString());
    }

    public static void findCount(ConditionPackages conditionPackages) {
        if (StrUtil.isNotEmpty(conditionPackages.getCustomSql())) {
            conditionPackages.setSql(FastSqlUtil.countQueryInfoReplace(conditionPackages.getCustomSql()));
            return;
        }
        StrBuilder fastSQL = FastSqlUtil.selectSql(conditionPackages);
        StrBuilder conditionBuilder = FastSqlUtil.whereSql(conditionPackages, conditionPackages.getParamMap(), Boolean.TRUE, Boolean.FALSE);
        if (StrUtil.isNotBlank(conditionBuilder)) {
            fastSQL.append(FastSqlUtil.WHERE).append(conditionBuilder);
        }
        conditionPackages.setSql(FastSqlUtil.countQueryInfoReplace(fastSQL.toString()));
    }



}

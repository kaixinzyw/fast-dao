package com.fast.dao.jdbc;

import cn.hutool.core.text.StrBuilder;
import cn.hutool.core.util.StrUtil;
import com.fast.condition.ConditionPackages;

/**
 * 删除方法Sql语句拼接
 *
 * @author 张亚伟 https://github.com/kaixinzyw
 */
public class FastDeleteProvider {

    public static void delete(ConditionPackages conditionPackages) {
        if (StrUtil.isNotEmpty(conditionPackages.getCustomSql())) {
            conditionPackages.setSql(conditionPackages.getCustomSql());
            return;
        }
        StrBuilder fastSQL = FastSqlUtil.deleteSql(conditionPackages);
        StrBuilder conditionBuilder = FastSqlUtil.whereSql(conditionPackages, conditionPackages.getParamMap(), Boolean.TRUE, Boolean.FALSE);
        if (StrUtil.isNotBlank(conditionBuilder)) {
            fastSQL.append(FastSqlUtil.WHERE).append(conditionBuilder);
        }
        conditionPackages.setSql(fastSQL.toString());

    }
}

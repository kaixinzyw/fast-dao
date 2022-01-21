package com.fast.dao.jdbc;

import cn.hutool.core.text.StrBuilder;
import cn.hutool.core.util.StrUtil;
import com.fast.condition.ConditionPackages;

/**
 * 更新方法Sql语句拼接
 *
 * @author 张亚伟 https://github.com/kaixinzyw
 */
public class FastUpdateProvider {

    public static void update(ConditionPackages conditionPackages) {
        if (StrUtil.isNotEmpty(conditionPackages.getCustomSql())) {
            conditionPackages.setSql(conditionPackages.getCustomSql());
            return;
        }
        StrBuilder fastSQL = FastSqlUtil.updateSql(conditionPackages);
        StrBuilder conditionBuilder = FastSqlUtil.whereSql(conditionPackages, conditionPackages.getParamMap(), Boolean.TRUE, Boolean.FALSE);
        if (StrUtil.isNotBlank(conditionBuilder)) {
            fastSQL.append(FastSqlUtil.WHERE).append(conditionBuilder);
        }
        conditionPackages.setSql(fastSQL.toString());
    }
}

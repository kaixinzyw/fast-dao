package com.fast.dao.jdbc;

import cn.hutool.core.text.StrBuilder;
import cn.hutool.core.util.StrUtil;
import com.fast.condition.ConditionPackages;

/**
 * 扩展插入方法Sql语句拼接
 *
 * @author 张亚伟 https://github.com/kaixinzyw
 */
public class FastInsertProvider {

    public static void insert(ConditionPackages conditionPackages) {
        if (StrUtil.isNotEmpty(conditionPackages.getCustomSql())) {
            conditionPackages.setSql(conditionPackages.getCustomSql());
            return;
        }
        StrBuilder sqlBuilder = FastSqlUtil.insertSql(conditionPackages);
        conditionPackages.setSql(sqlBuilder.toString());
    }
}

package com.fast.dao.utils;

import cn.hutool.core.text.StrBuilder;
import cn.hutool.core.util.StrUtil;
import com.fast.fast.FastDaoParam;

/**
 * 更新方法Sql语句拼接
 *
 * @author 张亚伟 https://github.com/kaixinzyw
 */
public class FastUpdateProvider {

    public static void update(FastDaoParam param) {
        if (StrUtil.isNotEmpty(param.getSql())) {
            return;
        }
        StrBuilder sqlBuilder = FastSqlUtil.updateSql(param);
        FastSqlUtil.whereSql(sqlBuilder, param.getConditionPackages(), param.getParamMap(), Boolean.TRUE, Boolean.FALSE);
        param.setSql(sqlBuilder.toString());
    }
}

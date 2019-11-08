package com.fast.dao.utils;

import cn.hutool.core.text.StrBuilder;
import cn.hutool.core.util.StrUtil;
import com.fast.fast.FastDaoParam;

/**
 * 扩展插入方法Sql语句拼接
 *
 * @author 张亚伟 https://github.com/kaixinzyw
 */
public class FastInsertProvider {

    public static void insert(FastDaoParam param) {
        if (StrUtil.isNotEmpty(param.getSql())) {
            return;
        }
        StrBuilder sqlBuilder = FastSqlUtil.insertSql(param);
        param.setSql(sqlBuilder.toString());
    }
}

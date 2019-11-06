package com.fast.dao.utils;

import cn.hutool.core.util.StrUtil;
import com.fast.mapper.TableMapper;
import com.fast.fast.FastDaoParam;
import com.fast.utils.FastSQL;

/**
 * 删除方法Sql语句拼接
 *
 * @author 张亚伟 https://github.com/kaixinzyw
 */
public class FastDeleteProvider {

    public static void delete(FastDaoParam param) {
        if (StrUtil.isNotEmpty(param.getSql())) {
            return;
        }
        TableMapper tableMapper = param.getTableMapper();
        FastSQL fastSQL = new FastSQL();
        fastSQL.DELETE_FROM(tableMapper.getTableName());
        FastSqlUtil.whereSql(fastSQL, param);
        param.setSql(fastSQL.toString());

    }
}

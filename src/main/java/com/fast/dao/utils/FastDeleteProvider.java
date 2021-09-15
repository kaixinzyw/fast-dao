package com.fast.dao.utils;

import cn.hutool.core.text.StrBuilder;
import cn.hutool.core.util.StrUtil;
import com.fast.mapper.TableMapper;
import com.fast.fast.FastDaoParam;

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
        StrBuilder fastSQL = FastSqlUtil.deleteSql(param);
        FastSqlUtil.whereSql(fastSQL, param.getConditionPackages(), param.getParamMap(), Boolean.TRUE, Boolean.FALSE);
        param.setSql(fastSQL.toString());

    }
}

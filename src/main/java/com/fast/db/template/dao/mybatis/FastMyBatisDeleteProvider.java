package com.fast.db.template.dao.mybatis;

import com.fast.db.template.template.FastDaoParam;

public class FastMyBatisDeleteProvider {

    public static String delete(FastDaoParam param) {
        return param.getSql();
    }
}

package com.fast.db.template.dao.mybatis;

import com.fast.db.template.template.FastDaoParam;

public class FastMyBatisInsertProvider {

    public static String insert(FastDaoParam param) {
        return param.getSql();
    }

}

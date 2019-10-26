package com.fast.db.template.dao.mybatis;

import com.fast.db.template.template.FastDaoParam;

public class FastMyBatisUpdateProvider {

    public static String update(FastDaoParam param) {
        return param.getSql();
    }
}

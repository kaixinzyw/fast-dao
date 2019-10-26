package com.fast.db.template.dao.mybatis;

import com.fast.db.template.template.FastDaoParam;

public class FastMyBatisSelectProvider {

    public static String findAll(FastDaoParam param) {
        return param.getSql();
    }

    public static String findCount(FastDaoParam param) {
        return param.getSql();
    }

}

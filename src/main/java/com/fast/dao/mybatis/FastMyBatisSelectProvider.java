package com.fast.dao.mybatis;

import com.fast.fast.FastDaoParam;

public class FastMyBatisSelectProvider {

    public static String findAll(FastDaoParam param) {
        return param.getSql();
    }

    public static String findCount(FastDaoParam param) {
        return param.getSql();
    }

}

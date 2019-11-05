package com.fast.dao.mybatis;


import com.fast.fast.FastDaoParam;

public class FastMyBatisInsertProvider {

    public static String insert(FastDaoParam param) {
        return param.getSql();
    }

}

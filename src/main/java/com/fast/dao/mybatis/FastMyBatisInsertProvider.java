package com.fast.dao.mybatis;

import com.fast.example.FastDaoParam;

public class FastMyBatisInsertProvider {

    public static String insert(FastDaoParam param) {
        return param.getSql();
    }

}

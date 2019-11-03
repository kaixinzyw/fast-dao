package com.fast.dao.mybatis;

import com.fast.example.FastDaoParam;

public class FastMyBatisUpdateProvider {

    public static String update(FastDaoParam param) {
        return param.getSql();
    }
}

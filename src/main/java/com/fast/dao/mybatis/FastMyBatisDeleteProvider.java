package com.fast.dao.mybatis;

import com.fast.example.FastDaoParam;

public class FastMyBatisDeleteProvider {

    public static String delete(FastDaoParam param) {
        return param.getSql();
    }
}

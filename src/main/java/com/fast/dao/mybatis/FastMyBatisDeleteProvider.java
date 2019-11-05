package com.fast.dao.mybatis;

import com.fast.fast.FastDaoParam;

public class FastMyBatisDeleteProvider {

    public static String delete(FastDaoParam param) {
        return param.getSql();
    }
}

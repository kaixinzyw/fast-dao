package com.fast.dao.mybatis;

import com.fast.fast.FastDaoParam;

public class FastMyBatisUpdateProvider {

    public static String update(FastDaoParam param) {
        return param.getSql();
    }
}

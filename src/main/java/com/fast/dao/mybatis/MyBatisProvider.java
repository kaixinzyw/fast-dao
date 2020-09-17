package com.fast.dao.mybatis;


import cn.hutool.core.util.ReUtil;
import com.fast.fast.FastDaoParam;

public class MyBatisProvider {
    private static final String PARAM_PREFIX = "#{";
    private static final String PARAM_PREFIX_2 = "${";
    private static final String JDBC_SQL_CONVERSION_RE_RULE = "[#][{]";
    private static final String JDBC_SQL_CONVERSION_RE_RULE_2 = "[$][{]";
    private static final String JDBC_SQL_CONVERSION_RE_RESULT = "#{paramMap.";

    public static String getSql(FastDaoParam param) {
        String sql = param.getSql();
        if (sql.contains(PARAM_PREFIX)) {
            sql = ReUtil.replaceAll(sql, JDBC_SQL_CONVERSION_RE_RULE, JDBC_SQL_CONVERSION_RE_RESULT);
        }
        if (sql.contains(PARAM_PREFIX_2)) {
            sql = ReUtil.replaceAll(sql, JDBC_SQL_CONVERSION_RE_RULE_2, JDBC_SQL_CONVERSION_RE_RESULT);
        }
        return sql;
    }

}

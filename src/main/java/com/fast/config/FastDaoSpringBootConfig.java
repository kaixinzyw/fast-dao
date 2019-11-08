package com.fast.config;

import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.StrUtil;
import com.fast.dao.jdbc.JdbcImpl;
import com.fast.dao.mybatis.FastMyBatisImpl;
import com.fast.dao.mybatis.FastMyBatisMapper;
import com.fast.utils.FastValueUtil;
import com.fast.utils.SpringUtil;
import org.apache.ibatis.session.SqlSession;
import org.springframework.core.env.Environment;

public class FastDaoSpringBootConfig {

    private static void loadMyBatisMapper() {
        try {
            SqlSession session = SpringUtil.getBean(SqlSession.class);
            if (session == null) {
                return;
            }
            session.getConfiguration().addMapper(FastMyBatisMapper.class);
        }catch (Exception e){}

    }

    private static void loadFastParams() {
        Environment env = SpringUtil.getBean(Environment.class);
        if (env == null) {
            return;
        }

        //是否开启驼峰转换
        Boolean toCamelCase = env.getProperty("fast.db.camel", Boolean.class);
        if (BooleanUtil.isFalse(toCamelCase)) {
            FastDaoAttributes.isToCamelCase = Boolean.FALSE;
        }

        //框架实现设置,默认使用jdbc
        String impl = env.getProperty("fast.db.impl", String.class);
        if (StrUtil.isNotEmpty(impl)) {
            if (StrUtil.equalsIgnoreCase(impl, "mybatis")) {
                loadMyBatisMapper();
                FastDaoAttributes.setDaoActuator(FastMyBatisImpl.class);
            } else if (StrUtil.equalsIgnoreCase(impl, "jdbc")) {
                FastDaoAttributes.setDaoActuator(JdbcImpl.class);
            } else {
                throw new RuntimeException("请设置正确的Fast框架实现,可选值为mybatis | jdbc");
            }
        }


        //自动设置数据创建时间
        String createTimeTableColumnName = env.getProperty("fast.db.set.create", String.class);
        if (StrUtil.isNotEmpty(createTimeTableColumnName)) {
            FastDaoAttributes.isAutoSetCreateTime = Boolean.TRUE;
            FastDaoAttributes.createTimeTableColumnName = createTimeTableColumnName;
            FastDaoAttributes.createTimeFieldName = FastValueUtil.toCamelCase(createTimeTableColumnName);
        }

        //自动设置数据更新时间
        String updateTimeTableColumnName = env.getProperty("fast.db.set.update", String.class);
        if (StrUtil.isNotEmpty(updateTimeTableColumnName)) {
            FastDaoAttributes.isAutoSetUpdateTime = Boolean.TRUE;
            FastDaoAttributes.updateTimeTableColumnName = updateTimeTableColumnName;
            FastDaoAttributes.updateTimeFieldName = FastValueUtil.toCamelCase(updateTimeTableColumnName);
        }

        //设置逻辑删除
        String deleteTableColumnName = env.getProperty("fast.db.set.delete", String.class);
        if (StrUtil.isNotEmpty(deleteTableColumnName)) {
            FastDaoAttributes.isOpenLogicDelete = Boolean.TRUE;
            FastDaoAttributes.deleteTableColumnName = deleteTableColumnName;
            FastDaoAttributes.deleteFieldName = FastValueUtil.toCamelCase(deleteTableColumnName);
        }
        Boolean defaultDeleteValue = env.getProperty("fast.db.set.delete.val", Boolean.class);
        if (BooleanUtil.isFalse(defaultDeleteValue)) {
            FastDaoAttributes.defaultDeleteValue = Boolean.FALSE;
        }

        //设置缓存
        Long defaultCacheTime = env.getProperty("fast.db.cache.time", Long.class);
        if (defaultCacheTime != null && defaultCacheTime > 0) {
            FastDaoAttributes.isOpenCache = Boolean.TRUE;
            FastDaoAttributes.defaultCacheTime = defaultCacheTime;
        }

        //SQL打印设置
        Boolean sqlPrint = env.getProperty("fast.db.sql.log", Boolean.class);
        if (BooleanUtil.isTrue(sqlPrint)) {
            FastDaoAttributes.sqlLogLevel = SqlLogLevel.INFO;
        }
        Boolean sqlResult = env.getProperty("fast.db.sql.log.result", Boolean.class);
        if (BooleanUtil.isTrue(sqlResult)) {
            FastDaoAttributes.isSqlPrintResult = Boolean.TRUE;
        }
    }


    public static void load() {
        loadFastParams();
    }

}

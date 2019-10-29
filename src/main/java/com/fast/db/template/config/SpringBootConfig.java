package com.fast.db.template.config;

import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.StrUtil;
import com.fast.db.template.dao.jdbc.SpringJDBCMySqlImpl;
import com.fast.db.template.dao.mybatis.FastMyBatisImpl;
import com.fast.db.template.dao.mybatis.FastMyBatisMapper;
import com.fast.db.template.utils.FastValueUtil;
import com.fast.db.template.utils.SpringUtil;
import org.apache.ibatis.session.SqlSession;
import org.springframework.core.env.Environment;

public class SpringBootConfig {

    private static void loadMyBatisMapper() {
        SqlSession session = SpringUtil.getBean(SqlSession.class);
        if (session == null) {
            return;
        }
        session.getConfiguration().addMapper(FastMyBatisMapper.class);
    }

    private static void loadFastParams() {
        Environment env = SpringUtil.getBean(Environment.class);
        if (env == null) {
            return;
        }

        //是否开启驼峰转换
        Boolean toCamelCase = env.getProperty("fast.db.camel", Boolean.class);
        if (BooleanUtil.isFalse(toCamelCase)) {
            FastParams.isToCamelCase = Boolean.FALSE;
        }

        //框架实现设置,默认使用spring-jdbc
        String impl = env.getProperty("fast.db.impl", String.class);
        if (StrUtil.isNotEmpty(impl)) {
            if (StrUtil.equalsIgnoreCase(impl, "mybatis")) {
                FastParams.setDBActuator(FastMyBatisImpl.class);
            } else if (StrUtil.equalsIgnoreCase(impl, "spring-jdbc")) {
                FastParams.setDBActuator(SpringJDBCMySqlImpl.class);
            } else {
                throw new RuntimeException("请设置正确的Fast框架实现,可选值为mybatis | spring-jdbc");
            }
        }


        //自动设置数据创建时间
        String createTimeTableColumnName = env.getProperty("fast.db.set.create", String.class);
        if (StrUtil.isNotEmpty(createTimeTableColumnName)) {
            FastParams.isAutoSetCreateTime = Boolean.TRUE;
            FastParams.createTimeTableColumnName = createTimeTableColumnName;
            FastParams.createTimeFieldName = FastValueUtil.toCamelCase(createTimeTableColumnName);
        }

        //自动设置数据更新时间
        String updateTimeTableColumnName = env.getProperty("fast.db.set.update", String.class);
        if (StrUtil.isNotEmpty(updateTimeTableColumnName)) {
            FastParams.isAutoSetUpdateTime = Boolean.TRUE;
            FastParams.updateTimeTableColumnName = updateTimeTableColumnName;
            FastParams.updateTimeFieldName = FastValueUtil.toCamelCase(updateTimeTableColumnName);
        }

        //设置逻辑删除
        String deleteTableColumnName = env.getProperty("fast.db.set.delete", String.class);
        if (StrUtil.isNotEmpty(deleteTableColumnName)) {
            FastParams.isOpenLogicDelete = Boolean.TRUE;
            FastParams.deleteTableColumnName = deleteTableColumnName;
            FastParams.deleteFieldName = FastValueUtil.toCamelCase(deleteTableColumnName);
        }
        Boolean defaultDeleteValue = env.getProperty("fast.db.set.delete.val", Boolean.class);
        if (BooleanUtil.isFalse(defaultDeleteValue)) {
            FastParams.defaultDeleteValue = Boolean.FALSE;
        }

        //设置缓存
        Long defaultCacheTime = env.getProperty("fast.db.cache.time", Long.class);
        if (defaultCacheTime != null && defaultCacheTime > 0) {
            FastParams.isOpenCache = Boolean.TRUE;
            FastParams.defaultCacheTime = defaultCacheTime;
        }

        //SQL打印设置
        Boolean sqlPrint = env.getProperty("fast.db.sql.log", Boolean.class);
        if (BooleanUtil.isTrue(sqlPrint)) {
            FastParams.isSqlPrint = Boolean.TRUE;
        }
        Boolean sqlResult = env.getProperty("fast.db.sql.log.result", Boolean.class);
        if (BooleanUtil.isTrue(sqlResult)) {
            FastParams.isSqlPrintResult = Boolean.TRUE;
        }
    }


    public static void load() {
        loadMyBatisMapper();
        loadFastParams();
    }

}

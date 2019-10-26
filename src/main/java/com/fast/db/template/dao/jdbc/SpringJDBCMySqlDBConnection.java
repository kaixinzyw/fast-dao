package com.fast.db.template.dao.jdbc;

import com.fast.db.template.utils.SpringUtil;
import io.netty.util.concurrent.FastThreadLocal;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import javax.sql.DataSource;

/**
 * JDBC链接获取
 *
 * @author 张亚伟 https://github.com/kaixinzyw/fast-db-template
 */
public class SpringJDBCMySqlDBConnection {

    /**
     * 线程缓存
     */
    private static FastThreadLocal<NamedParameterJdbcTemplate> threadLocal = new FastThreadLocal<>();

    /**
     * 获取NamedParameterJdbcTemplate
     *
     * @return 获取到的信息
     */
    public static NamedParameterJdbcTemplate getJdbcTemplate() {
        NamedParameterJdbcTemplate jdbcTemplate = threadLocal.get();
        if (jdbcTemplate == null) {
            jdbcTemplate = SpringUtil.getBean(NamedParameterJdbcTemplate.class);
            threadLocal.set(SpringUtil.getBean(NamedParameterJdbcTemplate.class));
        }
        return jdbcTemplate;
    }

    /**
     * 数据源切换
     *
     * @param dataSource 需要切换的数据源信息,可设置为null切换到默认数据源
     */
    public static void dataSource(DataSource dataSource) {
        if (dataSource == null) {
            threadLocal.remove();
            return;
        }
        threadLocal.set(new NamedParameterJdbcTemplate(dataSource));
    }

}

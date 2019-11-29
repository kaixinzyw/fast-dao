package com.fast.dao.jdbc;

import com.fast.config.FastDaoAttributes;
import io.netty.util.concurrent.FastThreadLocal;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import javax.sql.DataSource;

/**
 * JDBC链接获取
 *
 * @author 张亚伟 https://github.com/kaixinzyw
 */
public class JdbcConnection {

    private static final FastThreadLocal<NamedParameterJdbcTemplate> jdbcTemplateThreadLocal = new FastThreadLocal<>();

    /**
     * 获取NamedParameterJdbcTemplate
     *
     * @return 获取到的信息
     */
    public static NamedParameterJdbcTemplate getJdbcTemplate() {
        NamedParameterJdbcTemplate jdbcTemplate = jdbcTemplateThreadLocal.get();
        if (jdbcTemplate == null){
            jdbcTemplate = dataSource(null);
        }
        return jdbcTemplate;
    }

    public static NamedParameterJdbcTemplate dataSource(DataSource dataSource){
        if (dataSource == null) {
            dataSource = FastDaoAttributes.getDataSource();
        }
        NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
        jdbcTemplateThreadLocal.set(jdbcTemplate);
        return jdbcTemplate;
    }

}

package com.fast.dao.jdbc;

import com.fast.mapper.FastDaoThreadLocalAttributes;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

/**
 * JDBC链接获取
 *
 * @author 张亚伟 https://github.com/kaixinzyw
 */
public class SpringJDBCMySqlDBConnection {


    /**
     * 获取NamedParameterJdbcTemplate
     *
     * @return 获取到的信息
     */
    public static NamedParameterJdbcTemplate getJdbcTemplate() {
        return FastDaoThreadLocalAttributes.get().getJdbcTemplate();
    }

}

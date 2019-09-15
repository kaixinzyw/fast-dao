package com.fast.db.template.implement.spring_jdbc;

import com.fast.db.template.utils.SpringUtil;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

/**
 * JDBC链接获取
 * @author 张亚伟 398850094@qq.com
 */
public class SpringJDBCMySqlDBConnection {

    private static DataSource dataSource;


    public static JdbcTemplate getJdbcTemplate() {
        if (dataSource != null) {
            return new JdbcTemplate(dataSource);
        }
        return new JdbcTemplate(SpringUtil.getBean(DataSource.class));
    }

    public static void setJdbcTemplate(DataSource dataSource) {
        SpringJDBCMySqlDBConnection.dataSource = dataSource;
    }

}

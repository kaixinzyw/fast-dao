package com.fast.test;

import com.alibaba.druid.pool.DruidDataSource;
import com.fast.config.FastDaoConfig;
import com.fast.config.SqlLogLevel;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;

import javax.sql.DataSource;
import java.util.concurrent.TimeUnit;

/**
 * 框架设置
 */
public class FastSetConfigTest {

    public static void fastDaoConfig() {

        /**
         * 字段驼峰转换 例 user_name = userName 默认开启
         */
        FastDaoConfig.openToCamelCase();
        /**
         * 设置SQL日志打印,默认关闭
         * 参数1: 日志打印级别 DEBUG,INFO,OFF
         * 参数2: 是否打印详细SQL日志
         * 参数3: 是否打印SQL执行结果
         */
        FastDaoConfig.openSqlPrint(SqlLogLevel.INFO, true, true);
        /**
         * 开启自动对数据 新增操作 进行创建时间设置
         * 参数1: 需要设置创建时间的字段名
         */
        FastDaoConfig.openAutoSetCreateTime("create_time");
        /**
         * 开启自动对数据 更新操作/逻辑删除操作 进行更新时间设置
         * 参数1: 需要设置更新时间的字段名
         */
        FastDaoConfig.openAutoSetUpdateTime("update_time");
        /**
         * 开启逻辑删除功能,开启后会对逻辑删除标记的数据在 更新|删除|查询 时进行保护,可通过模板进行单次操作逻辑删除保护的关闭
         * 参数1:  逻辑删除字段名
         * 参数2:  逻辑删除标记默认值
         */
        FastDaoConfig.openLogicDelete("deleted", Boolean.TRUE);
        /**
         * 设置全局默认缓存时间,两种缓存模式(本地缓存，Redis缓存),支持缓存的自动刷新<更新,删除,新增>后会自动刷新缓存的数据
         * Reids缓存需要进行配置
         * 参数1:  默认缓存时间
         * 参数2:  默认缓存时间类型
         */
        FastDaoConfig.openCache(10L, TimeUnit.SECONDS);
        /**
         * 数据源配置,Spring环境可自动识别
         */
        FastDaoConfig.dataSource(getDataSource());

        /**
         * redis缓存配置,Spring环境可自动识别
         */
        FastDaoConfig.redisConnectionFactory(getRedisConnectionFactory());

    }

    private static DataSource getDataSource() {
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setUrl("jdbc:mysql://192.168.1.101:3306/my_test?useUnicode=true&characterEncoding=utf-8&useSSL=false&serverTimezone=UTC&useOldAliasMetadataBehavior=true");
        dataSource.setUsername("my_test");
        dataSource.setPassword("my_test");
        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        return dataSource;
    }

    private static RedisConnectionFactory getRedisConnectionFactory() {
        RedisStandaloneConfiguration redisConfig = new RedisStandaloneConfiguration();
        redisConfig.setHostName("127.0.0.1");
        redisConfig.setPort(6379);
        redisConfig.setDatabase(0);
        JedisConnectionFactory jedisConnectionFactory = new JedisConnectionFactory(redisConfig);
        jedisConnectionFactory.afterPropertiesSet();
        return jedisConnectionFactory;
    }


}

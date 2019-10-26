package com.fast.db.template.dao.mybatis;

import com.fast.db.template.utils.SpringUtil;
import io.netty.util.concurrent.FastThreadLocal;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;

import javax.sql.DataSource;

/**
 * 获取MyBatisMapper工具类
 *
 * @author 张亚伟 https://github.com/kaixinzyw/fast-db-template
 */
public class FastMyBatisConnection {

    /**
     * 线程缓存
     */
    private static FastThreadLocal<FastMyBatisMapper> threadLocal = new FastThreadLocal<>();

    /**
     * 获取Mapper
     * @return 从线程缓存中获取FastMyBatisMapper
     */
    public static FastMyBatisMapper getMapper() {
        FastMyBatisMapper fastMyBatisMapper = threadLocal.get();
        if (fastMyBatisMapper == null) {
            fastMyBatisMapper = SpringUtil.getBean(SqlSession.class).getMapper(FastMyBatisMapper.class);
            threadLocal.set(fastMyBatisMapper);
        }
        return fastMyBatisMapper;
    }

    /**
     * 切换数据源
     * @param dataSource 需要切换的数据源信息,可设置为null切换到默认数据源
     */
    public static void dataSource(DataSource dataSource) {
        if (dataSource == null) {
            threadLocal.remove();
            return;
        }
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(dataSource);
        SqlSessionFactory sqlSessionFactory = null;
        try {
            sqlSessionFactory = sqlSessionFactoryBean.getObject();
            sqlSessionFactory.getConfiguration().addMapper(FastMyBatisMapper.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        SqlSessionTemplate template = new SqlSessionTemplate(sqlSessionFactory);
        FastMyBatisMapper mapper = template.getMapper(FastMyBatisMapper.class);
        threadLocal.set(mapper);
    }

}

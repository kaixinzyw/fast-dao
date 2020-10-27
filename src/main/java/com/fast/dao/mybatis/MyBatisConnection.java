package com.fast.dao.mybatis;

import com.fast.config.FastDaoAttributes;
import io.netty.util.concurrent.FastThreadLocal;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;

import javax.sql.DataSource;

/**
 * 获取MyBatisMapper工具类
 *
 * @author 张亚伟 https://github.com/kaixinzyw
 */
public class MyBatisConnection {

    private static final FastThreadLocal<MyBatisMapper> mapperThreadLocal = new FastThreadLocal<>();

    /**
     * 获取Mapper
     *
     * @return 从线程缓存中获取FastMyBatisMapper
     */
    public static MyBatisMapper getMapper() {
        MyBatisMapper myBatisMapper = mapperThreadLocal.get();
        if (myBatisMapper == null) {
            myBatisMapper = dataSource();
        }
        return myBatisMapper;
    }

    public static MyBatisMapper dataSource() {
        DataSource dataSource = FastDaoAttributes.getDataSource();
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(dataSource);
        SqlSessionTemplate template;
        try {
            template = new SqlSessionTemplate(sqlSessionFactoryBean.getObject());
            template.getConfiguration().addMapper(MyBatisMapper.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        MyBatisMapper mapper = template.getMapper(MyBatisMapper.class);
        ;
        mapperThreadLocal.set(mapper);
        return mapper;
    }

}

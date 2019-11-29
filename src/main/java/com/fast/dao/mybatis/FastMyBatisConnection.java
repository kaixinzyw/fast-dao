package com.fast.dao.mybatis;

import com.fast.config.FastDaoAttributes;
import io.netty.util.concurrent.FastThreadLocal;
import org.apache.ibatis.binding.BindingException;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;

import javax.sql.DataSource;

/**
 * 获取MyBatisMapper工具类
 *
 * @author 张亚伟 https://github.com/kaixinzyw
 */
public class FastMyBatisConnection {

    private static final FastThreadLocal<FastMyBatisMapper> mapperThreadLocal = new FastThreadLocal<>();

    /**
     * 获取Mapper
     *
     * @return 从线程缓存中获取FastMyBatisMapper
     */
    public static FastMyBatisMapper getMapper() {
        FastMyBatisMapper fastMyBatisMapper = mapperThreadLocal.get();
        if (fastMyBatisMapper == null) {
            fastMyBatisMapper = dataSource(null);
        }
        return fastMyBatisMapper;
    }

    public static FastMyBatisMapper dataSource(DataSource dataSource) {
        if (dataSource == null) {
            dataSource = FastDaoAttributes.getDataSource();
        }
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(dataSource);

        SqlSessionTemplate template = null;
        try {
            template = new SqlSessionTemplate(sqlSessionFactoryBean.getObject());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        FastMyBatisMapper mapper;
        try {
            mapper = template.getMapper(FastMyBatisMapper.class);
        } catch (BindingException e1) {
            synchronized (FastMyBatisConnection.class) {
                try {
                    mapper = template.getMapper(FastMyBatisMapper.class);
                } catch (BindingException e2) {
                    template.getConfiguration().addMapper(FastMyBatisMapper.class);
                    mapper = template.getMapper(FastMyBatisMapper.class);
                }

            }
        }
        mapperThreadLocal.set(mapper);
        return mapper;
    }

}

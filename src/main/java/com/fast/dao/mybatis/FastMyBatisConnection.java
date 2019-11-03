package com.fast.dao.mybatis;

import com.fast.mapper.FastDaoThreadLocalAttributes;
import org.apache.ibatis.binding.BindingException;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;

import javax.sql.DataSource;

/**
 * 获取MyBatisMapper工具类
 *
 * @author 张亚伟 https://github.com/kaixinzyw
 */
public class FastMyBatisConnection {

    /**
     * 获取Mapper
     *
     * @return 从线程缓存中获取FastMyBatisMapper
     */
    public static FastMyBatisMapper getMapper() {
        return FastDaoThreadLocalAttributes.get().getMyBatisMapper();
    }

    public static FastMyBatisMapper mapperPack(DataSource dataSource) {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(dataSource);

        SqlSessionTemplate template = null;
        try {
            template = new SqlSessionTemplate(sqlSessionFactoryBean.getObject());
        } catch (Exception e) {
            e.printStackTrace();
        }

        FastMyBatisMapper mapper;
        try {
            mapper = template.getMapper(FastMyBatisMapper.class);
        }catch (BindingException e1){
            synchronized (FastMyBatisConnection.class) {
                try {
                    mapper = template.getMapper(FastMyBatisMapper.class);
                }catch (BindingException e2) {
                    template.getConfiguration().addMapper(FastMyBatisMapper.class);
                    mapper = template.getMapper(FastMyBatisMapper.class);
                }

            }
        }
        return mapper;
    }

}

package com.fast.db.template.utils;

import com.fast.db.template.dao.mybatis.FastMyBatisMapper;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * @author 张亚伟 https://github.com/kaixinzyw/fast-db-template
 */
@Component
public class SpringUtil implements ApplicationContextAware {

    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        if (SpringUtil.applicationContext == null) {
            SpringUtil.applicationContext = applicationContext;
            try {
                SqlSession sqlSession = SpringUtil.getBean(SqlSession.class);
                sqlSession.getConfiguration().addMapper(FastMyBatisMapper.class);
            }catch (Exception e){}
        }
    }

    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    public static Object getBean(String name) {
        try {
            return getApplicationContext().getBean(name);
        }catch (Exception e){return null;}
    }

    public static <T> T getBean(Class<T> clazz) {
        try {
            return getApplicationContext().getBean(clazz);
        }catch (Exception e){return null;}
    }

    public static <T> T getBean(String name, Class<T> clazz) {
        try {
            return getApplicationContext().getBean(name, clazz);
        }catch (Exception e){return null;}
    }
}

package com.fast.mapper;

import cn.hutool.core.collection.CollUtil;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Fast类加载器
 *
 * @author 张亚伟
 */
@Component
public class FastDaoBeanListener implements ApplicationContextAware {

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        Map<String, Object> map = applicationContext.getBeansWithAnnotation(FastDaoBean.class);
        if (CollUtil.isNotEmpty(map)) {
            for (Object value : map.values()) {
                try {
                    TableMapperUtil.getTableMappers(value.getClass());
                }catch (Exception ignored){}
            }
        }
    }
}
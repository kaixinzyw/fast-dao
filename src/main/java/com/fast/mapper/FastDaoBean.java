package com.fast.mapper;


import org.springframework.stereotype.Repository;

import java.lang.annotation.*;

/**
 * 启动服务时加载的Bean
 * @author 张亚伟
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Repository
public @interface FastDaoBean {

}

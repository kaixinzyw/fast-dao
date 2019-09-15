package com.fast.db.template.implement.mybatis;

import com.fast.db.template.utils.SpringUtil;
import org.apache.ibatis.session.SqlSession;

/**
 * 获取MyBatisMapper工具类
 *
 * @author 张亚伟 398850094@qq.com
 */
public class FastMyBatisUtil {

    private static Boolean mapperAutowired = Boolean.FALSE;


    public static FastMyBatisMapper getMapper() {
        SqlSession session = SpringUtil.getBean(SqlSession.class);
        if (!mapperAutowired) {
            session.getConfiguration().addMapper(FastMyBatisMapper.class);
            mapperAutowired = Boolean.TRUE;
        }
        FastMyBatisMapper mapper = session.getMapper(FastMyBatisMapper.class);
        return mapper;
    }

}

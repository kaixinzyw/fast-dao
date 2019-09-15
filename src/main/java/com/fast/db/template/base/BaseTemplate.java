package com.fast.db.template.base;

import com.fast.db.template.mapper.BeanMapper;
import com.fast.db.template.template.DBTemplate;
import com.fast.db.template.template.FastExample;

import java.lang.reflect.ParameterizedType;

/**
 * @author 张亚伟 398850094@qq.com
 */
public class BaseTemplate<Pojo> {

    protected Class<Pojo> clazz = (Class<Pojo>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    protected FastExample fastExample = new FastExample();

    public DBTemplate<Pojo> dbTemplate() {
        return BeanMapper.getDBMapper(clazz).getDbTemplate().fastExample(fastExample);
    }

}

package com.fast.db.template.base;

import com.fast.db.template.template.FastExample;
import com.fast.db.template.utils.page.PageInfo;

import java.util.Collection;
import java.util.List;

/**
 * @author 张亚伟 398850094@qq.com
 */
public interface IFastBaseService<T> {
    Boolean insert(T pojo);


    Boolean deleteByPrimaryKey(Object primaryKey);

    T findByPrimaryKey(Object primaryKey);


    List<T> findByIn(String inName, Collection inValues);

    List<T> findByIn(String inName, Object... inValues);

    List<T> findAll();

    Boolean updateByPrimaryKeySelective(T pojo);

    Boolean updateByPrimaryKey(T pojo);

    T findOne(FastExample fastExample);

    List<T> findAll(FastExample fastExample);

    Integer findCount(FastExample fastExample);

    PageInfo<T> findPage(FastExample fastExample, int pageNum, int pageSize);

    Integer update(T pojo, FastExample fastExample);

    Integer updateSelective(T pojo, FastExample fastExample);

    Integer delete(FastExample fastExample);

}

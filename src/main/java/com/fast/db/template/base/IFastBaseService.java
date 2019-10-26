package com.fast.db.template.base;

import com.fast.db.template.template.FastExample;
import com.fast.db.template.utils.page.PageInfo;

import java.util.Collection;
import java.util.List;

/**
 * @author 张亚伟 https://github.com/kaixinzyw/fast-db-template
 */
public interface IFastBaseService<T> {

    Boolean insert(T pojo);

    T findByPrimaryKey(Object primaryKey);
    T findOne(FastExample<T> fastExample);
    List<T> findByIn(String inName, Collection inValues);
    List<T> findByIn(String inName, Object... inValues);
    List<T> findAll(T pojo);
    List<T> findAll(FastExample<T> fastExample);
    Integer findCount(FastExample<T> fastExample);
    PageInfo<T> findPage(FastExample<T> fastExample, int pageNum, int pageSize);

    Boolean updateByPrimaryKey(T pojo);
    Boolean updateByPrimaryKeyOverwrite(T pojo);
    Integer update(T pojo, FastExample<T> fastExample);
    Integer updateSelective(T pojo, FastExample<T> fastExample);

    Boolean deleteByPrimaryKey(Object primaryKey);
    Boolean deleteByPrimaryKeyDisk(Object primaryKey);
    Integer delete(FastExample<T> fastExample);
    Integer deleteDisk(FastExample<T> fastExample);

}

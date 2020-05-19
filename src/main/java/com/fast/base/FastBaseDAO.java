package com.fast.base;

/**
 * DAO层基类封装
 *
 * @author 张亚伟 https://github.com/kaixinzyw/fast-db-template
 */

import com.fast.condition.FastExample;
import com.fast.utils.page.PageInfo;

import java.lang.reflect.ParameterizedType;
import java.util.List;

/**
 * DAO层基类封装
 *
 * @param <T> 操作的对象泛型
 * @author 张亚伟 https://github.com/kaixinzyw/fast-db-template
 */
public class FastBaseDAO<T> {

    private Class<T> fastDaoPojoClass = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];

    /**
     * 新增数据
     *
     * @param pojo 需要新增的数据,自动给对象中设置的主键进行赋值
     * @return 是否插入成功
     */
    public T insert(T pojo) {
        return new FastExample<>(fastDaoPojoClass).dao().insert(pojo);
    }

    public List<T> insertList(List<T> pojos) {
        return new FastExample<>(fastDaoPojoClass).dao().insertList(pojos);
    }

    /**
     * 通过主键查询数据
     * 如果设置逻辑删除,对逻辑删除的数据不进行操作
     *
     * @param primaryKeyValue 主键参数
     * @return 查询到的数据结果
     */
    public T findByPrimaryKey(Object primaryKeyValue) {
        return new FastExample<>(fastDaoPojoClass).dao().findByPrimaryKey(primaryKeyValue);
    }

    /**
     * 通过查询条件查询一条数据
     * 如果设置逻辑删除,对逻辑删除的数据不进行操作
     *
     * @param obj 参数中值不为空的属性作为AND条件
     * @return 数据结果
     */
    public T findOne(Object obj) {
        FastExample<T> example = new FastExample<>(fastDaoPojoClass);
        example.equalObject(obj);
        return example.dao().findOne();
    }

    /**
     * 查询表所有的数据
     * 如果设置逻辑删除,对逻辑删除的数据不进行操作
     *
     * @param obj 参数中值不为空的属性作为AND条件
     * @return 查询到的数据结果
     */
    public List<T> findAll(Object obj) {
        FastExample<T> example = new FastExample<>(fastDaoPojoClass);
        example.equalObject(obj);
        return example.dao().findAll();
    }

    /**
     * 通过查询条件查询所符合要求的数据数量
     * 如果设置逻辑删除,对逻辑删除的数据不进行操作
     *
     * @param obj 参数中值不为空的属性作为AND条件
     * @return 查询到的数据条数
     */
    public Integer findCount(Object obj) {
        FastExample<T> example = new FastExample<>(fastDaoPojoClass);
        example.equalObject(obj);
        return example.dao().findCount();
    }

    /**
     * 通过查询条件查询所符合要求的数据,并进行分页
     * 如果设置逻辑删除,对逻辑删除的数据不进行操作
     *
     * @param obj      参数中值不为空的属性作为AND条件
     * @param pageNum  页数
     * @param pageSize 条数
     * @return 分页对象, 内包含分页信息和查询到的数据
     */
    public PageInfo<T> findPage(Object obj, int pageNum, int pageSize) {
        FastExample<T> example = new FastExample<>(fastDaoPojoClass);
        example.equalObject(obj);
        return example.dao().findPage(pageNum, pageSize);
    }

    /**
     * 通过对象中的主键更新数据,参数为空则不进行更新
     * 如果设置逻辑删除,对逻辑删除的数据不进行操作
     *
     * @param pojo 需要更新的数据,对象中必须有主键参数
     * @return 是否更新成功
     */
    public Boolean updateByPrimaryKey(T pojo) {
        return new FastExample<>(fastDaoPojoClass).dao().updateByPrimaryKey(pojo);
    }

    /**
     * 通过对象中的主键更新数据,参数为空则也进行更新
     * 如果设置逻辑删除,对逻辑删除的数据不进行操作
     *
     * @param pojo 需要更新的数据,对象中必须有主键参数
     * @return 是否更新成功
     */
    public Boolean updateByPrimaryKeyOverwrite(T pojo) {
        return new FastExample<>(fastDaoPojoClass).dao().updateByPrimaryKeyOverwrite(pojo);
    }

    /**
     * 通过主键删除
     * 如果设置逻辑删除,对逻辑删除的数据不进行操作
     *
     * @param primaryKey 主键ID
     * @return 是否删除成功
     */
    public Boolean deleteByPrimaryKey(Object primaryKey) {
        return new FastExample<>(fastDaoPojoClass).dao().deleteByPrimaryKey(primaryKey);
    }

}

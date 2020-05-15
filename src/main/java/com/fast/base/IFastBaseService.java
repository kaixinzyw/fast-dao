package com.fast.base;

import com.fast.utils.page.PageInfo;

import java.util.List;

/**
 * @author 张亚伟 https://github.com/kaixinzyw/fast-db-template
 */
 public interface IFastBaseService<T> {
    /**
     * 新增数据
     *
     * @param pojo 需要新增的数据,自动给对象中设置的主键进行赋值
     * @return 是否插入成功
     */
     T insert(T pojo);

     List<T> insertList(List<T> pojos);

    /**
     * 通过主键查询数据
     * 如果设置逻辑删除,对逻辑删除的数据不进行操作
     *
     * @param primaryKeyValue 主键参数
     * @return 查询到的数据结果
     */
     T findByPrimaryKey(Object primaryKeyValue);

    /**
     * 通过查询条件查询一条数据
     * 如果设置逻辑删除,对逻辑删除的数据不进行操作
     *
     * @param obj 参数中值不为空的属性作为AND条件
     * @return 数据结果
     */
     T findOne(Object obj);

    /**
     * 查询表所有的数据
     * 如果设置逻辑删除,对逻辑删除的数据不进行操作
     *
     * @param obj 参数中值不为空的属性作为AND条件
     * @return 查询到的数据结果
     */
     List<T> findAll(Object obj);

    /**
     * 通过查询条件查询所符合要求的数据数量
     * 如果设置逻辑删除,对逻辑删除的数据不进行操作
     *
     * @param obj 参数中值不为空的属性作为AND条件
     * @return 查询到的数据条数
     */
     Integer findCount(Object obj);

    /**
     * 通过查询条件查询所符合要求的数据,并进行分页
     * 如果设置逻辑删除,对逻辑删除的数据不进行操作
     *
     * @param obj      参数中值不为空的属性作为AND条件
     * @param pageNum  页数
     * @param pageSize 条数
     * @return 分页对象, 内包含分页信息和查询到的数据
     */
     PageInfo<T> findPage(Object obj, int pageNum, int pageSize);

    /**
     * 通过对象中的主键更新数据,参数为空则不进行更新
     * 如果设置逻辑删除,对逻辑删除的数据不进行操作
     *
     * @param pojo 需要更新的数据,对象中必须有主键参数
     * @return 是否更新成功
     */
     Boolean updateByPrimaryKey(T pojo);

    /**
     * 通过对象中的主键更新数据,参数为空则也进行更新
     * 如果设置逻辑删除,对逻辑删除的数据不进行操作
     *
     * @param pojo 需要更新的数据,对象中必须有主键参数
     * @return 是否更新成功
     */
     Boolean updateByPrimaryKeyOverwrite(T pojo);

    /**
     * 通过主键逻辑删除标记
     * 如果设置逻辑删除,对逻辑删除的数据不进行操作
     *
     * @param primaryKey 主键ID
     * @return 是否删除成功
     */
     Boolean deleteByPrimaryKey(Object primaryKey);

}

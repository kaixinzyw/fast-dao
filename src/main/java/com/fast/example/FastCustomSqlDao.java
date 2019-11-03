package com.fast.example;

import com.fast.mapper.FastDaoThreadLocalAttributes;
import com.fast.utils.page.PageInfo;

import java.util.List;
import java.util.Map;

/**
 * 自定义SQL执行器
 * @param <T> 自定义SQL操作的对象泛型
 */
public class FastCustomSqlDao<T> {

    /**
     * 自定义SQL执行器初始化,如需使用,必须调用此方法进行初始化创建
     * @param clazz 自定义SQL操作的类
     * @param sql 自定义sql语句,如果有占位符,使用#{参数名}进行描述 例:select * from xxx where userName=${userName}
     * @param params 占位符参数,如果使用占位符进行条件参数封装,必须传入条件参数 如上,需要使用Map put("userName","XXX")
     * @param <T> 自定义SQL操作的对象泛型
     * @return 自定义SQL执行器
     */
    public static <T> FastCustomSqlDao<T> create(Class<T> clazz, String sql, Map<String, Object> params) {
        FastExample<T> fastExample = new FastExample<>(clazz);
        fastExample.conditionPackages().customSQL(sql, params);
        return FastDaoThreadLocalAttributes.fastCustomSqlDao(clazz, fastExample);
    }

    /**
     * 通过查询条件查询一条数据
     *
     * @return 数据结果
     */
    public T findOne() {
        return DaoTemplate.<T>init().findOne();
    }

    /**
     * 通过查询条件查询所符合要求的所有数据
     *
     * @return 数据结果
     */
    public List<T> findAll() {
        return DaoTemplate.<T>init().findAll();
    }

    /**
     * 通过查询条件查询所符合要求的数据数量
     *
     * @return 查询到的数据条数
     */
    public Integer findCount() {
        return DaoTemplate.<T>init().findCount();
    }

    /**
     * 通过查询条件查询所符合要求的数据,并进行分页
     *
     * @param pageNum       页数
     * @param pageSize      条数
     * @param navigatePages 页面数量
     * @return 分页对象, 内包含分页信息和查询到的数据
     */
    public PageInfo<T> findPage(int pageNum, int pageSize, int navigatePages) {
        return DaoTemplate.<T>init().findPage(pageNum, pageSize, navigatePages);
    }

    /**
     * 通过查询条件查询所符合要求的数据,并进行分页,默认9条
     *
     * @param pageNum  页数
     * @param pageSize 条数
     * @return 分页对象, 内包含分页信息和查询到的数据
     */
    public PageInfo<T> findPage(int pageNum, int pageSize) {
        return DaoTemplate.<T>init().findPage(pageNum, pageSize, 9);
    }

    /**
     * 更新数据
     *
     * @return 更新影响到的数据
     */
    public Integer update() {
        return DaoTemplate.<T>init().update(null, true);
    }


    /**
     * 删除数据
     *
     * @return 删除影响到的数据条数
     */
    public Integer delete() {
        return DaoTemplate.<T>init().delete(false);
    }

}

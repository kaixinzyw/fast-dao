package com.fast.example;

import com.fast.mapper.FastDaoThreadLocalAttributes;
import com.fast.utils.page.PageInfo;

import java.util.List;

/**
 * Dao执行类
 * 创建可执行Dao必须使用FastDao.create
 * @author 张亚伟 https://github.com/kaixinzyw
 */
public class FastDao<Pojo> {

    public FastDao() {
    }

    /**
     * 无初始条件的Dao创建
     * @param clazz 需要操作的类
     * @param <T> 操作类的泛型信息
     * @return FastDao
     */
    public static <T> FastDao<T> create(Class<T> clazz) {
        return FastDaoThreadLocalAttributes.fastDao(clazz);
    }

    /**
     * 有初始条件的Dao创建
     * @param clazz 需要操作的类
     * @param fastExample 条件信息
     * @param <T> 操作类的泛型信息
     * @return FastDao
     */
    public static <T> FastDao<T> create(Class<T> clazz, FastExample<T> fastExample) {
        return FastDaoThreadLocalAttributes.fastDao(clazz, fastExample);
    }

    /**
     * 新增数据
     *
     * @param pojo 需要新增的数据,会对框架设置的主键字段进行赋值
     * @return 是否新增成功
     */
    public Boolean insert(Pojo pojo) {
        return DaoTemplate.<Pojo>init().insert(pojo) > 0 ? Boolean.TRUE : Boolean.FALSE;
    }

    /**
     * 通过查询条件查询一条数据
     *
     * @return 数据结果
     */
    public Pojo findOne() {
        return DaoTemplate.<Pojo>init().findOne();
    }

    /**
     * 通过查询条件查询所符合要求的所有数据
     *
     * @return 数据结果
     */
    public List<Pojo> findAll() {
        return DaoTemplate.<Pojo>init().findAll();
    }

    /**
     * 通过查询条件查询所符合要求的数据数量
     *
     * @return 查询到的数据条数
     */
    public Integer findCount() {
        return DaoTemplate.<Pojo>init().findCount();
    }

    /**
     * 通过查询条件查询所符合要求的数据,并进行分页
     *
     * @param pageNum       页数
     * @param pageSize      每页条数
     * @param navigatePages 页面数量
     * @return 分页对象, 内包含分页信息和查询到的数据
     */
    public PageInfo<Pojo> findPage(int pageNum, int pageSize, int navigatePages) {
        return DaoTemplate.<Pojo>init().findPage(pageNum, pageSize, navigatePages);
    }

    /**
     * 通过查询条件查询所符合要求的数据,并进行分页,默认9页
     *
     * @param pageNum  页数
     * @param pageSize 条数
     * @return 分页对象, 内包含分页信息和查询到的数据
     */
    public PageInfo<Pojo> findPage(int pageNum, int pageSize) {
        return DaoTemplate.<Pojo>init().findPage(pageNum, pageSize, 9);
    }

    /**
     * 通过条件更新数据, 参数为空则的字段不会进行更新
     *
     * @param pojo 需要更新的数据 (本操作会对更新时间自动赋值)
     * @return 更新影响到的数据
     */
    public Integer update(Pojo pojo) {
        return DaoTemplate.<Pojo>init().update(pojo, true);
    }

    /**
     * 通过条件更新数据,参数为空则的字段也会进行更新
     *
     * @param pojo 需要更新的数据 (本操作会对更新时间自动赋值)
     * @return 更新影响到的数据
     */
    public Integer updateOverwrite(Pojo pojo) {
        return DaoTemplate.<Pojo>init().update(pojo, false);
    }

    /**
     * 通过主键逻辑删除标记,如果启动了逻辑删除功能,本操作会自动将数据删除标记修改,不会进行物理删除
     *
     * @return 删除影响到的数据条数
     */
    public Integer delete() {
        return DaoTemplate.<Pojo>init().delete(false);
    }

    /**
     * 通过条件物理删除 (本操作会进行物理删除,请谨慎操作)
     *
     * @return 删除影响到的数据条数
     */
    public Integer deleteDisk() {
        return DaoTemplate.<Pojo>init().delete(true);
    }

}

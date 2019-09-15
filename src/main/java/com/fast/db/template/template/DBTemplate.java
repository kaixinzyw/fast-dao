package com.fast.db.template.template;


import com.fast.db.template.utils.page.PageInfo;
import com.fast.db.template.mapper.BeanMapper;

import java.util.List;

/**
 * 查询操作模板
 *
 * @author 张亚伟 398850094@qq.com
 * @version 1.0
 */
public class DBTemplate<Pojo> {

    private Class<Pojo> clazz;
    private FastExample fastExample;

    public DBTemplate(Class<Pojo> clazz) {
        this.clazz = clazz;
    }

    public DBTemplate<Pojo> fastExample(FastExample fastExample) {
        this.fastExample = fastExample;
        return this;
    }

    /**
     * 通过查询条件查询一条数据
     *
     * @return 数据结果
     */
    public Pojo findOne() {
        return BeanMapper.getDBMapper(clazz).findOne(fastExample.criteria.compoundQuery);
    }


    /**
     * 通过查询条件查询所符合要求的所有数据
     *
     * @return 数据结果
     */
    public List<Pojo> findAll() {
        List<Pojo> all = BeanMapper.getDBMapper(clazz).findAll(fastExample.criteria.compoundQuery);
        return all;
    }

    /**
     * 通过查询条件查询所符合要求的数据数量
     *
     * @return 查询到的数据条数
     */
    public Integer findCount() {
        return BeanMapper.getDBMapper(clazz).findCount(fastExample.criteria.compoundQuery);
    }

    /**
     * 通过查询条件查询所符合要求的数据,并进行分页
     *
     * @param pageNum       页数
     * @param pageSize      条数
     * @param navigatePages 页面数量
     * @return 分页对象, 内包含分页信息和查询到的数据
     */
    public PageInfo<Pojo> findPage(int pageNum, int pageSize, int navigatePages) {
        return BeanMapper.getDBMapper(clazz).findPage(fastExample.criteria.compoundQuery, pageNum, pageSize, navigatePages);

    }

    /**
     * 通过查询条件查询所符合要求的数据,并进行分页
     *
     * @param pageNum  页数
     * @param pageSize 条数
     * @return 分页对象, 内包含分页信息和查询到的数据
     */
    public PageInfo<Pojo> findPage(int pageNum, int pageSize) {
        return BeanMapper.getDBMapper(clazz).findPage(fastExample.criteria.compoundQuery, pageNum, pageSize, 8);

    }


    /**
     * 通过条件更新数据,参数为空则的字段也会进行更新
     *
     * @param pojo 需要更新的数据 (本操作会对更新时间自动赋值)
     * @return 更新影响到的数据
     */
    public Integer update(Pojo pojo) {
        return BeanMapper.getDBMapper(clazz).update(pojo, fastExample.criteria.compoundQuery, false);
    }


    /**
     * 通过条件更新数据,参数为空则的字段不会进行更新
     *
     * @param pojo 需要更新的数据 (本操作会对更新时间自动赋值)
     * @return 更新影响到的数据
     */
    public Integer updateSelective(Pojo pojo) {
        return BeanMapper.getDBMapper(clazz).update(pojo, fastExample.criteria.compoundQuery, true);
    }


    /**
     * 通过主键逻辑删除标记 (本操作会自动将数据删除标记修改,不是物理删除,并会对更新时间进行自动赋值)
     *
     * @return 删除影响到的数据条数
     */
    public Integer delete() {
        return BeanMapper.getDBMapper(clazz).delete(fastExample.criteria.compoundQuery, false);
    }

    /**
     * 通过条件物理删除标记 (本操作会进行物理删除,请谨慎操作)
     *
     * @return 删除影响到的数据条数
     */
    public Integer deleteDisk() {
        return BeanMapper.getDBMapper(clazz).delete(fastExample.criteria.compoundQuery, true);
    }


}

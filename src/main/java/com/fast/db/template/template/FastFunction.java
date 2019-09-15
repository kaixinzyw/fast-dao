package com.fast.db.template.template;

import com.fast.db.template.mapper.BeanMapper;

import java.util.*;

public class FastFunction<P> {

    private DBMapper<P> sqlMapper;

    public FastFunction(Class<P> clazz) {
        sqlMapper = BeanMapper.getDBMapper(clazz);
    }

    /**
     * 新增数据
     *
     * @param pojo 需要新增的数据 (自动给主键赋值32位UUID,并添加创建时间和未删除标记,如果主键已有数据,则不进行主键赋值)
     * @return 更新影响的数据条数
     */
    public Boolean insert(P pojo) {
        return sqlMapper.insert(pojo) > 0 ? Boolean.TRUE : Boolean.FALSE;
    }


    /**
     * 通过主键逻辑删除标记
     *
     * @param primaryKey 主键ID (本操作会自动将数据删除标记修改,不是物理删除,并会对更新时间进行自动赋值)
     * @return 是否删除成功
     */
    public Boolean deleteByPrimaryKey(Object primaryKey) {
        return sqlMapper.deleteByPrimaryKey(primaryKey, false) > 0 ? Boolean.TRUE : Boolean.FALSE;

    }

    /**
     * 通过主键逻辑删除标记
     *
     * @param primaryKey 主键ID (本操作会进行数据的物理删除,请谨慎操作)
     * @return 是否删除成功
     */
    public Boolean deleteByPrimaryKeyDisk(Object primaryKey) {
        return sqlMapper.deleteByPrimaryKey(primaryKey, true) > 0 ? Boolean.TRUE : Boolean.FALSE;
    }

    /**
     * 通过主键查询数据
     *
     * @param primaryKey 主键ID
     * @return 查询到的数据结果
     */
    public P findByPrimaryKey(Object primaryKey) {
        return sqlMapper.findByPrimaryKey(primaryKey);
    }

    /**
     * 包含查询,查询指定字段包含指定值的数据结果
     *
     * @param inName   字段名称
     * @param inValues 包含指定参数的集合
     * @return 查询到的数据结果
     */
    public List<P> findByIn(String inName, Collection inValues) {
        FastExample example = new FastExample();
        example.field(inName).in(inValues);
        return sqlMapper.findAll(example.criteria.compoundQuery);
    }

    /**
     * 包含查询,查询指定字段包含指定值的数据结果
     *
     * @param inName   字段名称
     * @param inValues 字段中包含指定值的可变参数
     * @return 查询到的数据结果
     */
    public List<P> findByIn(String inName, Object... inValues) {
        FastExample example = new FastExample();
        example.field(inName).in(inValues);
        return sqlMapper.findAll(example.criteria.compoundQuery);
    }

    /**
     * 查询表所有的数据
     *
     * @return 查询到的数据结果
     */
    public List<P> findAll() {
        List<P> all = sqlMapper.findAll(null);
        return all;
    }

    /**
     * 通过对象中的主键更新数据,参数为空则不进行更新
     *
     * @param pojo 需要更新的数据,对象中必须有主键参数 (会自动对更新时间进行赋值)
     * @return 是否更新成功
     */
    public Boolean updateByPrimaryKeySelective(P pojo) {
        return sqlMapper.updateByPrimaryKey(pojo, true) > 0 ? Boolean.TRUE : Boolean.FALSE;
    }

    /**
     * 通过对象中的主键更新数据,参数为空则也进行更新
     *
     * @param pojo 需要更新的数据,对象中必须有主键参数 (会自动对更新时间进行赋值)
     * @return 是否更新成功
     */
    public Boolean updateByPrimaryKey(P pojo) {
        return sqlMapper.updateByPrimaryKey(pojo, false) > 0 ? Boolean.TRUE : Boolean.FALSE;
    }

}

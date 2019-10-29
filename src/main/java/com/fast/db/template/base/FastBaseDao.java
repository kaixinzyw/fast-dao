package com.fast.db.template.base;

import com.alibaba.fastjson.JSONObject;
import com.fast.db.template.config.FastParams;
import com.fast.db.template.mapper.FastMapperUtil;
import com.fast.db.template.template.FastDao;
import com.fast.db.template.template.FastExample;
import com.fast.db.template.utils.FastValueUtil;
import com.fast.db.template.utils.page.PageInfo;

import java.lang.reflect.ParameterizedType;
import java.util.Collection;
import java.util.List;

/**
 * DAO层基类封装
 *
 * @author 张亚伟 https://github.com/kaixinzyw/fast-db-template
 */

/**
 * DAO层基类封装
 * @param <T> 操作的对象泛型
 * @author 张亚伟 https://github.com/kaixinzyw/fast-db-template
 */
public class FastBaseDao<T> {

    private Class<T> clazz = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];

    /**
     * 新增数据
     *
     * @param pojo 需要新增的数据,自动给对象中设置的主键进行赋值
     * @return 是否插入成功
     */
    public Boolean insert(T pojo) {
        return FastDao.create(clazz).insert(pojo);
    }

    /**
     * 通过主键查询数据
     *
     * @param primaryKeyValue 主键参数
     * @return 查询到的数据结果
     */
    public T findByPrimaryKey(Object primaryKeyValue) {
        return FastMapperUtil.daoTemplate(clazz).findByPrimaryKey(primaryKeyValue);
    }
    /**
     * 通过查询条件查询一条数据
     *
     * @param fastExample 查询条件
     * @return 数据结果
     */
    public T findOne(FastExample<T> fastExample) {
        return FastDao.create(clazz, fastExample).findOne();
    }
    /**
     * 包含查询,查询指定字段包含指定值的数据结果
     *
     * @param inName   字段名称
     * @param inValues 包含指定参数的集合
     * @return 查询到的数据结果
     */
    public List<T> findByIn(String inName, Collection inValues) {
        return new FastExample<>(clazz).field(inName).in(inValues).dao().findAll();
    }
    /**
     * 包含查询,查询指定字段包含指定值的数据结果
     *
     * @param inName   字段名称
     * @param inValues 字段中包含指定值的可变参数
     * @return 查询到的数据结果
     */
    public List<T> findByIn(String inName, Object... inValues) {
        return new FastExample<>(clazz).field(inName).in(inValues).dao().findAll();
    }

    /**
     * 查询表所有的数据
     * @param t 参数中值不为空的属性作为AND条件
     * @return 查询到的数据结果
     */
    public List<T> findAll(T t) {
        return new FastExample<>(clazz,t).dao().findAll();
    }
    /**
     * 通过查询条件查询所符合要求的所有数据
     *
     * @param fastExample 查询条件
     * @return 数据结果
     */
    public List<T> findAll(FastExample<T> fastExample) {
        return FastDao.create(clazz, fastExample).findAll();
    }
    /**
     * 通过查询条件查询所符合要求的数据数量
     *
     * @param fastExample 查询条件
     * @return 查询到的数据条数
     */
    public Integer findCount(FastExample<T> fastExample) {
        return FastDao.create(clazz, fastExample).findCount();
    }
    /**
     * 通过查询条件查询所符合要求的数据,并进行分页
     *
     * @param fastExample 查询条件
     * @param pageNum     页数
     * @param pageSize    条数
     * @return 分页对象, 内包含分页信息和查询到的数据
     */
    public PageInfo<T> findPage(FastExample<T> fastExample, int pageNum, int pageSize) {
        return FastDao.create(clazz, fastExample).findPage(pageNum, pageSize);
    }

    /**
     * 通过对象中的主键更新数据,参数为空则不进行更新
     *
     * @param pojo 需要更新的数据,对象中必须有主键参数 (会自动对更新时间进行赋值)
     * @return 是否更新成功
     */
    public Boolean updateByPrimaryKey(T pojo) {
        return FastMapperUtil.daoTemplate(clazz).updateByPrimaryKey(pojo,true) > 0 ? Boolean.TRUE : Boolean.FALSE;
    }
    /**
     * 通过对象中的主键更新数据,参数为空则也进行更新
     *
     * @param pojo 需要更新的数据,对象中必须有主键参数 (会自动对更新时间进行赋值)
     * @return 是否更新成功
     */
    public Boolean updateByPrimaryKeyOverwrite(T pojo) {
        return FastMapperUtil.daoTemplate(clazz).updateByPrimaryKey(pojo,false) > 0 ? Boolean.TRUE : Boolean.FALSE;
    }
    /**
     * 通过条件更新数据,参数为空则的字段也会进行更新
     *
     * @param pojo        需要更新的数据 (本操作会对更新时间自动赋值)
     * @param fastExample 更新条件
     * @return 更新影响到的数据
     */
    public Integer update(T pojo, FastExample<T> fastExample) {
        return FastDao.create(clazz, fastExample).updateOverwrite(pojo);
    }
    /**
     * 通过条件更新数据,参数为空则的字段不会进行更新
     *
     * @param pojo        需要更新的数据 (本操作会对更新时间自动赋值)
     * @param fastExample 更新条件
     * @return 更新影响到的数据条数
     */
    public Integer updateSelective(T pojo, FastExample<T> fastExample) {
        return FastDao.create(clazz, fastExample).update(pojo);
    }

    /**
     * 通过主键逻辑删除标记
     *
     * @param fastExample 删除条件 (本操作会自动将数据删除标记修改,不是物理删除,并会对更新时间进行自动赋值)
     * @return 删除影响到的数据条数
     */
    public Integer delete(FastExample<T> fastExample) {
        return FastDao.create(clazz, fastExample).delete();
    }
    /**
     * 通过主键物理删除
     *
     * @param fastExample 删除条件
     * @return 删除影响到的数据条数
     */
    public Integer deleteDisk(FastExample<T> fastExample) {
        return FastDao.create(clazz, fastExample).deleteDisk();
    }
    /**
     * 通过主键逻辑删除标记
     *
     * @param primaryKey 主键ID (本操作会自动将数据删除标记修改,不是物理删除,并会对更新时间进行自动赋值)
     * @return 是否删除成功
     */
    public Boolean deleteByPrimaryKey(Object primaryKey) {
        return FastMapperUtil.daoTemplate(clazz).deleteByPrimaryKey(primaryKey,false) > 0 ? Boolean.TRUE : Boolean.FALSE;
    }
    /**
     * 通过主键物理删除标记
     *
     * @param primaryKey 主键ID
     * @return 是否删除成功
     */
    public Boolean deleteByPrimaryKeyDisk(Object primaryKey) {
        return FastMapperUtil.daoTemplate(clazz).deleteByPrimaryKey(primaryKey,true) > 0 ? Boolean.TRUE : Boolean.FALSE;
    }

}

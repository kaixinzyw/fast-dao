package com.fast.fast;

import cn.hutool.aop.ProxyUtil;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.fast.aspect.DaoActuatorAspect;
import com.fast.cache.DataCache;
import com.fast.condition.FastDaoParameterException;
import com.fast.condition.FastExample;
import com.fast.config.FastDaoAttributes;
import com.fast.dao.DaoActuator;
import com.fast.mapper.TableMapper;
import com.fast.mapper.TableMapperUtil;
import com.fast.utils.FastValueUtil;
import com.fast.utils.page.PageInfo;
import io.netty.util.concurrent.FastThreadLocal;

import java.util.*;

/**
 * ORM执行器
 *
 * @author 张亚伟 https://github.com/kaixinzyw
 */
public class DaoTemplate<T> {

    private static final FastThreadLocal<DaoTemplate> daoTemplateThreadLocal = new FastThreadLocal<>();

    /**
     * 条件封装
     */
    private FastExample<T> fastExample;
    /**
     * ORM实现
     */
    private DaoActuator<T> daoActuator;
    /**
     * 操作对象映射
     */
    private TableMapper<T> tableMapper;

    private DaoTemplate() {
    }

    /**
     * 初始化
     *
     * @param <T>         操作对象的泛型信息
     * @param fastExample 条件封装
     * @return ORM执行器
     */
    public static <T> DaoTemplate<T> init(FastExample<T> fastExample) {
        if (fastExample == null) {
            throw new RuntimeException("Fast-Dao初始化失败 FastExample不能为null");
        }
        DaoTemplate<T> template = daoTemplateThreadLocal.get();
        if (template == null) {
            template = new DaoTemplate<>();
            template.daoActuator = ProxyUtil.proxy(FastDaoAttributes.<T>getDaoActuator(), DaoActuatorAspect.class);
            daoTemplateThreadLocal.set(template);
        }
        template.fastExample = fastExample;
        template.tableMapper = TableMapperUtil.getTableMappers(fastExample.getPojoClass());
        FastDaoParam.init(template.tableMapper, template.fastExample);
        return template;
    }


    /**
     * 新增操作
     *
     * @param pojo 需要新增的数据
     * @return 新增结果
     */
    public T insert(T pojo) {
        if (pojo != null) {
            FastValueUtil.setPrimaryKey(pojo, tableMapper);
            FastValueUtil.setCreateTime(pojo, tableMapper);
            FastValueUtil.setNoDelete(pojo, tableMapper);
            FastDaoParam.<T>get().setInsertList(Collections.singletonList(pojo));
        }
        List<T> ts = daoActuator.insert();
        DataCache.upCache(tableMapper);
        if (CollUtil.isEmpty(ts)) {
            return null;
        }
        return ts.get(0);
    }


    /**
     * 新增操作
     *
     * @param ins 需要新增的数据
     * @return 新增结果
     */
    public List<T> insertList(List<T> ins) {
        if (CollUtil.isNotEmpty(ins)) {
            for (T bean : ins) {
                FastValueUtil.setPrimaryKey(bean, tableMapper);
                FastValueUtil.setCreateTime(bean, tableMapper);
                FastValueUtil.setNoDelete(bean, tableMapper);
            }
            FastDaoParam.<T>get().setInsertList(ins);
        }
        List<T> insertList = daoActuator.insert();
        DataCache.upCache(tableMapper);
        return insertList;
    }

    /**
     * 新增操作
     *
     * @param ins  需要新增的数据
     * @param size 每次插入条数
     * @return 新增结果
     */
    public List<T> insertList(List<T> ins, Integer size) {
        if (CollUtil.isEmpty(ins)) {
            return ins;
        }
        for (T bean : ins) {
            FastValueUtil.setPrimaryKey(bean, tableMapper);
            FastValueUtil.setCreateTime(bean, tableMapper);
            FastValueUtil.setNoDelete(bean, tableMapper);
        }
        List<List<T>> inSplit = CollUtil.split(ins, size);
        FastDaoParam<T> daoParam = FastDaoParam.<T>get();
        for (List<T> ts : inSplit) {
            FastDaoParam.<T>get().setInsertList(ts);
            daoActuator.insert();
            FastDaoParam.init(tableMapper, fastExample);
        }
        DataCache.upCache(tableMapper);
        return ins;
    }

    /**
     * 新增或更新操作
     * @param pojo        如果主键有值则进行更新操作,主键为空则进行新增操作
     * @param isSelective 是否进行更新覆盖操作
     * @return 新增结果
     */
    public T insertOrUpdateByPrimaryKey(T pojo, boolean isSelective) {
        String primaryKeyField = tableMapper.getPrimaryKeyField();
        if (StrUtil.isBlank(primaryKeyField)) {
            throw new FastDaoParameterException(tableMapper.getTableName() + ": 未设置主键!!!");
        }
        Object fieldValue = BeanUtil.getFieldValue(pojo, primaryKeyField);
        if (fieldValue == null) {
            return insert(pojo);
        } else {
            updateByPrimaryKey(pojo, isSelective);
            return pojo;
        }
    }

    /**
     * 通过主键查询数据
     * 如果设置逻辑删除,对逻辑删除的数据不进行操作
     *
     * @param primaryKeyValue 主键参数
     * @return 查询到的数据结果
     */
    public T findByPrimaryKey(Object primaryKeyValue) {
        if (primaryKeyValue == null) {
            throw new FastDaoParameterException(tableMapper.getTableName() + ": 主键参数不能为空!!!");
        }
        String primaryKeyField = tableMapper.getPrimaryKeyField();
        if (StrUtil.isBlank(primaryKeyField)) {
            throw new FastDaoParameterException(tableMapper.getTableName() + ": 未设置主键!!!");
        }
        this.fastExample.conditionPackages().init();
        this.fastExample.conditionPackages().addEqualFieldQuery(tableMapper.getPrimaryKeyField(), primaryKeyValue);
        return findOne();
    }

    /**
     * 单条数据查询
     *
     * @return 查询结果
     */
    public T findOne() {
        this.fastExample.conditionPackages().setLimit(1);
        List<T> pojos = findAll();
        if (CollUtil.isNotEmpty(pojos)) {
            return pojos.get(0);
        }
        return null;
    }

    /**
     * 列表数据查询
     *
     * @return 查询结果
     */
    public List<T> findAll() {
        if (FastDaoAttributes.isOpenCache && tableMapper.getCacheType() != null) {
            List<T> list = DataCache.<T>init(tableMapper, fastExample).getList();
            if (list != null) {
                return list;
            }
        }
        List<T> query = daoActuator.select();
        if (FastDaoAttributes.isOpenCache && tableMapper.getCacheType() != null && query != null) {
            DataCache.<T>init(tableMapper, fastExample).setList(query);
        }
        return query;
    }

    /**
     * 数据条数查询
     *
     * @return 查询结果
     */
    public Integer findCount() {
        if (FastDaoAttributes.isOpenCache && tableMapper.getCacheType() != null) {
            Integer one = DataCache.<Integer>init(tableMapper, fastExample).getCount();
            if (one != null) {
                return one;
            }
        }
        Integer count = daoActuator.count();
        if (FastDaoAttributes.isOpenCache && tableMapper.getCacheType() != null && count != null) {
            DataCache.<Integer>init(tableMapper, fastExample).setCount(count);
        }
        return count;
    }

    /**
     * 分页查询
     *
     * @param pageNum       页数
     * @param pageSize      每页条数
     * @param navigatePages 显示页数
     * @return 查询结果
     */
    public PageInfo<T> findPage(Integer pageNum, Integer pageSize, Integer navigatePages) {
        if (pageNum == null || pageNum < 1 || pageSize == null || pageSize < 1 || navigatePages == null || navigatePages < 1) {
            throw new FastDaoParameterException("分页参数错误!!!");
        }
        Integer count = findCount();
        if (count == null || count < 1) {
            return new PageInfo<>(0, pageNum, pageSize, new ArrayList<T>(), navigatePages);
        }
        if (pageNum == 1) {
            this.fastExample.conditionPackages().setPage(0);
        } else {
            this.fastExample.conditionPackages().setPage((pageNum - 1) * pageSize);
        }
        this.fastExample.conditionPackages().setSize(pageSize);
        FastDaoParam.init(tableMapper, fastExample);
        List<T> list = findAll();
        if (list == null) {
            list = new ArrayList<>();
        }
        return new PageInfo<>(count, pageNum, pageSize, list, navigatePages);
    }

    /**
     * 通过对象中的主键更新数据
     * 如果设置逻辑删除,对逻辑删除的数据不进行操作
     *
     * @param t           需要更新的数据,对象中必须有主键参数
     * @param isSelective 是否对null值属性不操作
     * @return 是否更新成功
     */
    public Boolean updateByPrimaryKey(T t, boolean isSelective) {
        if (t == null) {
            throw new FastDaoParameterException(tableMapper.getTableName() + ": 更新数据不能为空!!!");
        }

        String primaryKeyField = tableMapper.getPrimaryKeyField();
        if (StrUtil.isBlank(primaryKeyField)) {
            throw new FastDaoParameterException(tableMapper.getTableName() + ": 未设置主键!!!");
        }

        Object fieldValue = BeanUtil.getFieldValue(t, primaryKeyField);
        if (fieldValue == null) {
            throw new FastDaoParameterException(tableMapper.getTableName() + ": 主键参数不能为空!!!");
        }
        this.fastExample.conditionPackages().init();
        this.fastExample.conditionPackages().addEqualFieldQuery(primaryKeyField, fieldValue);
        return update(t, isSelective) > 0 ? Boolean.TRUE : Boolean.FALSE;
    }


    /**
     * 数据更新
     *
     * @param pojo        需要更新的数据
     * @param isSelective 是否对null值属性不操作
     * @return 更新条数
     */
    public Integer update(T pojo, boolean isSelective) {
        FastDaoParam<T> fastDaoParam = FastDaoParam.get();
        if (CollUtil.isEmpty(this.fastExample.conditionPackages().getConditions()) && StrUtil.isBlank(this.fastExample.conditionPackages().getCustomSql())) {
            Object primaryKeyVal = FastValueUtil.getPrimaryKeyVal(pojo, tableMapper);
            if (primaryKeyVal != null) {
                fastDaoParam.getFastExample().conditionPackages().addEqualFieldQuery(tableMapper.getPrimaryKeyField(), primaryKeyVal);
            } else {
                throw new FastDaoParameterException(tableMapper.getTableName() + ": 更新操作必须设置条件!!!");
            }
        }
        if (pojo != null) {
            FastValueUtil.setUpdateTime(pojo, tableMapper);
        }
        fastDaoParam.setUpdate(pojo);
        fastDaoParam.setUpdateSelective(isSelective);
        return DataCache.upCache(daoActuator.update(), tableMapper);
    }

    /**
     * 通过主键删除数据
     * 如果设置逻辑删除,对逻辑删除的数据不进行操作
     *
     * @param primaryKeyValue 主键参数
     * @return 查询到的数据结果
     */
    public Boolean deleteByPrimaryKey(Object primaryKeyValue) {
        if (primaryKeyValue == null) {
            throw new FastDaoParameterException(tableMapper.getTableName() + ": 主键参数不能为空!!!");
        }
        String primaryKeyField = tableMapper.getPrimaryKeyField();
        if (StrUtil.isBlank(primaryKeyField)) {
            throw new FastDaoParameterException(tableMapper.getTableName() + ": 未设置主键!!!");
        }
        this.fastExample.conditionPackages().init();
        this.fastExample.conditionPackages().addEqualFieldQuery(tableMapper.getPrimaryKeyField(), primaryKeyValue);
        return delete() > 0 ? Boolean.TRUE : Boolean.FALSE;
    }

    /**
     * 删除数据
     *
     * @return 删除条数
     */
    public Integer delete() {
        if (CollUtil.isEmpty(this.fastExample.conditionPackages().getConditions()) && StrUtil.isBlank(this.fastExample.conditionPackages().getCustomSql())) {
            throw new FastDaoParameterException(tableMapper.getTableName() + ": 删除操作必须设置条件!!!");
        }
        if (!FastDaoAttributes.isOpenLogicDelete || !tableMapper.getLogicDelete() || !fastExample.conditionPackages().getLogicDeleteProtect() || fastExample.conditionPackages().getCustomSql() != null) {
            return DataCache.upCache(daoActuator.delete(), tableMapper);
        }

        try {
            FastDaoParam.get().setLogicDelete(true);
            T pojo = tableMapper.getObjClass().newInstance();
            FastValueUtil.setDeleted(pojo, tableMapper);
            return update(pojo, Boolean.TRUE);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


}

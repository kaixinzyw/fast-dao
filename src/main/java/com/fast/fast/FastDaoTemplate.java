package com.fast.fast;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.fast.cache.DataCache;
import com.fast.condition.ConditionPackages;
import com.fast.condition.FastCondition;
import com.fast.condition.FastDaoException;
import com.fast.condition.FastDaoParameterException;
import com.fast.config.FastDaoAttributes;
import com.fast.dao.jdbc.JdbcImpl;
import com.fast.utils.FastValueUtil;
import com.fast.utils.page.PageInfo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * ORM执行器
 *
 * @author 张亚伟 https://github.com/kaixinzyw
 */
public class FastDaoTemplate {

    /**
     * 发现通过主键
     * 通过主键查询数据
     * 如果设置逻辑删除,对逻辑删除的数据不进行操作
     *
     * @param <T>               类型
     * @param primaryKeyValue   主键参数
     * @param conditionPackages 条件的包
     * @return 查询到的数据结果
     */
    public static <T> T findByPrimaryKey(ConditionPackages<T> conditionPackages, Object primaryKeyValue) {
        if (primaryKeyValue == null) {
            throw new FastDaoParameterException(conditionPackages.getTableMapper().getTableName() + ": 主键参数不能为空!!!");
        }
        if (StrUtil.isBlank(conditionPackages.getTableMapper().getPrimaryKeyField())) {
            throw new FastDaoParameterException(conditionPackages.getTableMapper().getTableName() + ": 未设置主键!!!");
        }
        conditionPackages.addEqualFieldQuery(conditionPackages.getTableMapper().getPrimaryKeyField(), primaryKeyValue);
        return findOne(conditionPackages);
    }

    /**
     * 单条数据查询
     *
     * @param <T>               类型
     * @param conditionPackages 条件的包
     * @return 查询结果
     */
    public static <T> T findOne(ConditionPackages<T> conditionPackages) {
        conditionPackages.setLimit(1);
        List<T> pojos = findAll(conditionPackages);
        if (CollUtil.isNotEmpty(pojos)) {
            return pojos.get(0);
        }
        return null;
    }

    /**
     * 列表数据查询
     *
     * @param <T>               类型
     * @param conditionPackages 条件的包
     * @return 查询结果
     */
    public static <T> List<T> findAll(ConditionPackages<T> conditionPackages) {
        if (conditionPackages.getTableMapper().getOpenCache()) {
            List<T> list = DataCache.init(conditionPackages.getTableMapper().getObjClass(), conditionPackages, "findAll").get();
            if (list != null) {
                return list;
            }
        }
        List<T> query = JdbcImpl.select(conditionPackages);
        if (conditionPackages.getTableMapper().getOpenCache()) {
            DataCache.init(conditionPackages.getTableMapper().getObjClass(), conditionPackages, "findAll").set(query);
        }
        return query;
    }

    /**
     * 数据条数查询
     *
     * @param conditionPackages 条件的包
     * @return 查询结果
     */
    public static Integer findCount(ConditionPackages conditionPackages) {
        if (conditionPackages.getTableMapper().getOpenCache()) {
            List<Integer> count = DataCache.init(Integer.class, conditionPackages, "findCount").get();
            if (CollUtil.isNotEmpty(count)) {
                return count.get(0);
            }
        }
        Integer count = JdbcImpl.count(conditionPackages);
        if (conditionPackages.getTableMapper().getOpenCache()) {
            DataCache.init(Integer.class, conditionPackages, "findCount").set(CollUtil.newArrayList(count));
        }
        return count;
    }

    /**
     * 分页查询
     *
     * @param <T>               类型
     * @param pageNum           页数
     * @param pageSize          每页条数
     * @param navigatePages     显示页数
     * @param conditionPackages 条件的包
     * @return 查询结果
     */
    public static <T> PageInfo<T> findPage(ConditionPackages<T> conditionPackages, Integer pageNum, Integer pageSize, Integer navigatePages) {
        if (pageNum == null || pageNum < 1 || pageSize == null || pageSize < 1 || navigatePages == null || navigatePages < 1) {
            throw new FastDaoParameterException("分页参数错误!!!");
        }
        List<FastCondition> tempConditions = BeanUtil.copyToList(conditionPackages.getConditions(), FastCondition.class);
        Integer count = findCount(conditionPackages);
        if (count == null || count < 1) {
            return new PageInfo<>(0, pageNum, pageSize, new ArrayList<T>(), navigatePages);
        }
        if (pageNum == 1) {
            conditionPackages.setPage(0);
        } else {
            conditionPackages.setPage((pageNum - 1) * pageSize);
        }
        conditionPackages.setSize(pageSize);
        conditionPackages.setConditions(tempConditions);
        List<T> list = findAll(conditionPackages);
        if (list == null) {
            list = new ArrayList<>();
        }
        return new PageInfo<>(count, pageNum, pageSize, list, navigatePages);
    }


    /**
     * 新增操作
     *
     * @param <T>               类型
     * @param pojo              需要新增的数据
     * @param conditionPackages 条件的包
     * @return 新增结果
     */
    public static <T> T insert(ConditionPackages<T> conditionPackages, T pojo) {
        if (pojo != null) {
            FastValueUtil.setPrimaryKey(pojo, conditionPackages.getTableMapper());
            FastValueUtil.setCreateTime(pojo, conditionPackages.getTableMapper());
            FastValueUtil.setNoDelete(pojo, conditionPackages.getTableMapper());
        }
        conditionPackages.setInsertList(Collections.singletonList(pojo));
        List<T> ts = JdbcImpl.insert(conditionPackages);
        DataCache.upCache(conditionPackages.getTableMapper());
        if (CollUtil.isEmpty(ts)) {
            return null;
        }
        return ts.get(0);
    }


    /**
     * 新增操作
     *
     * @param <T>               类型
     * @param ins               需要新增的数据
     * @param conditionPackages 条件的包
     * @return 新增结果
     */
    public static <T> List<T> insertList(ConditionPackages<T> conditionPackages, List<T> ins) {
        if (CollUtil.isNotEmpty(ins)) {
            for (T bean : ins) {
                FastValueUtil.setPrimaryKey(bean, conditionPackages.getTableMapper());
                FastValueUtil.setCreateTime(bean, conditionPackages.getTableMapper());
                FastValueUtil.setNoDelete(bean, conditionPackages.getTableMapper());
            }
        } else {
            throw new FastDaoException("新增的数据不能为空");
        }
        conditionPackages.setInsertList(ins);
        List<T> insertList = JdbcImpl.insert(conditionPackages);
        DataCache.upCache(conditionPackages.getTableMapper());
        return insertList;
    }

    /**
     * 新增操作
     *
     * @param <T>               类型
     * @param ins               需要新增的数据
     * @param size              每次插入条数
     * @param conditionPackages 条件的包
     * @return 新增结果
     */
    public static <T> List<T> insertList(ConditionPackages<T> conditionPackages, List<T> ins, Integer size) {
        if (CollUtil.isEmpty(ins)) {
            throw new FastDaoException("新增的数据不能为空");
        }
        for (T bean : ins) {
            FastValueUtil.setPrimaryKey(bean, conditionPackages.getTableMapper());
            FastValueUtil.setCreateTime(bean, conditionPackages.getTableMapper());
            FastValueUtil.setNoDelete(bean, conditionPackages.getTableMapper());
        }
        List<List<T>> inSplit = CollUtil.split(ins, size);
        for (List<T> ts : inSplit) {
            conditionPackages.setInsertList(ts);
            JdbcImpl.insert(conditionPackages);
        }
        DataCache.upCache(conditionPackages.getTableMapper());
        return ins;
    }

    /**
     * 新增或更新操作
     *
     * @param <T>               类型
     * @param pojo              如果主键有值则进行更新操作,主键为空则进行新增操作
     * @param isSelective       是否进行更新覆盖操作
     * @param conditionPackages 条件的包
     * @return 新增结果
     */
    public static <T> T insertOrUpdateByPrimaryKey(ConditionPackages<T> conditionPackages, T pojo, boolean isSelective) {
        String primaryKeyField = conditionPackages.getTableMapper().getPrimaryKeyField();
        if (StrUtil.isBlank(primaryKeyField)) {
            throw new FastDaoParameterException(conditionPackages.getTableMapper().getTableName() + ": 未设置主键!!!");
        }
        Object fieldValue = BeanUtil.getFieldValue(pojo, primaryKeyField);
        if (fieldValue == null) {
            return insert(conditionPackages, pojo);
        } else {
            updateByPrimaryKey(conditionPackages, pojo, isSelective);
            return pojo;
        }
    }


    /**
     * 通过对象中的主键更新数据
     * 如果设置逻辑删除,对逻辑删除的数据不进行操作
     *
     * @param <T>               类型
     * @param t                 需要更新的数据,对象中必须有主键参数
     * @param isSelective       是否对null值属性不操作
     * @param conditionPackages 条件的包
     * @return 是否更新成功
     */
    public static <T> Boolean updateByPrimaryKey(ConditionPackages<T> conditionPackages, T t, boolean isSelective) {
        if (t == null) {
            throw new FastDaoParameterException(conditionPackages.getTableMapper().getTableName() + ": 更新数据不能为空!!!");
        }
        String primaryKeyField = conditionPackages.getTableMapper().getPrimaryKeyField();
        if (StrUtil.isBlank(primaryKeyField)) {
            throw new FastDaoParameterException(conditionPackages.getTableMapper().getTableName() + ": 未设置主键!!!");
        }
        Object fieldValue = BeanUtil.getFieldValue(t, primaryKeyField);
        if (fieldValue == null) {
            throw new FastDaoParameterException(conditionPackages.getTableMapper().getTableName() + ": 主键参数不能为空!!!");
        }
        conditionPackages.addEqualFieldQuery(primaryKeyField, fieldValue);
        return update(conditionPackages, t, isSelective) > 0 ? Boolean.TRUE : Boolean.FALSE;
    }


    /**
     * 数据更新
     *
     * @param <T>               类型
     * @param pojo              需要更新的数据
     * @param isSelective       是否对null值属性不操作
     * @param conditionPackages 条件的包
     * @return 更新条数
     */
    public static <T> Integer update(ConditionPackages<T> conditionPackages, T pojo, boolean isSelective) {
        if (CollUtil.isEmpty(conditionPackages.getConditions()) && StrUtil.isBlank(conditionPackages.getCustomSql())) {
            Object primaryKeyVal = FastValueUtil.getPrimaryKeyVal(pojo, conditionPackages.getTableMapper());
            if (primaryKeyVal != null) {
                conditionPackages.addEqualFieldQuery(conditionPackages.getTableMapper().getPrimaryKeyField(), primaryKeyVal);
            } else {
                throw new FastDaoParameterException(conditionPackages.getTableMapper().getTableName() + ": 更新操作必须设置条件!!!");
            }
        }
        if (pojo != null) {
            FastValueUtil.setUpdateTime(pojo, conditionPackages.getTableMapper());
        }
        conditionPackages.setUpdate(pojo);
        conditionPackages.setUpdateSelective(isSelective);
        return DataCache.upCache(JdbcImpl.update(conditionPackages), conditionPackages.getTableMapper());
    }

    /**
     * 通过主键删除数据
     * 如果设置逻辑删除,对逻辑删除的数据不进行操作
     *
     * @param <T>               类型
     * @param primaryKeyValue   主键参数
     * @param conditionPackages 条件的包
     * @return 查询到的数据结果
     */
    public static <T> Boolean deleteByPrimaryKey(ConditionPackages<T> conditionPackages, Object primaryKeyValue) {
        if (primaryKeyValue == null) {
            throw new FastDaoParameterException(conditionPackages.getTableMapper().getTableName() + ": 主键参数不能为空!!!");
        }
        String primaryKeyField = conditionPackages.getTableMapper().getPrimaryKeyField();
        if (StrUtil.isBlank(primaryKeyField)) {
            throw new FastDaoParameterException(conditionPackages.getTableMapper().getTableName() + ": 未设置主键!!!");
        }
        conditionPackages.addEqualFieldQuery(conditionPackages.getTableMapper().getPrimaryKeyField(), primaryKeyValue);
        return delete(conditionPackages) > 0 ? Boolean.TRUE : Boolean.FALSE;
    }

    /**
     * 删除数据
     *
     * @param conditionPackages 条件的包
     * @param <T>               接收类型
     * @return 删除条数
     */
    public static <T> Integer delete(ConditionPackages<T> conditionPackages) {
        if (CollUtil.isEmpty(conditionPackages.getConditions()) && StrUtil.isBlank(conditionPackages.getCustomSql())) {
            throw new FastDaoParameterException(conditionPackages.getTableMapper().getTableName() + ": 删除操作必须设置条件!!!");
        }
        if (!FastDaoAttributes.isOpenLogicDelete || !conditionPackages.getTableMapper().getLogicDelete() || !conditionPackages.getLogicDeleteProtect() || conditionPackages.getCustomSql() != null) {
            return DataCache.upCache(JdbcImpl.delete(conditionPackages), conditionPackages.getTableMapper());
        }

        try {
            conditionPackages.setLogicDelete(true);
            T pojo = (T) conditionPackages.getTableMapper().getObjClass().newInstance();
            FastValueUtil.setDeleted(pojo, conditionPackages.getTableMapper());
            return update(conditionPackages, pojo, Boolean.TRUE);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}

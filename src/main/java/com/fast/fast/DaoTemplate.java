package com.fast.fast;

import cn.hutool.aop.ProxyUtil;
import cn.hutool.core.collection.CollUtil;
import com.fast.aspect.DaoActuatorAspect;
import com.fast.cache.DataCache;
import com.fast.condition.FastExample;
import com.fast.config.FastDaoAttributes;
import com.fast.dao.DaoActuator;
import com.fast.dao.utils.FastDeleteProvider;
import com.fast.dao.utils.FastInsertProvider;
import com.fast.dao.utils.FastSelectProvider;
import com.fast.dao.utils.FastUpdateProvider;
import com.fast.mapper.TableMapper;
import com.fast.mapper.TableMapperUtil;
import com.fast.utils.FastValueUtil;
import com.fast.utils.page.PageInfo;
import io.netty.util.concurrent.FastThreadLocal;

import java.util.ArrayList;
import java.util.List;

/**
 * ORM执行器
 *
 * @author 张亚伟 https://github.com/kaixinzyw
 */
public class DaoTemplate<T> {

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
     * @param <T> 操作对象的泛型信息
     * @return ORM执行器
     */
    private static final FastThreadLocal<DaoTemplate> daoTemplateThreadLocal = new FastThreadLocal<>();

    public static <T> DaoTemplate<T> init(Class<T> clazz, FastExample<T> fastExample) {
        DaoTemplate<T> template = daoTemplateThreadLocal.get();
        if (template == null) {
            template = new DaoTemplate<>();
            template.daoActuator = ProxyUtil.proxy(FastDaoAttributes.<T>getDaoActuator(), DaoActuatorAspect.class);
            daoTemplateThreadLocal.set(template);
        }
        template.fastExample = fastExample;
        template.tableMapper = TableMapperUtil.getTableMappers(clazz);
        FastDaoParam.init(template.tableMapper, template.fastExample);
        return template;
    }


    /**
     * 新增操作
     *
     * @param pojo 需要新增的数据
     * @return 新增结果
     */
    public Object insert(T pojo) {
        if (pojo != null) {
            FastValueUtil.setPrimaryKey(pojo, tableMapper);
            FastValueUtil.setCreateTime(pojo);
            FastValueUtil.setNoDelete(pojo);
        } else {
            return 0;
        }
        FastDaoParam<T> daoParam = FastDaoParam.get();
        daoParam.setInsert(pojo);
        FastInsertProvider.insert(daoParam);

        Object insert = daoActuator.insert();
        if (insert == null) {
            DataCache.upCache(tableMapper);
        }
        return insert;
    }

    /**
     * 新增操作
     *
     * @param pojos 需要新增的数据
     * @return 新增结果
     */
    public Object insertList(List<T> pojos) {
        if (CollUtil.isNotEmpty(pojos)) {
            for (T pojo : pojos) {
                FastValueUtil.setPrimaryKey(pojo, tableMapper);
                FastValueUtil.setCreateTime(pojo);
                FastValueUtil.setNoDelete(pojo);
            }
        } else {
            return 0;
        }
        FastDaoParam<T> daoParam = FastDaoParam.get();
        daoParam.setInsertList(pojos);
        FastInsertProvider.insertList(daoParam);

        Object insertList = daoActuator.insertList();
        if (insertList != null) {
            DataCache.upCache(tableMapper);
        }
        return insertList;
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
        FastSelectProvider.findAll(FastDaoParam.get());
        List<T> query = daoActuator.findAll();
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
        FastSelectProvider.findCount(FastDaoParam.get());
        Integer count = daoActuator.findCount();

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
    public PageInfo<T> findPage(int pageNum, int pageSize, int navigatePages) {
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
        FastDaoParam.get().setSql(null);
        List<T> list = findAll();
        if (list == null) {
            list = new ArrayList<>();
        }
        return new PageInfo<>(count, pageNum, pageSize, list, navigatePages);
    }

    /**
     * 数据更新
     *
     * @param pojo        需要更新的数据
     * @param isSelective 是否对null值属性不操作
     * @return 更新条数
     */
    public Integer update(T pojo, boolean isSelective) {
        FastValueUtil.setUpdateTime(pojo);
        FastDaoParam<T> fastDaoParam = FastDaoParam.get();
        fastDaoParam.setUpdate(pojo);
        fastDaoParam.setUpdateSelective(isSelective);
        FastUpdateProvider.update(fastDaoParam);
        return DataCache.upCache(daoActuator.update(), tableMapper);
    }


    /**
     * 删除数据
     *
     * @param isDiskDelete 是否启用逻辑删除,如果启用逻辑删除的话必须开启逻辑删除功能
     * @return 删除条数
     */
    public Integer delete(boolean isDiskDelete) {
        if (!isDiskDelete && !FastDaoAttributes.isOpenLogicDelete) {
            isDiskDelete = true;
        }
        if (isDiskDelete) {
            FastDeleteProvider.delete(FastDaoParam.get());
            return DataCache.upCache(daoActuator.delete(), tableMapper);
        }
        try {
            FastDaoParam.get().setLogicDelete(true);
            T pojo = tableMapper.getObjClass().newInstance();
            FastValueUtil.setDeleted(pojo);
            return update(pojo, Boolean.TRUE);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}

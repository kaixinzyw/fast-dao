package com.fast.db.template.template;

import cn.hutool.core.collection.CollUtil;
import com.fast.db.template.cache.DataCache;
import com.fast.db.template.config.FastParams;
import com.fast.db.template.dao.DaoActuator;
import com.fast.db.template.mapper.FastMapperUtil;
import com.fast.db.template.mapper.TableMapper;
import com.fast.db.template.utils.FastValueUtil;
import com.fast.db.template.utils.page.PageInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * ORM执行器
 *
 * @author 张亚伟 https://github.com/kaixinzyw/fast-db-template
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

    /**
     * 初始化
     *
     * @param <T> 操作对象的泛型信息
     * @return ORM执行器
     */
    public static <T> DaoTemplate<T> init() {
        FastMapperUtil<T> fastMapperUtil = FastMapperUtil.get();
        DaoTemplate<T> daoTemplate = fastMapperUtil.getDaoTemplate();
        daoTemplate.setFastExample(fastMapperUtil.getFastExample());
        daoTemplate.setTableMapper(fastMapperUtil.getTableMapper());
        return daoTemplate;
    }

    public DaoTemplate(DaoActuator<T> daoActuator) {
        this.daoActuator = daoActuator;
    }

    /**
     * 新增操作
     *
     * @param pojo 需要新增的数据
     * @return 新增结果
     */
    public Integer insert(T pojo) {
        if (pojo != null) {
            FastValueUtil.setPrimaryKey(pojo);
            FastValueUtil.setCreateTime(pojo);
            FastValueUtil.setNoDelete(pojo);
        }
        Integer insert = daoActuator.insert(pojo);
        return updateCache(insert);
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
        if (FastParams.isOpenCache && tableMapper.getCacheType() != null) {
            List<T> list = DataCache.<T>init().getList();
            if (list != null) {
                return list;
            }
        }

        List<T> query = daoActuator.findAll();

        if (FastParams.isOpenCache && tableMapper.getCacheType() != null && query != null) {
            DataCache.<T>init().setList(query);
        }
        return query;
    }

    /**
     * 数据条数查询
     *
     * @return 查询结果
     */
    public Integer findCount() {
        if (FastParams.isOpenCache && tableMapper.getCacheType() != null) {
            Integer one = DataCache.<Integer>init().getCount();
            if (one != null) {
                return one;
            }
        }

        Integer count = daoActuator.findCount();

        if (FastParams.isOpenCache && tableMapper.getCacheType() != null && count != null) {
            DataCache.<Integer>init().setCount(count);
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
        return updateCache(daoActuator.update(pojo, isSelective));
    }

    /**
     * 删除数据
     *
     * @param isDiskDelete 是否启用逻辑删除,如果启用逻辑删除的话必须开启逻辑删除功能
     * @return 删除条数
     */
    public Integer delete(boolean isDiskDelete) {
        if (!isDiskDelete && !FastParams.isOpenLogicDelete) {
            throw new RuntimeException("fast-db-template未启用逻辑删除功能,如需使用请在配置文件中添加fast.db.set.delete=指定表列名,指定列数据类型只能使用bit");
        }
        if (isDiskDelete) {
            return updateCache(daoActuator.delete());
        }
        try {
            T pojo = tableMapper.getObjClass().newInstance();
            FastValueUtil.setDeleted(pojo);
            return update(pojo, Boolean.TRUE);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;

    }

    /**
     * 缓存刷新
     *
     * @param updateCount SQL执行结果
     * @return SQL执行结果
     */
    private int updateCache(Integer updateCount) {
        if (updateCount < 1 || !FastParams.isOpenCache) {
            return updateCount;
        }
        DataCache.upCacheVersion(tableMapper);
        return updateCount;
    }

    public FastExample<T> getFastExample() {
        return fastExample;
    }

    public void setFastExample(FastExample<T> fastExample) {
        this.fastExample = fastExample;
    }

    public TableMapper<T> getTableMapper() {
        return tableMapper;
    }

    public void setTableMapper(TableMapper<T> tableMapper) {
        this.tableMapper = tableMapper;
    }
}

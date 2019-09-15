package com.fast.db.template.template;


import cn.hutool.core.bean.BeanUtil;
import com.fast.db.template.config.AutomaticParameterAttributes;
import com.fast.db.template.mapper.FastClassTableMapper;
import com.fast.db.template.utils.page.PageInfo;
import com.fast.db.template.cache.FastCacheTemplate;
import com.fast.db.template.implement.FastDBActuator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


/**
 * @author 张亚伟 398850094@qq.com
 */
public class DBMapper<Pojo> {

    private FastClassTableMapper fastClassTableMapper;
    private FastDBActuator<Pojo> dbActuator;
    private DBTemplate<Pojo> dbTemplate;


    public DBMapper(FastClassTableMapper fastClassTableMapper, FastDBActuator<Pojo> dbActuator) {
        this.fastClassTableMapper = fastClassTableMapper;
        this.dbActuator = dbActuator;
        dbTemplate = new DBTemplate<>(fastClassTableMapper.getObjClass());
    }

    public DBTemplate<Pojo> getDbTemplate() {
        return dbTemplate;
    }

    public FastClassTableMapper getFastClassTableMapper() {
        return fastClassTableMapper;
    }

    public FastDBActuator<Pojo> getDbActuator() {
        return dbActuator;
    }

    public Integer insert(Pojo pojo) {
        FastSQLUtil.setPrimaryKey(pojo);
        FastSQLUtil.setCreateTime(pojo);
        FastSQLUtil.setNoDelete(pojo);
        Integer insert = dbActuator.insert(pojo);
        return updateCache(insert);
    }


    public Pojo findByPrimaryKey(Object primaryKey) {
        if (AutomaticParameterAttributes.isOpenCache && fastClassTableMapper.getCacheType() != null) {
            Pojo one = new FastCacheTemplate<Pojo>(fastClassTableMapper, primaryKey.toString()).getOne();
            if (one != null) {
                return one;
            }
        }
        Pojo pojo = dbActuator.findByPrimaryKey(primaryKey);
        if (AutomaticParameterAttributes.isOpenCache && fastClassTableMapper.getCacheType() != null && pojo != null) {
            new FastCacheTemplate<Pojo>(fastClassTableMapper, primaryKey.toString()).setOne(pojo);
        }
        return pojo;
    }

    public Pojo findOne(CompoundQuery compoundQuery) {
        if (AutomaticParameterAttributes.isOpenCache && (fastClassTableMapper.getCacheType() != null)) {
            Pojo one = new FastCacheTemplate<Pojo>(fastClassTableMapper, compoundQuery).getOne();

            if (one != null) {
                return one;
            }
        }


        Pojo pojo = dbActuator.findOne(compoundQuery);
        if (AutomaticParameterAttributes.isOpenCache && fastClassTableMapper.getCacheType() != null && pojo != null) {
            new FastCacheTemplate<Pojo>(fastClassTableMapper, compoundQuery).setOne(pojo);
        }
        return pojo;

    }

    public List<Pojo> findByIn(String inName, Collection inValues) {
        FastExample fastExample = new FastExample();
        fastExample.field(inName).in(inValues);
        List<Pojo> list = findAll(fastExample.criteria.compoundQuery);
        return list;
    }

    public List<Pojo> findAll(CompoundQuery compoundQuery) {

        if (AutomaticParameterAttributes.isOpenCache && fastClassTableMapper.getCacheType() != null) {
            List<Pojo> list = new FastCacheTemplate<Pojo>(fastClassTableMapper, compoundQuery).getList();
            if (list != null) {
                return list;
            }
        }

        List<Pojo> query = dbActuator.findAll(compoundQuery);

        if (AutomaticParameterAttributes.isOpenCache && fastClassTableMapper.getCacheType() != null && query != null) {
            new FastCacheTemplate<Pojo>(fastClassTableMapper, compoundQuery).setList(query);
        }
        return query;
    }

    public Integer findCount(CompoundQuery compoundQuery) {
        if (AutomaticParameterAttributes.isOpenCache && fastClassTableMapper.getCacheType() != null) {
            Integer one = new FastCacheTemplate<Integer>(fastClassTableMapper, compoundQuery, "findCount").getOne();
            if (one != null) {
                return one;
            }

        }

        Integer count = dbActuator.findCount(compoundQuery);

        if (AutomaticParameterAttributes.isOpenCache && fastClassTableMapper.getCacheType() != null && count != null) {
            new FastCacheTemplate<Integer>(fastClassTableMapper, compoundQuery, "findCount").setOne(count);
        }
        return count;
    }

    public PageInfo<Pojo> findPage(CompoundQuery compoundQuery, int pageNum, int pageSize, int navigatePages) {

        Integer count = findCount(compoundQuery);
        if (count == null || count < 1) {
            return new PageInfo<>(0, pageNum, pageSize, new ArrayList<>(), navigatePages);
        }

        if (pageNum == 1) {
            compoundQuery.setPage(0);
        } else {
            compoundQuery.setPage((pageNum - 1) * pageSize);
        }
        compoundQuery.setSize(pageSize);

        if (AutomaticParameterAttributes.isOpenCache && fastClassTableMapper.getCacheType() != null) {
            List<Pojo> list = new FastCacheTemplate<Pojo>(fastClassTableMapper, compoundQuery).getList();
            if (list != null) {
                return new PageInfo<>(count, pageNum, pageSize, list, navigatePages);
            }
        }


        List<Pojo> list = findAll(compoundQuery);
        if (AutomaticParameterAttributes.isOpenCache && fastClassTableMapper.getCacheType() != null && list != null) {
            new FastCacheTemplate<Pojo>(fastClassTableMapper, compoundQuery).setList(list);
        }
        return new PageInfo<>(count, pageNum, pageSize, list, navigatePages);

    }

    public Integer update(Pojo pojo, CompoundQuery compoundQuery, boolean isSelective) {
        FastSQLUtil.setUpdateTime(pojo);
        return updateCache(dbActuator.update(pojo, compoundQuery, isSelective));

    }

    public Integer updateByPrimaryKey(Pojo pojo, boolean isSelective) {
        FastSQLUtil.setUpdateTime(pojo);
        return updateCache(dbActuator.updateByPrimaryKey(pojo, isSelective));
    }

    public Integer delete(CompoundQuery compoundQuery, boolean isDiskDelete) {
        if (isDiskDelete) {
            return updateCache(dbActuator.delete(compoundQuery));
        }
        if (!AutomaticParameterAttributes.isOpenLogicDelete) {
            throw new RuntimeException("未启用逻辑删除!!!");
        }
        try {
            Pojo pojo = (Pojo) fastClassTableMapper.getObjClass().newInstance();
            FastSQLUtil.setDeleted(pojo);
            return updateCache(update(pojo, compoundQuery, Boolean.TRUE));
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return 0;

    }


    public Integer deleteByPrimaryKey(Object primaryKey, boolean isDiskDelete) {
        if (isDiskDelete) {
            return dbActuator.deleteByPrimaryKey(primaryKey);
        }
        if (!AutomaticParameterAttributes.isOpenLogicDelete) {
            throw new RuntimeException("未启用逻辑删除!!!");
        }
        try {
            Pojo pojo = (Pojo) fastClassTableMapper.getObjClass().newInstance();
            FastSQLUtil.setDeleted(pojo);
            BeanUtil.setFieldValue(pojo, fastClassTableMapper.getPrimaryKeyField(), primaryKey);
            return updateCache(updateByPrimaryKey(pojo, Boolean.TRUE));
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        return 0;
    }

    private int updateCache(Integer updateCount) {
        if (updateCount < 1 || !AutomaticParameterAttributes.isOpenCache) {
            return updateCount;
        }
        FastCacheTemplate.upCacheVersion(fastClassTableMapper.getTableName());
        return updateCount;
    }

}

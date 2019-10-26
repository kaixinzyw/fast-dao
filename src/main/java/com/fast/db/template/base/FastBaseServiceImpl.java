package com.fast.db.template.base;

import com.fast.db.template.template.FastExample;
import com.fast.db.template.utils.page.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collection;
import java.util.List;

/**
 * @author 张亚伟 https://github.com/kaixinzyw/fast-db-template
 */
public class FastBaseServiceImpl<T> {

    @Autowired
    private FastBaseDao<T> fastBaseDao;

    public Boolean insert(T pojo) {
        return fastBaseDao.insert(pojo);
    }

    public T findByPrimaryKey(Object primaryKey) {
        return fastBaseDao.findByPrimaryKey(primaryKey);
    }
    public List<T> findByIn(String inName, Collection inValues) {
        return fastBaseDao.findByIn(inName, inValues);
    }
    public List<T> findByIn(String inName, Object... inValues) {
        return fastBaseDao.findByIn(inName, inValues);
    }
    public T findOne(FastExample<T> fastExample) {
        return fastBaseDao.findOne(fastExample);
    }
    public List<T> findAll(T t) {
        return fastBaseDao.findAll(t);
    }
    public List<T> findAll(FastExample<T> fastExample) {
        return fastBaseDao.findAll(fastExample);
    }
    public Integer findCount(FastExample<T> fastExample) {
        return fastBaseDao.findCount(fastExample);
    }
    public PageInfo<T> findPage(FastExample<T> fastExample, int pageNum, int pageSize) {
        return fastBaseDao.findPage(fastExample, pageNum, pageSize);
    }

    public Boolean updateByPrimaryKey(T pojo) {
        return fastBaseDao.updateByPrimaryKey(pojo);
    }
    public Boolean updateByPrimaryKeyOverwrite(T pojo) {
        return fastBaseDao.updateByPrimaryKeyOverwrite(pojo);
    }
    public Integer update(T pojo, FastExample<T> fastExample) {
        return fastBaseDao.update(pojo, fastExample);
    }
    public Integer updateSelective(T pojo, FastExample<T> fastExample) {
        return fastBaseDao.updateSelective(pojo, fastExample);
    }

    public Boolean deleteByPrimaryKey(Object primaryKey) {
        return fastBaseDao.deleteByPrimaryKey(primaryKey);
    }
    public Boolean deleteByPrimaryKeyDisk(Object primaryKey) {
        return fastBaseDao.deleteByPrimaryKeyDisk(primaryKey);
    }
    public Integer delete(FastExample<T> fastExample) {
        return fastBaseDao.delete(fastExample);
    }
    public Integer deleteDisk(FastExample<T> fastExample) {
        return fastBaseDao.deleteDisk(fastExample);
    }
}

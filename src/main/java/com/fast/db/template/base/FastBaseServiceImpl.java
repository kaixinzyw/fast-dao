package com.fast.db.template.base;

import com.fast.db.template.template.FastExample;
import com.fast.db.template.utils.page.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collection;
import java.util.List;

/**
 * @author 张亚伟 398850094@qq.com
 */
public class FastBaseServiceImpl<T> {

    @Autowired
    private FastBaseDao<T> fastBaseDao;

    public Boolean insert(T pojo) {
        return fastBaseDao.insert(pojo);
    }


    public Boolean deleteByPrimaryKey(Object primaryKey) {
        return fastBaseDao.deleteByPrimaryKey(primaryKey);
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

    public List<T> findAll() {
        return fastBaseDao.findAll();
    }

    public Boolean updateByPrimaryKeySelective(T pojo) {
        return fastBaseDao.updateByPrimaryKeySelective(pojo);
    }

    public Boolean updateByPrimaryKey(T pojo) {
        return fastBaseDao.updateByPrimaryKey(pojo);
    }

    public T findOne(FastExample fastExample) {
        return fastBaseDao.findOne(fastExample);
    }

    public List<T> findAll(FastExample fastExample) {
        return fastBaseDao.findAll(fastExample);
    }

    public Integer findCount(FastExample fastExample) {
        return fastBaseDao.findCount(fastExample);
    }

    public PageInfo<T> findPage(FastExample fastExample, int pageNum, int pageSize) {
        return fastBaseDao.findPage(fastExample, pageNum, pageSize);
    }

    public Integer update(T pojo, FastExample fastExample) {
        return fastBaseDao.update(pojo, fastExample);
    }

    public Integer updateSelective(T pojo, FastExample fastExample) {
        return fastBaseDao.updateSelective(pojo, fastExample);
    }

    public Integer delete(FastExample fastExample) {
        return fastBaseDao.delete(fastExample);
    }
}

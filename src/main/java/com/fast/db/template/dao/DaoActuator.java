package com.fast.db.template.dao;

import java.util.List;

/**
 * FastDB 执行器接口
 *
 * @author 张亚伟 https://github.com/kaixinzyw/fast-db-template
 */
public interface DaoActuator<T> {

    Integer insert(T pojo);

    List<T> findAll();

    Integer findCount();

    Integer update(T pojo, boolean isSelective);

    Integer delete();
}

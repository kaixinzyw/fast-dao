package com.fast.dao;

import java.util.List;

/**
 * FastDB 执行器接口
 *
 * @author 张亚伟 https://github.com/kaixinzyw
 */
public interface DaoActuator<T> {

    T insert();

    List<T> insertList();

    List<T> findAll();

    Integer findCount();

    Integer update();

    Integer delete();
}

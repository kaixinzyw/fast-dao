package com.fast.aspect;

import com.fast.condition.ConditionPackages;

import java.util.List;

public interface FastDaoExpander {

    /**
     * Dao执行前的操作
     *
     * @param conditionPackages 目标对象
     */
    void before(ConditionPackages conditionPackages);

    /**
     * Dao执行后的操作
     *
     * @param conditionPackages 目标对象
     */
    void after(ConditionPackages conditionPackages);

    /**
     * 执行场景
     * @return INSERT,SELECT,UPDATE,DELETE
     */
    List<ExpanderOccasion> occasion();


}

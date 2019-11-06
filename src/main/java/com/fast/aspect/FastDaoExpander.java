package com.fast.aspect;

import com.fast.fast.FastDaoParam;

import java.util.List;

public interface FastDaoExpander {

    /**
     * Dao执行前的操作
     *
     * @param param 目标对象
     * @return 是否继续执行接下来的操作
     */
    boolean before(FastDaoParam param);

    /**
     * Dao执行后的操作
     *
     * @param param 目标对象
     */
    void after(FastDaoParam param);

    /**
     * 执行场景
     * @return INSERT,SELECT,UPDATE,DELETE
     */
    List<ExpanderOccasion> occasion();


}

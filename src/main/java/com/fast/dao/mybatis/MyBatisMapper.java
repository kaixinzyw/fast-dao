package com.fast.dao.mybatis;

import com.fast.fast.FastDaoParam;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Map;

/**
 * MyBatis映射类
 *
 * @author 张亚伟 https://github.com/kaixinzyw
 */
public interface MyBatisMapper {


    @InsertProvider(type = MyBatisProvider.class, method = "getSql")
    Integer insert(FastDaoParam param);

    @InsertProvider(type = MyBatisProvider.class, method = "getSql")
    @Options(useGeneratedKeys = true, keyProperty = "returnVal")
    Integer insertPrimaryKeyAuto(FastDaoParam param);

    @SelectProvider(type = MyBatisProvider.class, method = "getSql")
    List<Map<String, Object>> findAll(FastDaoParam param);

    @SelectProvider(type = MyBatisProvider.class, method = "getSql")
    Integer findCount(FastDaoParam param);

    @UpdateProvider(type = MyBatisProvider.class, method = "getSql")
    Integer update(FastDaoParam param);

    @DeleteProvider(type = MyBatisProvider.class, method = "getSql")
    Integer delete(FastDaoParam param);

}

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
public interface FastMyBatisMapper {


    @InsertProvider(type = FastMyBatisInsertProvider.class, method = "insert")
    Integer insert(FastDaoParam param);

    @InsertProvider(type = FastMyBatisInsertProvider.class, method = "insert")
    @Options(useGeneratedKeys = true, keyProperty = "returnVal")
    Integer insertPrimaryKeyAuto(FastDaoParam param);

    @SelectProvider(type = FastMyBatisSelectProvider.class, method = "findAll")
    List<Map<String, Object>> findAll(FastDaoParam param);

    @SelectProvider(type = FastMyBatisSelectProvider.class, method = "findCount")
    Integer findCount(FastDaoParam param);

    @UpdateProvider(type = FastMyBatisUpdateProvider.class, method = "update")
    Integer update(FastDaoParam param);

    @DeleteProvider(type = FastMyBatisDeleteProvider.class, method = "delete")
    Integer delete(FastDaoParam param);

}

package com.fast.db.template.implement.mybatis;

import org.apache.ibatis.annotations.DeleteProvider;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.UpdateProvider;

import java.util.List;
import java.util.Map;

/**
 * MyBatis映射类
 * @author 张亚伟 398850094@qq.com
 */
public interface FastMyBatisMapper {


    @InsertProvider(type = FastMyBatisInsertProvider.class, method = "insert")
    Integer insert(FastMybatisParam param);

    @SelectProvider(type = FastMyBatisSelectProvider.class, method = "findByPrimaryKey")
    Map<String,Object> findByPrimaryKey(FastMybatisParam param);

    @SelectProvider(type = FastMyBatisSelectProvider.class, method = "findOne")
    Map<String, Object> findOne(FastMybatisParam param);

    @SelectProvider(type = FastMyBatisSelectProvider.class, method = "findAll")
    List<Map<String, Object>> findAll(FastMybatisParam param);

    @SelectProvider(type = FastMyBatisSelectProvider.class, method = "findCount")
    Integer findCount(FastMybatisParam param);

    @UpdateProvider(type = FastMyBatisUpdateProvider.class, method = "update")
    Integer update(FastMybatisParam param);

    @UpdateProvider(type = FastMyBatisUpdateProvider.class, method = "updateByPrimaryKey")
    Integer updateByPrimaryKey(FastMybatisParam param);

    @DeleteProvider(type = FastMyBatisDeleteProvider.class, method = "delete")
    Integer delete(FastMybatisParam param);

    @DeleteProvider(type = FastMyBatisDeleteProvider.class, method = "deleteByPrimaryKey")
    Integer deleteByPrimaryKey(FastMybatisParam param);
}

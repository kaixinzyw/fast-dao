package com.fast.db.template.implement.mybatis;

import cn.hutool.core.bean.BeanUtil;
import com.fast.db.template.implement.spring_jdbc.FastSqlUtil;
import com.fast.db.template.mapper.FastClassTableMapper;
import com.fast.db.template.utils.FastSQL;

import java.util.List;
import java.util.Map;

/**
 * 扩展MyBatis 插入方法Sql语句拼接
 * @author 张亚伟 398850094@qq.com
 */
public class FastMyBatisInsertProvider {

    public String insert(FastMybatisParam param){
        FastClassTableMapper fastClassTableMapper = param.getFastClassTableMapper();
        Object pojo = param.getPojo();
        FastSqlUtil util = new FastSqlUtil(fastClassTableMapper);

        FastSQL fastSQL = new FastSQL();
        fastSQL.INSERT_INTO(fastClassTableMapper.getTableName());

        List<String> fieldNames = fastClassTableMapper.getFieldNames();
        Map<String, String> fieldTableNames = fastClassTableMapper.getFieldTableNames();

        for (String fieldName : fieldNames) {
            Object fieldValue = BeanUtil.getFieldValue(pojo, fieldName);
            if (fieldValue != null) {
                fastSQL.VALUES(util.fieldPackage(fieldTableNames.get(fieldName)), "#{pojo."+ fieldName+"}");
            }
        }
        return fastSQL.toString();
    }
}

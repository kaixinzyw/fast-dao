package com.fast.dao.jdbc;


import cn.hutool.core.util.ClassUtil;
import com.alibaba.fastjson.JSONObject;
import com.fast.condition.ConditionPackages;
import com.fast.utils.BeanCopyUtil;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;

/**
 * SpringJDBC 对象映射
 *
 * @author 张亚伟 https://github.com/kaixinzyw
 */
public class JdbcRowMapper<T> implements RowMapper<T> {

    private final Class<T> mapperClass;

    public JdbcRowMapper(ConditionPackages<T> conditionPackages) {
        this.mapperClass = conditionPackages.getTableMapper().getObjClass();
    }

    /**
     * {@inheritDoc}.
     */
    @Override
    public T mapRow(ResultSet rs, int arg1) {
        try {
            if (ClassUtil.isBasicType(mapperClass)) {
                return BeanCopyUtil.copy(rs.getObject(1), mapperClass);
            }
            final ResultSetMetaData metaData = rs.getMetaData();
            int columnLength = metaData.getColumnCount();
            JSONObject jsonObject = new JSONObject();
            for (int i = 1; i <= columnLength; i++) {
                jsonObject.put(metaData.getColumnLabel(i), rs.getObject(i));
            }
            return jsonObject.toJavaObject(mapperClass);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
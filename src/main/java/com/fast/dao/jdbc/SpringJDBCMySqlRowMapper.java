package com.fast.dao.jdbc;


import com.alibaba.fastjson.JSONObject;
import com.fast.mapper.TableMapper;
import com.fast.example.FastDaoParam;
import io.netty.util.concurrent.FastThreadLocal;
import org.springframework.jdbc.core.RowMapper;

import javax.annotation.Nullable;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.Map;

/**
 * SpringJDBC 对象映射
 *
 * @author 张亚伟 https://github.com/kaixinzyw
 */
public class SpringJDBCMySqlRowMapper<T> implements RowMapper<T> {

    private static FastThreadLocal<SpringJDBCMySqlRowMapper> rowMapperThread = new FastThreadLocal<>();

    public static <T> SpringJDBCMySqlRowMapper<T> init(FastDaoParam<T> param) {
        SpringJDBCMySqlRowMapper<T> rowMapper = rowMapperThread.get();
        if (rowMapper == null) {
            rowMapper = new SpringJDBCMySqlRowMapper<>();
            rowMapperThread.set(rowMapper);
        }
        rowMapper.mapper = param.getTableMapper();
        return rowMapper;
    }

    private SpringJDBCMySqlRowMapper() {
    }

    private TableMapper<T> mapper;

    /**
     * {@inheritDoc}.
     */
    @Override
    public T mapRow(@Nullable ResultSet rs, int arg1) {
        try {
            assert rs != null;
            final ResultSetMetaData metaData = rs.getMetaData();
            int columnLength = metaData.getColumnCount();
            JSONObject jsonObject = new JSONObject();
            Map<String, String> tableFieldNames = mapper.getTableFieldNames();
            Map<String, Class> fieldTypes = mapper.getFieldTypes();
            for (int i = 1; i <= columnLength; i++) {
                String columnName = metaData.getColumnName(i);
                String fieldName = tableFieldNames.get(columnName);
                Object val = rs.getObject(columnName);
                if (val != null) {
                    jsonObject.put(fieldName, val);
                }
            }
            return jsonObject.toJavaObject(mapper.getObjClass());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
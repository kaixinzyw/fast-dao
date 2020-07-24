package com.fast.dao.jdbc;


import com.alibaba.fastjson.JSONObject;
import com.fast.mapper.TableMapper;
import com.fast.fast.FastDaoParam;
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
public class JdbcRowMapper<T> implements RowMapper<T> {

    private static FastThreadLocal<JdbcRowMapper> rowMapperThread = new FastThreadLocal<>();

    public static <T> JdbcRowMapper<T> init(FastDaoParam<T> param) {
        JdbcRowMapper<T> rowMapper = rowMapperThread.get();
        if (rowMapper == null) {
            rowMapper = new JdbcRowMapper<>();
            rowMapperThread.set(rowMapper);
        }
        rowMapper.mapper = param.getTableMapper();
        return rowMapper;
    }

    private JdbcRowMapper() {
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
            for (int i = 1; i <= columnLength; i++) {
                String columnName = metaData.getColumnLabel(i);
                String fieldName = tableFieldNames.get(columnName);
                Object val = rs.getObject(columnName);
                if (val != null) {
                    if (fieldName != null) {
                        jsonObject.put(fieldName, val);
                    }else {
                        jsonObject.put(columnName, val);
                    }
                }
            }
            return jsonObject.toJavaObject(mapper.getObjClass());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
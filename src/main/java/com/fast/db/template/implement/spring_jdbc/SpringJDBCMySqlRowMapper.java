package com.fast.db.template.implement.spring_jdbc;


import com.alibaba.fastjson.JSONObject;
import com.fast.db.template.mapper.FastClassTableMapper;
import org.springframework.jdbc.core.RowMapper;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Timestamp;
import java.util.Date;

/**
 * SpringJDBC 对象映射
 * @author 张亚伟 398850094@qq.com
 */
public class SpringJDBCMySqlRowMapper<T> implements RowMapper<T> {

    /**
     * 添加字段注释.
     */
    private FastClassTableMapper mapper;


    public SpringJDBCMySqlRowMapper(FastClassTableMapper mapper) {
        this.mapper = mapper;
    }

    /**
     * {@inheritDoc}.
     */
    @Override
    public T mapRow(ResultSet rs, int arg1) {
        try {
            final ResultSetMetaData metaData = rs.getMetaData();
            int columnLength = metaData.getColumnCount();
            JSONObject jsonObject = new JSONObject();
            for (int i = 1; i <= columnLength; i++) {
                String columnName = metaData.getColumnName(i);
                String fieldName = mapper.getFieldTableNames().get(columnName);
                Class fieldClazz = mapper.getFieldTypes().get(fieldName);

                if (fieldClazz == int.class || fieldClazz == Integer.class) { // int
                    jsonObject.put(fieldName, rs.getInt(columnName));
                } else if (fieldClazz == boolean.class || fieldClazz == Boolean.class) { // boolean
                    jsonObject.put(fieldName, rs.getBoolean(columnName));
                } else if (fieldClazz == String.class) { // string
                    jsonObject.put(fieldName, rs.getString(columnName));
                } else if (fieldClazz == float.class) { // float
                    jsonObject.put(fieldName, rs.getFloat(columnName));
                } else if (fieldClazz == double.class || fieldClazz == Double.class) { // double
                    jsonObject.put(fieldName, rs.getDouble(columnName));
                } else if (fieldClazz == BigDecimal.class) { // bigdecimal
                    jsonObject.put(fieldName, rs.getBigDecimal(columnName));
                } else if (fieldClazz == short.class || fieldClazz == Short.class) { // short
                    jsonObject.put(fieldName, rs.getShort(columnName));
                } else if (fieldClazz == Date.class) { // date
                    jsonObject.put(fieldName, rs.getDate(columnName));
                } else if (fieldClazz == Timestamp.class) { // timestamp
                    jsonObject.put(fieldName, rs.getTimestamp(columnName));
                } else if (fieldClazz == Long.class || fieldClazz == long.class) { // long
                    jsonObject.put(fieldName, rs.getLong(columnName));
                }
            }
            return (T) jsonObject.toJavaObject(mapper.getObjClass());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

}
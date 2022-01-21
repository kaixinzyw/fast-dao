package com.fast.dao.many;


import com.alibaba.fastjson.JSONObject;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.HashMap;
import java.util.Map;

/**
 * SpringJDBC 对象映射
 *
 * @author 张亚伟 https://github.com/kaixinzyw
 */
public class JdbcJoinQueryRowMapper implements RowMapper<Map<String, JSONObject>> {

    /**
     * {@inheritDoc}.
     */
    @Override
    public Map<String, JSONObject> mapRow(ResultSet rs, int arg1) {
        try {
            final ResultSetMetaData metaData = rs.getMetaData();
            int columnLength = metaData.getColumnCount();
            Map<String, JSONObject> resultMap = new HashMap<>();
            for (int i = 1; i <= columnLength; i++) {
                String tableName = metaData.getTableName(i);
                JSONObject json = resultMap.get(tableName);
                if (json == null) {
                    json = new JSONObject();
                    resultMap.put(tableName, json);
                }
                json.put(metaData.getColumnLabel(i), rs.getObject(i));
            }
            return resultMap;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
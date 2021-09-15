package com.fast.dao.jdbc;


import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.fast.fast.FastDaoParam;
import io.netty.util.concurrent.FastThreadLocal;
import org.springframework.jdbc.core.RowMapper;

import javax.annotation.Nullable;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * SpringJDBC 对象映射
 *
 * @author 张亚伟 https://github.com/kaixinzyw
 */
public class JdbcJoinQueryRowMapper implements RowMapper<Map<String, JSONObject>> {

    private static FastThreadLocal<JdbcJoinQueryRowMapper> rowMapperThread = new FastThreadLocal<>();
    public static JdbcJoinQueryRowMapper init() {
        JdbcJoinQueryRowMapper rowMapper = rowMapperThread.get();
        if (rowMapper == null) {
            rowMapper = new JdbcJoinQueryRowMapper();
            rowMapperThread.set(rowMapper);
        }
        return rowMapper;
    }

    /**
     * {@inheritDoc}.
     */
    @Override
    public Map<String, JSONObject> mapRow(@Nullable ResultSet rs, int arg1) {
        try {
            assert rs != null;
            final ResultSetMetaData metaData = rs.getMetaData();
            int columnLength = metaData.getColumnCount();
            Map<String,JSONObject> resultMap = new HashMap<>();
            for (int i = 1; i <= columnLength; i++) {
                String tableName = metaData.getTableName(i);
                String columnLabel = metaData.getColumnLabel(i);
                JSONObject json = resultMap.get(tableName);
                if (json == null) {
                    json = new JSONObject();
                    resultMap.put(tableName,json);
                }
                json.put(columnLabel,rs.getObject(StrUtil.strBuilder(tableName,StrUtil.DOT,columnLabel).toString()));
            }
            return resultMap;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
package com.fast.dao.utils;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import com.fast.config.FastDaoAttributes;
import com.fast.dao.mybatis.FastMyBatisImpl;
import com.fast.mapper.TableMapper;
import com.fast.fast.FastDaoParam;
import com.fast.utils.FastSQL;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 扩展插入方法Sql语句拼接
 *
 * @author 张亚伟 https://github.com/kaixinzyw
 */
public class FastInsertProvider {

    public static void insert(FastDaoParam param) {
        if (StrUtil.isNotEmpty(param.getSql())) {
            return;
        }

        TableMapper tableMapper = param.getTableMapper();
        Object pojo = param.getInsert();

        FastSQL fastSQL = new FastSQL();
        fastSQL.INSERT_INTO(tableMapper.getTableName());

        List<String> fieldNames = tableMapper.getFieldNames();
        Map<String, String> fieldTableNames = tableMapper.getFieldTableNames();
        Map<String, Object> paramMap = param.getParamMap();
        int paramIndex = 0;
        for (String fieldName : fieldNames) {
            Object fieldValue = BeanUtil.getFieldValue(pojo, fieldName);
            if (fieldValue != null) {
                String paramKey = "insert_param_" + paramIndex;
                fastSQL.VALUES("`" + fieldTableNames.get(fieldName) + "`", "#{" + paramKey + "}");
                paramMap.put(paramKey, fieldValue);
                paramIndex++;
            }
        }
        param.setSql(fastSQL.toString());
    }

    public static void insertList(FastDaoParam param) {
        if (StrUtil.isNotEmpty(param.getSql())) {
            return;
        }
        TableMapper tableMapper = param.getTableMapper();
        List<String> fieldNames = tableMapper.getFieldNames();
        Map<String, String> fieldTableNames = tableMapper.getFieldTableNames();

        FastSQL fastSQL = new FastSQL();
        fastSQL.INSERT_INTO(tableMapper.getTableName());
        List<String> columnList = new ArrayList<>();
        for (String fieldName : fieldNames) {
            columnList.add("`" + fieldTableNames.get(fieldName) + "`");
        }
        fastSQL.INTO_COLUMNS(ArrayUtil.toArray(columnList,String.class));
        StringBuilder sb = new StringBuilder(fastSQL.toString()+" VALUES " + StrUtil.CRLF);
        int paramIndex = 0;
        Map<String, Object> paramMap = param.getParamMap();
        for (Object pojo : param.getInsertList()) {
            sb.append("(");
            List<String> paramKeys = new ArrayList<>();
            for (String fieldName : fieldNames) {
                Object fieldValue = BeanUtil.getFieldValue(pojo, fieldName);
                String paramKey = "insert_param_" + paramIndex;
                paramKeys.add("#{" + paramKey + "}");
                paramMap.put(paramKey, fieldValue);
                paramIndex++;
            }
            ;
            sb.append(StrUtil.join(",",paramKeys)+"),"+StrUtil.CRLF);
//            fastSQL.INTO_VALUES(ArrayUtil.toArray(paramKeys, String.class));
        }
        String sql = sb.toString();
        param.setSql(sql.substring(0,sql.length()-3));
    }




}

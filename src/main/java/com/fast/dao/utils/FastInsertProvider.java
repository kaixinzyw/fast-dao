package com.fast.dao.utils;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.fast.mapper.TableMapper;
import com.fast.example.FastDaoParam;
import com.fast.utils.FastSQL;

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
            param.setSql(param.getSql().replaceAll("[#][{]", "#{paramMap."));
            return;
        }

        TableMapper tableMapper = param.getTableMapper();
        Object pojo = param.getPojo();

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
                fastSQL.VALUES("`" + fieldTableNames.get(fieldName) + "`", "#{paramMap." + paramKey + "}");
                paramMap.put(paramKey, fieldValue);
                paramIndex++;
            }
        }
        param.setSql(fastSQL.toString()+ ";");
    }
}

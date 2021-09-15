package com.fast.dao.utils;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.fast.dao.many.FastJoinQueryInfo;
import com.fast.fast.FastDaoParam;
import com.fast.mapper.TableMapper;

import java.util.*;

/**
 * @author 张亚伟
 */
public class JSONResultUtil {

    public static List<JSONObject> change(List<Map<String, JSONObject>> jdbcMapResult) {
        if (CollUtil.isEmpty(jdbcMapResult)) {
            return null;
        }
        TableMapper tableMapper = FastDaoParam.get().getTableMapper();
        Map<String, List<JSONObject>> dataMap = new HashMap<>();
        for (Map<String, JSONObject> objectMap : jdbcMapResult) {
            for (String tableName : objectMap.keySet()) {
                List<JSONObject> list = dataMap.get(tableName);
                if (list == null) {
                    list = new ArrayList<>();
                    dataMap.put(tableName, list);
                }
                list.add(objectMap.get(tableName));
            }
        }
        List<FastJoinQueryInfo> fastJoinQueryInfoList = tableMapper.getJoinQueryInfoList();
        dataMap.put(tableMapper.getTableAlias(), CollUtil.distinct(dataMap.get(tableMapper.getTableAlias())));
        for (FastJoinQueryInfo fastJoinQueryInfo : fastJoinQueryInfoList) {
            List<JSONObject> thisTableObjectList = dataMap.get(fastJoinQueryInfo.getThisTableAlias());
            if (CollUtil.isEmpty(thisTableObjectList)) {
                continue;
            }
            for (JSONObject joinTableObject : thisTableObjectList) {
                Object thisId = joinTableObject.get(fastJoinQueryInfo.getThisColumnName());
                List<JSONObject> joinDataList = dataMap.get(fastJoinQueryInfo.getJoinTableAlias());
                if (CollUtil.isEmpty(joinDataList)) {
                    continue;
                }
                Map<Object, JSONObject> joinMap = new LinkedHashMap<>();
                for (JSONObject joinData : joinDataList) {
                    if (ObjectUtil.equal(joinData.get(fastJoinQueryInfo.getJoinColumnName()), thisId)) {
                        joinMap.put(joinData.get(fastJoinQueryInfo.getJoinPrimaryKey()), joinData);
                        if (CollUtil.isNotEmpty(tableMapper.getTableAliasFieldMap().get(fastJoinQueryInfo.getJoinTableAlias())) &&
                                StrUtil.equals(fastJoinQueryInfo.getThisTableAlias(), tableMapper.getTableAlias())) {
                            for (String columnName : tableMapper.getTableAliasFieldMap().get(fastJoinQueryInfo.getJoinTableAlias()).keySet()) {
                                joinTableObject.put(columnName, joinData.get(columnName));
                            }
                        }
                    }
                }
                if (CollUtil.isNotEmpty(joinMap)) {
                    if (fastJoinQueryInfo.getCollectionType()) {
                        List<JSONObject> joinList = new ArrayList<>();
                        for (Object joinId : joinMap.keySet()) {
                            joinList.add(joinMap.get(joinId));
                        }
                        joinTableObject.put(fastJoinQueryInfo.getFieldName(), joinList);
                    } else {
                        for (Object joinId : joinMap.keySet()) {
                            if (ObjectUtil.equal(joinId, thisId)) {
                                joinTableObject.put(fastJoinQueryInfo.getFieldName(), joinMap.get(joinId));
                                break;
                            }
                        }
                    }

                }
            }
        }
        return dataMap.get(tableMapper.getTableAlias());
    }
}

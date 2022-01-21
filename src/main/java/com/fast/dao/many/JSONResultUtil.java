package com.fast.dao.many;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.fast.condition.ConditionPackages;
import com.fast.dao.many.FastJoinQueryInfo;
import com.fast.mapper.TableMapper;

import java.util.*;

/**
 * @author 张亚伟
 */
public class JSONResultUtil {

    public static List<JSONObject> change(ConditionPackages conditionPackages, List<FastJoinQueryInfo> fastJoinQueryInfoList, List<Map<String, JSONObject>> jdbcMapResult) {
        if (CollUtil.isEmpty(jdbcMapResult)) {
            return null;
        }
        TableMapper tableMapper = conditionPackages.getTableMapper();
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
                List<JSONObject> joinGroupList = new ArrayList<>();
                for (JSONObject joinData : joinDataList) {
                    if (ObjectUtil.equal(joinData.get(fastJoinQueryInfo.getJoinColumnName()), thisId)) {
                        joinGroupList.add(joinData);
                        if (CollUtil.isNotEmpty(tableMapper.getTableAliasFieldMap().get(fastJoinQueryInfo.getJoinTableAlias())) &&
                                StrUtil.equals(fastJoinQueryInfo.getThisTableAlias(), tableMapper.getTableAlias())) {
                            Map<String,String> tableAliasFieldMap = tableMapper.getTableAliasFieldMap().get(fastJoinQueryInfo.getJoinTableAlias());
                            for (String columnName : tableAliasFieldMap.keySet()) {
                                joinTableObject.put(tableAliasFieldMap.get(columnName), joinData.get(columnName));
                            }
                        }
                    }
                }
                joinGroupList = CollUtil.distinct(joinGroupList);
                if (CollUtil.isNotEmpty(joinGroupList)) {
                    if (fastJoinQueryInfo.getCollectionType()) {
                        joinTableObject.put(fastJoinQueryInfo.getFieldName(), joinGroupList);
                    } else {
                        for (JSONObject joinObj : joinGroupList) {
                            if (ObjectUtil.equal(joinObj.get(fastJoinQueryInfo.getJoinColumnName()), thisId)) {
                                joinTableObject.put(fastJoinQueryInfo.getFieldName(), joinObj);
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

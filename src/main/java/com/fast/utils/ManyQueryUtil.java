package com.fast.utils;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.fast.condition.FastExample;
import com.fast.condition.many.ManyToManyInfo;
import com.fast.fast.FastDaoParam;
import com.fast.mapper.TableMapper;

import java.util.*;

/**
 * 多表查询工具类
 *
 * @author zyw
 * @date 2021/02/19
 */
public class ManyQueryUtil {

    public static void manyToMany(List parseArray, FastDaoParam fastDaoParam) {
        if (!fastDaoParam.getFastExample().conditionPackages().getRelatedQuery()) {
            return;
        }
        TableMapper tableMapper = fastDaoParam.getTableMapper();
        List<ManyToManyInfo> manyToManyInfoList = tableMapper.getManyToManyInfoList();
        if (CollUtil.isNotEmpty(manyToManyInfoList)) {
            Set<Object> manyPrimaryKeyList = new HashSet<>();
            for (Object t : parseArray) {
                Object fieldValue = BeanUtil.getFieldValue(t, tableMapper.getPrimaryKeyField());
                if (fieldValue != null) {
                    manyPrimaryKeyList.add(fieldValue);
                }
            }
            for (ManyToManyInfo manyToManyInfo : manyToManyInfoList) {
//                FastExample toManyFastExample = new FastExample(manyToManyInfo.getRelationMapper().getObjClass());
//                toManyFastExample.andSql(StrUtil.format("exists(select 1 from {} where {} in ({}) and {} = {})",
//                        manyToManyInfo.getJoinMapper().getTableName(),
//                        manyToManyInfo.getJoinMapper().getTableName()+"."+manyToManyInfo.getJoinMapper().getFieldTableNames().get(manyToManyInfo.getJoinMapperFieldName()),
//                        CollUtil.join(manyPrimaryKeyList, StrUtil.COMMA),
//                        param.getTableMapper().getTableName()+"."+param.getTableMapper().getPrimaryKeyTableField(),
//                        manyToManyInfo.getJoinMapper().getTableName()+"."+manyToManyInfo.getJoinMapper().getFieldTableNames().get(manyToManyInfo.getRelationMapperFieldName())),
//                        null);
                FastExample middleFastExample = new FastExample(manyToManyInfo.getJoinMapper().getObjClass());
                middleFastExample.closeRelatedQuery();
                middleFastExample.field(manyToManyInfo.getJoinMapperFieldName()).in(manyPrimaryKeyList);
                List middleDataList = middleFastExample.dao().findAll();
                if (CollUtil.isNotEmpty(middleDataList)) {
                    Set<Object> toManyPrimaryKeyList = new HashSet<>();
                    for (Object middleData : middleDataList) {
                        Object fieldValue = BeanUtil.getFieldValue(middleData, manyToManyInfo.getRelationMapperFieldName());
                        if (fieldValue != null) {
                            toManyPrimaryKeyList.add(fieldValue);
                        }
                    }
                    if (CollUtil.isNotEmpty(toManyPrimaryKeyList)) {
                        FastExample toManyFastExample = new FastExample(manyToManyInfo.getRelationMapper().getObjClass());
                        toManyFastExample.closeRelatedQuery();
                        toManyFastExample.field(manyToManyInfo.getRelationMapper().getPrimaryKeyTableField()).in(toManyPrimaryKeyList);
                        List toManyDataList = toManyFastExample.dao().findAll();
                        if (CollUtil.isNotEmpty(toManyDataList)) {
                            Map<Object, List<Object>> middleDataPrimaryKeyGroup = new HashMap<>();
                            for (Object t : middleDataList) {
                                Object key = BeanUtil.getFieldValue(t, manyToManyInfo.getJoinMapperFieldName());
                                List vs = middleDataPrimaryKeyGroup.get(key);
                                if (CollUtil.isEmpty(vs)) {
                                    vs = new ArrayList<>();
                                    middleDataPrimaryKeyGroup.put(key, vs);
                                }
                                vs.add(BeanUtil.getFieldValue(t, manyToManyInfo.getRelationMapperFieldName()));
                            }
                            Map<Object, List<Object>> middleDataGroup = new HashMap<>();
                            for (Object primaryKey : middleDataPrimaryKeyGroup.keySet()) {
                                List<Object> toManyPrimaryKeyDataList = middleDataPrimaryKeyGroup.get(primaryKey);
                                List<Object> toManyDataGroupList = new ArrayList<>();
                                for (Object toManyPrimaryKey : toManyPrimaryKeyDataList) {
                                    for (Object toManyData : toManyDataList) {
                                        if (ObjectUtil.equal(BeanUtil.getFieldValue(toManyData, manyToManyInfo.getRelationMapper().getPrimaryKeyField()), toManyPrimaryKey)) {
                                            toManyDataGroupList.add(toManyData);
                                            break;
                                        }
                                    }
                                }
                                middleDataGroup.put(primaryKey, toManyDataGroupList);
                            }
                            for (Object t : parseArray) {
                                Object manyPrimaryKey = BeanUtil.getFieldValue(t, tableMapper.getPrimaryKeyField());
                                List<Object> ts = middleDataGroup.get(manyPrimaryKey);
                                if (CollUtil.isNotEmpty(ts)) {
                                    BeanUtil.setFieldValue(t,manyToManyInfo.getDataFieldName(),ts);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

}

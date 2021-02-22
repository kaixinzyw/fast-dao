package com.fast.mapper;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.fast.cache.DataCacheType;
import com.fast.cache.FastRedisCache;
import com.fast.cache.FastStatisCache;
import com.fast.condition.many.*;
import com.fast.config.FastDaoAttributes;
import com.fast.config.PrimaryKeyType;
import org.apache.commons.lang3.reflect.FieldUtils;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 对象映射创建工具
 *
 * @author 张亚伟 https://github.com/kaixinzyw
 */
public class TableMapperUtil {

    /**
     * 对象映射信息<类名,映射信息>
     */
    private static Map<String, TableMapper> tableMappers = new HashMap<>();

    /**
     * 获取类映射关系
     *
     * @param clazz 类信息
     * @param <T>   操作的类泛型
     * @return 结果
     */
    public static <T> TableMapper<T> getTableMappers(Class<T> clazz) {
        TableMapper tableMapper = tableMappers.get(clazz.getSimpleName());
        if (tableMapper == null) {
            return createRowMapper(clazz);
        }
        return tableMapper;
    }


    /**
     * 创建类映射关系
     *
     * @param clazz 类信息
     * @param <T>   操作的类泛型
     * @return 创建结果
     */
    private static synchronized <T> TableMapper<T> createRowMapper(Class<T> clazz) {
        if (tableMappers.get(clazz.getSimpleName()) != null) {
            return tableMappers.get(clazz.getSimpleName());
        }
        TableMapper<T> tableMapper = new TableMapper<>();
        tableMapper.setClassName(clazz.getSimpleName());
        tableMapper.setObjClass(clazz);
        boolean isTableAnnotation = clazz.isAnnotationPresent(Table.class);
        if (isTableAnnotation) {
            tableMapper.setTableName((clazz.getAnnotation(Table.class)).name());
        } else {
            tableMapper.setTableName(StrUtil.toUnderlineCase(tableMapper.getClassName()));
        }
        if (FastDaoAttributes.isOpenCache) {
            if (clazz.isAnnotationPresent(FastRedisCache.class)) {
                FastRedisCache redisCache = clazz.getAnnotation(FastRedisCache.class);
                tableMapper.setCacheType(DataCacheType.RedisCache);
                if (redisCache.value() != 0L) {
                    tableMapper.setCacheTime(redisCache.value());
                    tableMapper.setCacheTimeType(redisCache.cacheTimeType());
                } else if (redisCache.cacheTime() != 0L) {
                    tableMapper.setCacheTime(redisCache.cacheTime());
                    tableMapper.setCacheTimeType(redisCache.cacheTimeType());
                } else {
                    tableMapper.setCacheTime(FastDaoAttributes.defaultCacheTime);
                    tableMapper.setCacheTimeType(FastDaoAttributes.defaultCacheTimeType);
                }
            } else if (clazz.isAnnotationPresent(FastStatisCache.class)) {
                FastStatisCache statisCache = clazz.getAnnotation(FastStatisCache.class);
                tableMapper.setCacheType(DataCacheType.StatisCache);
                if (statisCache.value() != 0L) {
                    tableMapper.setCacheTime(statisCache.value());
                    tableMapper.setCacheTimeType(statisCache.cacheTimeType());
                } else if (statisCache.cacheTime() != 0L) {
                    tableMapper.setCacheTime(statisCache.cacheTime());
                    tableMapper.setCacheTimeType(statisCache.cacheTimeType());
                } else {
                    tableMapper.setCacheTime(FastDaoAttributes.defaultCacheTime);
                    tableMapper.setCacheTimeType(FastDaoAttributes.defaultCacheTimeType);
                }
            }
        }

        Field[] fields = FieldUtils.getAllFields(clazz);
        List<String> fieldNames = new ArrayList<>();
        HashMap<String, Class> fieldTypes = new HashMap<>();
        HashMap<String, String> fieldTableNames = new HashMap<>();
        HashMap<String, String> selectShowField = new HashMap<>();
        HashMap<String, String> tableFieldNames = new HashMap<>();
        StringBuilder selectAllShowField = new StringBuilder();
        //获取主键
        for (Field field : fields) {
            boolean isId = field.isAnnotationPresent(Id.class);
            if (isId || field.getName().equals("id")) {
                if (tableMapper.getPrimaryKeyTableField() == null) {
                    tableMapper.setPrimaryKeyField(field.getName());
                    tableMapper.setPrimaryKeyTableField(getTabFieldName(field));
                    tableMapper.setPrimaryKeyClass(field.getType());
                    if (field.getType() == String.class) {
                        tableMapper.setPrimaryKeyType(PrimaryKeyType.OBJECTID);
                    } else {
                        tableMapper.setPrimaryKeyType(PrimaryKeyType.AUTO);
                    }
                }
                break;
            }
        }
        List<ManyToManyInfo> manyToManyInfoList = new ArrayList<>();
        List<Field> manyToManyFieldList = new ArrayList<>();

        List<OneToManyInfo> oneToManyInfoList = new ArrayList<>();
        List<Field> oneToManyFieldList = new ArrayList<>();

        List<ManyToOneInfo> manyToOneInfoList = new ArrayList<>();
        List<Field> manyToOneFieldList = new ArrayList<>();
        for (Field field : fields) {
            if (field.getName().equals("serialVersionUID") || CollUtil.contains(FastDaoAttributes.ruleOutFieldList, field.getName())) {
                continue;
            }

            boolean isManyToMany = field.isAnnotationPresent(FastManyToMany.class);
            if (isManyToMany) {
                manyToManyFieldList.add(field);
                continue;
            }

            boolean isOneToMany = field.isAnnotationPresent(FastOneToMany.class);
            if (isOneToMany) {
                oneToManyFieldList.add(field);
                continue;
            }

            boolean isManyToOne = field.isAnnotationPresent(FastManyToOne.class);
            if (isManyToOne) {
                manyToOneFieldList.add(field);
                continue;
            }


            String tabFieldName = getTabFieldName(field);

            if (FastDaoAttributes.isOpenLogicDelete && field.getName().equals(FastDaoAttributes.deleteFieldName)) {
                tableMapper.setLogicDelete(Boolean.TRUE);
            }

            if (FastDaoAttributes.isAutoSetCreateTime && field.getName().equals(FastDaoAttributes.createTimeFieldName)) {
                tableMapper.setAutoSetCreateTime(Boolean.TRUE);
            }

            if (FastDaoAttributes.isAutoSetUpdateTime && field.getName().equals(FastDaoAttributes.updateTimeFieldName)) {
                tableMapper.setAutoSetUpdateTime(Boolean.TRUE);
            }

            fieldNames.add(field.getName());
            fieldTypes.put(field.getName(), field.getType());
            fieldTableNames.put(field.getName(), tabFieldName);
            tableFieldNames.put(tabFieldName, field.getName());
            String tableFieldName = "`" + tabFieldName + "`";
            selectShowField.put(field.getName(), tableFieldName);
            selectAllShowField.append(tableFieldName + ", ");
        }
        tableMapper.setManyToManyInfoList(manyToManyInfoList);
        tableMapper.setOneToManyInfoList(oneToManyInfoList);
        tableMapper.setManyToOneInfoList(manyToOneInfoList);
        tableMapper.setShowTableNames(selectShowField);
        tableMapper.setFieldNames(fieldNames);
        tableMapper.setFieldTypes(fieldTypes);
        tableMapper.setFieldTableNames(fieldTableNames);
        tableMapper.setTableFieldNames(tableFieldNames);
        if (StrUtil.isNotBlank(selectAllShowField) && selectAllShowField.length() > 2) {
            tableMapper.setShowAllTableNames(selectAllShowField.substring(0, selectAllShowField.length() - 2));
        }
        tableMappers.put(clazz.getSimpleName(), tableMapper);
        if (CollUtil.isNotEmpty(manyToManyFieldList)) {
            for (Field field : manyToManyFieldList) {
                FastManyToMany manyToMany = field.getAnnotation(FastManyToMany.class);
                ManyToManyInfo info = new ManyToManyInfo();
                info.setDataFieldName(field.getName());
                info.setJoinMapper(getTableMappers(manyToMany.joinEntity()));
                info.setJoinMapperFieldName(StrUtil.isNotBlank(manyToMany.joinMappedBy()) ? manyToMany.joinMappedBy() :
                        StrUtil.toCamelCase(tableMapper.getTableName() + StrUtil.UNDERLINE + tableMapper.getPrimaryKeyTableField()));
                info.setRelationMapper(getTableMappers(manyToMany.relationalEntity()));
                info.setRelationMapperFieldName(StrUtil.isNotBlank(manyToMany.relationalMappedBy()) ? manyToMany.relationalMappedBy() :
                        StrUtil.toCamelCase(info.getRelationMapper().getTableName() + StrUtil.UNDERLINE + info.getRelationMapper().getPrimaryKeyTableField()));
                manyToManyInfoList.add(info);
            }
        }

        if (CollUtil.isNotEmpty(oneToManyFieldList)) {
            for (Field field : oneToManyFieldList) {
                FastOneToMany oneToMany = field.getAnnotation(FastOneToMany.class);
                OneToManyInfo info = new OneToManyInfo();
                info.setDataFieldName(field.getName());
                info.setJoinMapper(getTableMappers(oneToMany.joinEntity()));
                info.setJoinMapperFieldName(StrUtil.isNotBlank(oneToMany.joinMappedBy()) ? oneToMany.joinMappedBy() :
                        StrUtil.toCamelCase(info.getJoinMapper().getTableName() + StrUtil.UNDERLINE + info.getJoinMapper().getPrimaryKeyTableField()));
                oneToManyInfoList.add(info);
            }
        }

        if (CollUtil.isNotEmpty(manyToOneFieldList)) {
            for (Field field : manyToOneFieldList) {
                FastManyToOne manyToOne = field.getAnnotation(FastManyToOne.class);
                ManyToOneInfo info = new ManyToOneInfo();
                info.setDataFieldName(field.getName());
                info.setJoinMapper(getTableMappers(manyToOne.joinEntity()));
                info.setJoinMapperFieldName(StrUtil.isNotBlank(manyToOne.joinMappedBy()) ? manyToOne.joinMappedBy() :
                        StrUtil.toCamelCase(info.getJoinMapper().getTableName() + StrUtil.UNDERLINE + info.getJoinMapper().getPrimaryKeyTableField()));
                manyToOneInfoList.add(info);
            }
        }

        return tableMapper;
    }

    private static String getTabFieldName(Field field) {
        String tabFieldName;
        boolean isColumn = field.isAnnotationPresent(Column.class);
        if (isColumn) {
            Column name = field.getAnnotation(Column.class);
            tabFieldName = name.name();
        } else {
            if (FastDaoAttributes.isToCamelCase) {
                tabFieldName = StrUtil.toUnderlineCase(field.getName());
            } else {
                tabFieldName = field.getName();
            }
        }
        return tabFieldName;
    }

}

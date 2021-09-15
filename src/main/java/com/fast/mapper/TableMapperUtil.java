package com.fast.mapper;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.text.StrBuilder;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.TypeUtil;
import com.fast.cache.DataCacheType;
import com.fast.cache.FastRedisCache;
import com.fast.cache.FastStatisCache;
import com.fast.config.FastDaoAttributes;
import com.fast.config.PrimaryKeyType;
import com.fast.dao.many.*;
import com.fast.fast.TableAlias;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.FieldUtils;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.lang.reflect.Field;
import java.util.*;

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
     * @param <T>   结果泛型
     * @return 结果
     */
    public static <T> TableMapper getTableMappers(Class<T> clazz) {
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
    private static synchronized <T> TableMapper createRowMapper(Class<T> clazz) {
        if (tableMappers.get(clazz.getSimpleName()) != null) {
            return tableMappers.get(clazz.getSimpleName());
        }
        TableMapper tableMapper = new TableMapper();
        tableMapper.setClassName(clazz.getSimpleName());
        tableMapper.setObjClass(clazz);

        if (clazz.isAnnotationPresent(Table.class)) {
            Table annotation = clazz.getAnnotation(Table.class);
            tableMapper.setTableName(annotation.name());
            tableMapper.setTableAlias(annotation.name());

        } else {
            String toUnderlineCase = StrUtil.toUnderlineCase(tableMapper.getClassName());
            tableMapper.setTableName(toUnderlineCase);
            tableMapper.setTableAlias(toUnderlineCase);
        }
        if (clazz.isAnnotationPresent(TableAlias.class)) {
            TableAlias annotation = clazz.getAnnotation(TableAlias.class);
            tableMapper.setTableAlias(annotation.value());
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
        LinkedHashMap<String, Class> fieldTypes = new LinkedHashMap<>();
        LinkedHashMap<String, String> fieldTableNames = new LinkedHashMap<>();
        LinkedHashMap<String, String> selectShowField = new LinkedHashMap<>();
        LinkedHashMap<String, String> tableFieldNames = new LinkedHashMap<>();
        StrBuilder selectAllShowField = new StrBuilder();
        StrBuilder selectPrefixAllShowField = new StrBuilder();
        //获取主键
        for (Field field : fields) {
            boolean isId = field.isAnnotationPresent(Id.class);
            if (isId || "id".equals(field.getName())) {
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

        List<FastJoinQueryInfo> fastJoinQueryInfoList = new ArrayList<>();

        for (Field field : fields) {
            if ("serialVersionUID".equals(field.getName()) || StrUtil.equals(field.getName(), "fastExample") ||
                    CollUtil.contains(FastDaoAttributes.ruleOutFieldList, field.getName()) || fieldTableNames.get(field.getName()) != null) {
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

            boolean isManyObject = field.isAnnotationPresent(FastJoinQuery.class);
            if (isManyObject) {
                FastJoinQueryInfo info = new FastJoinQueryInfo();
                FastJoinQuery annotation = field.getAnnotation(FastJoinQuery.class);
                info.setFieldName(field.getName());
                info.setCollectionType(Collection.class.isAssignableFrom(field.getType()));
                if (StrUtil.isNotBlank(annotation.thisTableAlias())) {
                    info.setThisTableAlias(annotation.thisTableAlias());
                } else {
                    info.setThisTableAlias(tableMapper.getTableAlias());
                }
                Class mapperClass;
                if (info.getCollectionType()) {
                    mapperClass = TypeUtil.getClass(TypeUtil.getTypeArgument(
                            TypeUtil.getReturnType(ReflectUtil.getMethod(tableMapper.getObjClass(), StrBuilder.create("get", StringUtils.capitalize(field.getName())).toString()))));
                } else {
                    mapperClass = field.getType();
                }
                TableMapper joinMappers = getTableMappers(mapperClass);
                info.setJoinPrimaryKey(joinMappers.getPrimaryKeyTableField());
                if (StrUtil.isNotBlank(annotation.joinTableAlias())) {
                    info.setJoinTableAlias(annotation.joinTableAlias());
                } else {
                    info.setJoinTableAlias(joinMappers.getTableAlias());
                }
                if (StrUtil.isNotBlank(annotation.joinColumnName())) {
                    info.setJoinColumnName(annotation.joinColumnName());
                } else {
                    if (info.getCollectionType()) {
                        info.setJoinColumnName(info.getThisTableAlias() + StrUtil.UNDERLINE + tableMapper.getPrimaryKeyTableField());
                    } else {
                        info.setJoinColumnName(joinMappers.getPrimaryKeyTableField());
                    }
                }
                if (StrUtil.isNotBlank(annotation.thisColumnName())) {
                    info.setThisColumnName(annotation.thisColumnName());
                } else {
                    if (info.getCollectionType()) {
                        info.setThisColumnName(tableMapper.getPrimaryKeyTableField());
                    } else {
                        info.setThisColumnName(info.getJoinTableAlias() + StrUtil.UNDERLINE + joinMappers.getPrimaryKeyTableField());
                    }
                }
                tableMapper.addFastJoinQueryInfoMap(mapperClass, info);
                fastJoinQueryInfoList.add(info);
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
            if (field.isAnnotationPresent(TableAlias.class)) {
                TableAlias tableAlias = field.getAnnotation(TableAlias.class);
                selectAllShowField.append(tableFieldName).append(StrUtil.COMMA);
                selectPrefixAllShowField.append(StrUtil.strBuilder(tableAlias.value(), StrUtil.DOT, tableFieldName, ","));
                tableMapper.addTableAliasFieldMap(tableAlias.value(), tabFieldName, field.getName());
            } else {
                selectAllShowField.append(tableFieldName).append(",");
                selectPrefixAllShowField.append(StrUtil.strBuilder(tableMapper.getTableAlias(), StrUtil.DOT, tableFieldName, ","));
            }
        }
        tableMapper.setJoinQueryInfoList(fastJoinQueryInfoList);
        tableMapper.setManyToManyInfoList(manyToManyInfoList);
        tableMapper.setOneToManyInfoList(oneToManyInfoList);
        tableMapper.setManyToOneInfoList(manyToOneInfoList);
        tableMapper.setShowTableNames(selectShowField);
        tableMapper.setFieldNames(fieldNames);
        tableMapper.setFieldTypes(fieldTypes);
        tableMapper.setFieldTableNames(fieldTableNames);
        tableMapper.setTableFieldNames(tableFieldNames);
        if (StrUtil.isNotBlank(selectAllShowField) && selectAllShowField.length() > 1) {
            tableMapper.setShowAllTableNames(selectAllShowField.subString(0, selectAllShowField.length() - 1));
            tableMapper.setShowPrefixAllTableNames(selectPrefixAllShowField.subString(0, selectPrefixAllShowField.length() - 1));
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
                        StrUtil.toCamelCase(tableMapper.getTableName() + StrUtil.UNDERLINE + tableMapper.getPrimaryKeyTableField()));
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

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
     * @return 结果
     */
    public static TableMapper getTableMappers(Class clazz) {
        TableMapper tableMapper = tableMappers.get(clazz.getSimpleName());
        if (tableMapper == null) {
            tableMapper = createRowMapper(clazz);
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
        tableMappers.put(clazz.getSimpleName(), tableMapper);

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
                tableMapper.setOpenCache(Boolean.TRUE);
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
                tableMapper.setOpenCache(Boolean.TRUE);
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
        for (Field field : fields) {
            if ("serialVersionUID".equals(field.getName()) || StrUtil.equals(field.getName(), "fastExample") ||
                    CollUtil.contains(FastDaoAttributes.ruleOutFieldList, field.getName()) || fieldTableNames.get(field.getName()) != null) {
                continue;
            }
            boolean isManyObject = field.isAnnotationPresent(FastJoinQuery.class);
            if (isManyObject) {
                FastJoinQuery annotation = field.getAnnotation(FastJoinQuery.class);
                FastJoinQueryInfo info = new FastJoinQueryInfo();
                info.setFieldName(field.getName());
                info.setCollectionType(Collection.class.isAssignableFrom(field.getType()));

                TableMapper joinMappers;
                String joinTableAlias;
                if (info.getCollectionType()) {
                    joinMappers = getTableMappers(TypeUtil.getClass(TypeUtil.getTypeArgument(
                            TypeUtil.getReturnType(ReflectUtil.getMethod(tableMapper.getObjClass(), StrBuilder.create("get", StringUtils.capitalize(field.getName())).toString())))));
                } else {
                    joinMappers = getTableMappers(field.getType());
                }
                if (StrUtil.isNotBlank(annotation.thisTableAlias())) {
                    info.setThisTableAlias(annotation.thisTableAlias());
                }
                if (StrUtil.isNotBlank(annotation.thisColumnName())) {
                    info.setThisColumnName(annotation.thisColumnName());
                }
                if (StrUtil.isNotBlank(annotation.joinTableAlias())) {
                    info.setJoinTableAlias(annotation.joinTableAlias());
                    joinTableAlias = annotation.joinTableAlias();
                } else {
                    if (StrUtil.isNotBlank(annotation.value())) {
                        info.setJoinTableAlias(annotation.value());
                        joinTableAlias = annotation.value();
                    } else {
                        joinTableAlias = joinMappers.getTableAlias();
                    }
                }
                if (StrUtil.isNotBlank(annotation.joinColumnName())) {
                    info.setJoinColumnName(annotation.joinColumnName());
                }
                if (field.isAnnotationPresent(TableAlias.class)) {
                    info.setJoinTableAlias(field.getAnnotation(TableAlias.class).value());
                }
                tableMapper.addFastJoinQueryInfoMap(joinTableAlias, info);
                tableMapper.putFastJoinQueryInfoMap(joinMappers.getFastJoinQueryInfoMap());
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

            if (!field.isAnnotationPresent(NotQuery.class)) {
                String tableFieldName;
                if (field.isAnnotationPresent(ColumnAlias.class)) {
                    ColumnAlias columnAlias = field.getAnnotation(ColumnAlias.class);
                    tableFieldName = "`" + columnAlias.value() + "`" + " AS " + field.getName();
                    tableMapper.addColumnAliasMap(tabFieldName, columnAlias.value());
                } else {
                    tableFieldName = "`" + tabFieldName + "`";
                }
                selectShowField.put(field.getName(), tableFieldName);
                if (field.isAnnotationPresent(TableAlias.class)) {
                    TableAlias tableAlias = field.getAnnotation(TableAlias.class);
                    selectAllShowField.append(tableFieldName).append(StrUtil.COMMA);
                    selectPrefixAllShowField.append(StrUtil.strBuilder(tableAlias.value(), StrUtil.DOT, tableFieldName, ","));
                    tableMapper.addTableAliasFieldMap(tableAlias.value(), tableMapper.getColumnAliasMap().get(tabFieldName) != null ? tableMapper.getColumnAliasMap().get(tabFieldName) : tabFieldName, field.getName());
                } else {
                    selectAllShowField.append(tableFieldName).append(",");
                    selectPrefixAllShowField.append(StrUtil.strBuilder(tableMapper.getTableAlias(), StrUtil.DOT, tableFieldName, ","));
                }
            }
        }
        tableMapper.setShowTableNames(selectShowField);
        tableMapper.setFieldNames(fieldNames);
        tableMapper.setFieldTypes(fieldTypes);
        tableMapper.setFieldTableNames(fieldTableNames);
        tableMapper.setTableFieldNames(tableFieldNames);
        if (StrUtil.isNotBlank(selectAllShowField) && selectAllShowField.length() > 1) {
            tableMapper.setShowAllTableNames(selectAllShowField.subString(0, selectAllShowField.length() - 1));
            tableMapper.setShowPrefixAllTableNames(selectPrefixAllShowField.subString(0, selectPrefixAllShowField.length() - 1));
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

package com.fast.db.template.mapper;

import cn.hutool.core.util.StrUtil;
import com.fast.db.template.cache.DataCacheType;
import com.fast.db.template.cache.FastRedisCache;
import com.fast.db.template.cache.FastRedisLocalCache;
import com.fast.db.template.cache.FastStatisCache;
import com.fast.db.template.config.FastParams;
import com.fast.db.template.config.PrimaryKeyType;

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
 * @author 张亚伟 https://github.com/kaixinzyw/fast-db-template
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
        if (FastParams.isOpenCache) {
            if (clazz.isAnnotationPresent(FastRedisLocalCache.class)) {
                FastRedisLocalCache fastLocalCache = clazz.getAnnotation(FastRedisLocalCache.class);
                tableMapper.setCacheType(DataCacheType.RedisLocalCache);
                if (fastLocalCache.value() != 0L) {
                    tableMapper.setCacheTime(fastLocalCache.value());
                    tableMapper.setCacheTimeType(fastLocalCache.cacheTimeType());
                } else if (fastLocalCache.cacheTime() != 0L) {
                    tableMapper.setCacheTime(fastLocalCache.cacheTime());
                    tableMapper.setCacheTimeType(fastLocalCache.cacheTimeType());
                } else {
                    tableMapper.setCacheTime(FastParams.defaultCacheTime);
                    tableMapper.setCacheTimeType(FastParams.defaultCacheTimeType);
                }
            } else if (clazz.isAnnotationPresent(FastRedisCache.class)) {
                FastRedisCache redisCache = clazz.getAnnotation(FastRedisCache.class);
                tableMapper.setCacheType(DataCacheType.RedisCache);
                if (redisCache.value() != 0L) {
                    tableMapper.setCacheTime(redisCache.value());
                    tableMapper.setCacheTimeType(redisCache.cacheTimeType());
                } else if (redisCache.cacheTime() != 0L) {
                    tableMapper.setCacheTime(redisCache.cacheTime());
                    tableMapper.setCacheTimeType(redisCache.cacheTimeType());
                } else {
                    tableMapper.setCacheTime(FastParams.defaultCacheTime);
                    tableMapper.setCacheTimeType(FastParams.defaultCacheTimeType);
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
                    tableMapper.setCacheTime(FastParams.defaultCacheTime);
                    tableMapper.setCacheTimeType(FastParams.defaultCacheTimeType);
                }
            }
        }

        Field[] fields = clazz.getDeclaredFields();
        List<String> fieldNames = new ArrayList<>();
        Map<String, Class> fieldTypes = new HashMap<>();
        Map<String, String> fieldTableNames = new HashMap<>();
        Map<String, String> selectShowField = new HashMap<>();
        Map<String, String> tableFieldNames = new HashMap<>();
        StringBuilder selectAllShowField = new StringBuilder();
        for (Field field : fields) {
            if (field.getName().equals("serialVersionUID")) {
                continue;
            }
            String tabFieldName;
            boolean isId = field.isAnnotationPresent(Id.class);
            boolean isColumn = field.isAnnotationPresent(Column.class);
            if (isColumn) {
                Column name = field.getAnnotation(Column.class);
                tabFieldName = name.name();
            } else {
                if (FastParams.isToCamelCase) {
                    tabFieldName = StrUtil.toUnderlineCase(field.getName());
                } else {
                    tabFieldName = field.getName();
                }
            }
            if (isId) {
                tableMapper.setPrimaryKeyField(field.getName());
                tableMapper.setPrimaryKeyTableField(tabFieldName);
                tableMapper.setPrimaryKeyClass(field.getType());
                if (field.getType() == String.class) {
                    tableMapper.setPrimaryKeyType(PrimaryKeyType.UUID);
                }else {
                    tableMapper.setPrimaryKeyType(PrimaryKeyType.AUTO);
                }
            }
            fieldNames.add(field.getName());
            fieldTypes.put(field.getName(), field.getType());
            fieldTableNames.put(field.getName(), tabFieldName);
            tableFieldNames.put(tabFieldName, field.getName());
            String tableFieldName = "`" + tabFieldName + "`";
            selectShowField.put(field.getName(), tableFieldName);
            selectAllShowField.append(tableFieldName + " as " + field.getName() + ", ");
        }
        tableMapper.setShowTableNames(selectShowField);
        tableMapper.setFieldNames(fieldNames);
        tableMapper.setFieldTypes(fieldTypes);
        tableMapper.setFieldTableNames(fieldTableNames);
        tableMapper.setTableFieldNames(tableFieldNames);
        tableMapper.setShowAllTableNames(selectAllShowField.substring(0, selectAllShowField.length() - 2));
        tableMappers.put(clazz.getSimpleName(), tableMapper);
        return tableMapper;
    }

}

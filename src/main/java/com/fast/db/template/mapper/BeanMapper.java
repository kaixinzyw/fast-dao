package com.fast.db.template.mapper;

import cn.hutool.core.util.StrUtil;
import com.fast.db.template.config.AutomaticParameterAttributes;
import com.fast.db.template.template.DBTemplate;
import com.fast.db.template.cache.FastCacheType;
import com.fast.db.template.cache.FastLocalCache;
import com.fast.db.template.implement.FastDBActuator;
import com.fast.db.template.template.DBMapper;
import io.netty.util.concurrent.FastThreadLocal;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 表和对象关系映射生成工具 被动
 * @author 张亚伟 398850094@qq.com
 */
public class BeanMapper {

    private static Map<String, FastThreadLocal<DBMapper>> dbMapperThread = new HashMap<>();

    public static <Pojo> DBMapper<Pojo> getDBMapper(Class<Pojo> clazz) {
        if (dbMapperThread.get(clazz.getSimpleName()) == null) {
            return createRowMapper(clazz);
        }
        return dbMapperThread.get(clazz.getSimpleName()).get();
    }

    public static <Pojo> FastClassTableMapper getFastClassTableMapper(Class<Pojo> clazz) {
        return getDBMapper(clazz).getFastClassTableMapper();
    }

    public static <Pojo> DBTemplate<Pojo> getDBTemplate(Class<Pojo> clazz) {
        return getDBMapper(clazz).getDbTemplate();
    }

    private static synchronized <Pojo> DBMapper<Pojo> createRowMapper(Class<Pojo> clazz) {
        if (dbMapperThread.get(clazz.getSimpleName()) != null) {
            return dbMapperThread.get(clazz.getSimpleName()).get();
        }

        FastClassTableMapper fastClassTableMapper = new FastClassTableMapper();
        fastClassTableMapper.setClassName(clazz.getSimpleName());
        fastClassTableMapper.setObjClass(clazz);

        boolean isTableAnnotation = clazz.isAnnotationPresent(Table.class);
        if (isTableAnnotation) {
            fastClassTableMapper.setTableName((clazz.getAnnotation(Table.class)).name());
        } else {
            fastClassTableMapper.setTableName(StrUtil.toUnderlineCase(fastClassTableMapper.getClassName()));
        }

        if (AutomaticParameterAttributes.isOpenCache) {
            boolean annotationPresent = clazz.isAnnotationPresent(FastLocalCache.class);
            if (annotationPresent) {
                FastLocalCache fastLocalCache = clazz.getAnnotation(FastLocalCache.class);
                fastClassTableMapper.setCacheType(FastCacheType.LocalCache);
                if (fastLocalCache.value() != 0L) {
                    fastClassTableMapper.setCacheTime(fastLocalCache.value());
                    fastClassTableMapper.setCacheTimeType(fastLocalCache.cacheTimeType());
                } else if (fastLocalCache.cacheTime() != 0L) {
                    fastClassTableMapper.setCacheTime(fastLocalCache.cacheTime());
                    fastClassTableMapper.setCacheTimeType(fastLocalCache.cacheTimeType());
                } else {
                    fastClassTableMapper.setCacheTime(AutomaticParameterAttributes.defaultCacheTime);
                    fastClassTableMapper.setCacheTimeType(AutomaticParameterAttributes.defaultCacheTimeType);
                }
            }
        }


        Field[] fields = clazz.getDeclaredFields();

        List<String> fieldNames = new ArrayList<>();
        Map<String, Class> fieldTypes = new HashMap<>();
        Map<String, String> fieldTableNames = new HashMap<>();
        StringBuilder selectShowField = new StringBuilder();
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
                if (AutomaticParameterAttributes.isToCamelCase) {
                    tabFieldName = StrUtil.toUnderlineCase(field.getName());

                } else {
                    tabFieldName = field.getName();
                }
            }
            if (isId) {
                fastClassTableMapper.setPrimaryKeyField(field.getName());
                fastClassTableMapper.setPrimaryKeyTableField(tabFieldName);
            }
            fieldNames.add(field.getName());
            fieldTypes.put(field.getName(), field.getType());
            fieldTableNames.put(field.getName(), tabFieldName);
            selectShowField.append(fastClassTableMapper.getTableName() + ".`" + tabFieldName + "`,");
        }
        fastClassTableMapper.setFieldNames(fieldNames);
        fastClassTableMapper.setFieldTypes(fieldTypes);
        fastClassTableMapper.setFieldTableNames(fieldTableNames);
        fastClassTableMapper.setShowAllTableNames(selectShowField.substring(0, selectShowField.length() - 1));


        FastThreadLocal<DBMapper> threadLocal = new FastThreadLocal<>();
        try {
            FastDBActuator fastDBActuator = AutomaticParameterAttributes.getDBActuator().newInstance();
            fastDBActuator.setFastClassTableMapper(fastClassTableMapper);
            DBMapper dbMapper = new DBMapper(fastClassTableMapper, fastDBActuator);
            threadLocal.set(dbMapper);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
        dbMapperThread.put(fastClassTableMapper.getClassName(), threadLocal);
        return threadLocal.get();
    }
}

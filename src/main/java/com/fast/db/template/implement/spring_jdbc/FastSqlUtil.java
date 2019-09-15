package com.fast.db.template.implement.spring_jdbc;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import com.fast.db.template.config.AutomaticParameterAttributes;
import com.fast.db.template.mapper.FastClassTableMapper;
import com.fast.db.template.template.CompoundQuery;
import com.fast.db.template.template.MapperQueryObject;
import com.fast.db.template.utils.FastSQL;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

/**
 * SQL拼接工具类
 * @author 张亚伟 398850094@qq.com
 */
public class FastSqlUtil {

    private FastClassTableMapper fastClassTableMapper;


    public FastSqlUtil(FastClassTableMapper fastClassTableMapper) {
        this.fastClassTableMapper = fastClassTableMapper;
    }


    public String selectShowField(CompoundQuery select) {
        List<String> fieldNames = fastClassTableMapper.getFieldNames();
        Map<String, String> fieldTableNames = fastClassTableMapper.getFieldTableNames();
        StringBuilder selectShowField = new StringBuilder();

        if (select == null) {

            return fastClassTableMapper.getShowAllTableNames();
        }
        if (CollUtil.isNotEmpty(select.getDistinctFields())) {
            String distinctQuery = "DISTINCT" + " ";
            for (String distinctField : select.getDistinctFields()) {
                distinctQuery += (fieldPackage(fieldTableNames.get(distinctField)) + ",");
            }
            return distinctQuery.substring(0, distinctQuery.length() - 1);
        }
        if (CollUtil.isNotEmpty(select.getShowFields())) {
            for (String showField : select.getShowFields()) {
                selectShowField.append(fieldPackage(fieldTableNames.get(showField)) + ",");
            }
        } else if (CollUtil.isNotEmpty(select.getHideFields())) {
            for (String fieldName : fieldNames) {
                if (!select.getHideFields().contains(fieldName)) {
                    selectShowField.append(fieldPackage(fieldTableNames.get(fieldName)) + ",");
                }
            }
        }
        if (selectShowField.length() == 0) {
            return fastClassTableMapper.getShowAllTableNames();
        } else {
            return selectShowField.substring(0, selectShowField.length() - 1);
        }
    }

    private Integer paramIndex = 0;

    private String getPlaceholder(String placeholder) {
        if (placeholder.equals("?")) {
            return "? ";
        }
        String s = "#{paramList[" + paramIndex + "]} ";
        paramIndex += 1;
        return s;
    }

    public String whereSql(CompoundQuery select, List<Object> params, String placeholder) {

        String tableName = fastClassTableMapper.getTableName();
        Map<String, String> fieldTableNames = fastClassTableMapper.getFieldTableNames();
        if (select == null) {
            return " 1 = 1 ";
        }

        StringBuilder sql = new StringBuilder();
        paramIndex = 0;
        if (select.getEqualObject() != null) {
            Map<String, Object> pojoFieldTable = BeanUtil.beanToMap(select.getEqualObject(), false, true);
            for (String fieldName : pojoFieldTable.keySet()) {
                sql.append("AND " + fieldPackage(fieldTableNames.get(fieldName)) + " = " + getPlaceholder(placeholder));
                params.add(valueTypeCheck(pojoFieldTable.get(fieldName)));
            }
        }


        if (select.getRangeQuery() != null) {
            for (MapperQueryObject.RangeQuery rangeQuery : select.getRangeQuery()) {
                sql.append("AND " + fieldPackage(fieldTableNames.get(rangeQuery.getRangeName())) + " BETWEEN " + getPlaceholder(placeholder) + " AND " + getPlaceholder(placeholder));
                params.add(valueTypeCheck(rangeQuery.getRangeMin()));
                params.add(valueTypeCheck(rangeQuery.getRangeMax()));
            }
        }

        if (select.getEqualFieldQuery() != null) {
            for (MapperQueryObject.FieldQuery fieldQuery : select.getEqualFieldQuery()) {
                sql.append("AND " + fieldPackage(fieldTableNames.get(fieldQuery.getFieldName())) + " = " + getPlaceholder(placeholder));
                params.add(valueTypeCheck(fieldQuery.getValue()));
            }
        }

        if (select.getGreaterFieldsQuery() != null) {
            for (MapperQueryObject.FieldQuery fieldQuery : select.getGreaterFieldsQuery()) {
                sql.append("AND " + fieldPackage(fieldTableNames.get(fieldQuery.getFieldName())) + " > " + getPlaceholder(placeholder));
                params.add(valueTypeCheck(fieldQuery.getValue()));
            }
        }

        if (select.getLessFieldsQuery() != null) {
            for (MapperQueryObject.FieldQuery fieldQuery : select.getLessFieldsQuery()) {
                sql.append("AND " + fieldPackage(fieldTableNames.get(fieldQuery.getFieldName())) + " < " + getPlaceholder(placeholder));
                params.add(valueTypeCheck(fieldQuery.getValue()));
            }
        }


        if (select.getGreaterOrEqualFieldsQuery() != null) {
            for (MapperQueryObject.FieldQuery fieldQuery : select.getGreaterOrEqualFieldsQuery()) {
                sql.append("AND " + fieldPackage(fieldTableNames.get(fieldQuery.getFieldName())) + " >= " + getPlaceholder(placeholder));
                params.add(valueTypeCheck(fieldQuery.getValue()));
            }
        }

        if (select.getLessOrEqualFieldsQuery() != null) {
            for (MapperQueryObject.FieldQuery fieldQuery : select.getLessOrEqualFieldsQuery()) {
                sql.append("AND " + fieldPackage(fieldTableNames.get(fieldQuery.getFieldName())) + " <= " + getPlaceholder(placeholder));
                params.add(valueTypeCheck(fieldQuery.getValue()));
            }
        }
        if (select.getLikeQuery() != null) {
            for (MapperQueryObject.FieldQuery fieldQuery : select.getLikeQuery()) {
                sql.append("AND " + fieldPackage(fieldTableNames.get(fieldQuery.getFieldName())) + " LIKE " + getPlaceholder(placeholder));
                params.add(valueTypeCheck(fieldQuery.getValue()));
            }
        }

        if (select.getInQuery() != null) {
            for (MapperQueryObject.InQuery inQuery : select.getInQuery()) {

                String vals = "";
                for (Object inValue : inQuery.getInValues()) {
                    vals = vals + getPlaceholder(placeholder) + ",";
                    params.add(inValue);
                }
                sql.append("AND " + fieldPackage(fieldTableNames.get(inQuery.getInName())) + " IN (" +
                        vals.substring(0, vals.length() - 1) +
                        ") ");
            }
        }


        if (select.getNotNullFieldsQuery() != null) {
            for (String notNullStr : select.getNotNullFieldsQuery()) {
                sql.append("AND " + fieldPackage(fieldTableNames.get(notNullStr)) + " IS NOT NULL" + " ");
            }
        }

        if (select.getNullFieldsQuery() != null) {
            for (String nullStr : select.getNullFieldsQuery()) {
                sql.append("AND " + fieldPackage(fieldTableNames.get(nullStr)) + " IS NULL" + " ");
            }
        }

        if (select.getOr() != null) {
            whereOrSql(sql, tableName, fieldTableNames, select.getOr(), params, placeholder);
        }

        if (sql.length() > 3) {
            String temp = sql.toString();
            return temp.substring(3);
        } else {
            return " 1 = 1 ";
        }
    }


    public String valueTypeCheck(Object value) {
        if (value == null) {
            return "null";
        }
        if ("Date".equals(value.getClass().getSimpleName())) {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            return "\"" + formatter.format(value) + "\"";
        }
        if ("Boolean".equals(value.getClass().getSimpleName())) {
            if ((Boolean) value) {
                return "1";
            } else {
                return "0";
            }
        }
        return value.toString();
    }

    public StringBuilder whereOrSql(StringBuilder sql, String tableName, Map<String, String> fieldTableNames, CompoundQuery select, List<Object> params, String placeholder) {

        if (select.getRangeQuery() != null) {
            for (MapperQueryObject.RangeQuery rangeQuery : select.getRangeQuery()) {
                sql.append("OR " + fieldPackage(fieldTableNames.get(rangeQuery.getRangeName())) + " BETWEEN " + getPlaceholder(placeholder) + " AND " + getPlaceholder(placeholder));
                params.add(valueTypeCheck(rangeQuery.getRangeMin()));
                params.add(valueTypeCheck(rangeQuery.getRangeMax()));
            }
        }

        if (select.getEqualFieldQuery() != null) {
            for (MapperQueryObject.FieldQuery fieldQuery : select.getEqualFieldQuery()) {
                sql.append("OR " + fieldPackage(fieldTableNames.get(fieldQuery.getFieldName())) + " = " + getPlaceholder(placeholder));
                params.add(valueTypeCheck(fieldQuery.getValue()));
            }
        }


        if (select.getGreaterOrEqualFieldsQuery() != null) {
            for (MapperQueryObject.FieldQuery fieldQuery : select.getGreaterOrEqualFieldsQuery()) {
                sql.append("OR " + fieldPackage(fieldTableNames.get(fieldQuery.getFieldName())) + " >= " + getPlaceholder(placeholder));
                params.add(valueTypeCheck(fieldQuery.getValue()));
            }
        }

        if (select.getLessOrEqualFieldsQuery() != null) {
            for (MapperQueryObject.FieldQuery fieldQuery : select.getLessOrEqualFieldsQuery()) {
                sql.append("OR " + fieldPackage(fieldTableNames.get(fieldQuery.getFieldName())) + " <= " + getPlaceholder(placeholder));
                params.add(valueTypeCheck(fieldQuery.getValue()));
            }
        }
        if (select.getLikeQuery() != null) {
            for (MapperQueryObject.FieldQuery fieldQuery : select.getLikeQuery()) {
                sql.append("OR " + fieldPackage(fieldTableNames.get(fieldQuery.getFieldName())) + " LIKE " + getPlaceholder(placeholder));
                params.add(valueTypeCheck(fieldQuery.getValue()));
            }
        }

        if (select.getInQuery() != null) {
            for (MapperQueryObject.InQuery inQuery : select.getInQuery()) {


                String vals = "";
                for (Object inValue : inQuery.getInValues()) {
                    vals = vals + getPlaceholder(placeholder) + ",";
                    params.add(inValue);
                }
                sql.append("OR " + fieldPackage(fieldTableNames.get(inQuery.getInName())) + " IN (" +
                        vals.substring(0, vals.length() - 1) +
                        ") ");
            }
        }


        if (select.getNotNullFieldsQuery() != null) {
            for (String notNullStr : select.getNotNullFieldsQuery()) {
                sql.append("OR " + fieldPackage(fieldTableNames.get(notNullStr)) + " IS NOT NULL" + " ");
            }
        }

        if (select.getNullFieldsQuery() != null) {
            for (String nullStr : select.getNullFieldsQuery()) {
                sql.append("OR " + fieldPackage(fieldTableNames.get(nullStr)) + " IS NULL" + " ");
            }

        }
        return sql;
    }

    public String fieldPackage(String field) {
        return fastClassTableMapper.getTableName() + ".`" + field + "`";
    }

    public void updateSql(FastSQL fastSQL, Object obj, List<Object> params, Boolean isSelective, String placeholder) {

        List<String> fieldNames = fastClassTableMapper.getFieldNames();
        Map<String, String> fieldTableNames = fastClassTableMapper.getFieldTableNames();

        for (String fieldName : fieldNames) {

            if (AutomaticParameterAttributes.isAutoSetCreateTime && fieldName.equals(AutomaticParameterAttributes.createTimeField)) {
                continue;
            }

            Object fieldValue = BeanUtil.getFieldValue(obj, fieldName);
            if (fieldValue == null) {
                if (isSelective) {
                    continue;
                }
                if (AutomaticParameterAttributes.isOpenLogicDelete && fieldName.equals(AutomaticParameterAttributes.deleteField)) {
                    continue;
                }
            }

            if (placeholder.equals("?")) {
                params.add(fieldValue);
                fastSQL.SET(fieldTableNames.get(fieldName) + " = ? ");
            } else {
                fastSQL.SET(fieldTableNames.get(fieldName) + " = " + "#{pojo." + fieldName + "}");
            }

        }
    }


    public void orderBy(CompoundQuery compoundQuery, FastSQL fastSQL) {

        if (compoundQuery != null && compoundQuery.getOrderByQuery() != null) {
            compoundQuery.getOrderByQuery().forEach(orderByQuery -> {
                if (orderByQuery.getDesc()) {
                    fastSQL.ORDER_BY(fieldPackage(fastClassTableMapper.getFieldTableNames().get(orderByQuery.getOrderByName())) + " desc ");
                } else {
                    fastSQL.ORDER_BY(fieldPackage(fastClassTableMapper.getFieldTableNames().get(orderByQuery.getOrderByName())) + " asc ");
                }
            });
        }
    }


    public void noDeleteWhere(FastSQL fastSQL) {
        if (AutomaticParameterAttributes.isOpenLogicDelete) {
            fastSQL.AND().WHERE(AutomaticParameterAttributes.defaultDeleteValue + " = " + !AutomaticParameterAttributes.defaultDeleteValue);
        }
    }

    public void deleteWhere(FastSQL fastSQL) {
        if (AutomaticParameterAttributes.isOpenLogicDelete) {
            fastSQL.AND().WHERE(fieldPackage(AutomaticParameterAttributes.deleteTableField) + " = " + AutomaticParameterAttributes.defaultDeleteValue);
        }
    }
}

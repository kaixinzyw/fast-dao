package com.fast.generator;


import cn.hutool.core.util.StrUtil;
import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.fastjson.JSONObject;
import com.fast.config.FastDaoAttributes;
import com.fast.demo.pojo.Product;
import com.fast.fast.FastCustomSqlDao;
import org.apache.commons.lang3.RegExUtils;
import org.apache.commons.lang3.StringUtils;

import javax.sql.DataSource;
import java.util.*;

/**
 * 表解析 工具类
 */
public class GenUtils {

    public static GenTable getGenTable(String tableName) {
        GenTable table = initTable(tableName);
        return table;
    }


    public static void main(String[] args) {
        FastDaoAttributes.setDataSource(getDataSource());
        String sql = "select product.id,brand.create_time from product left join product_brand on product_brand.product_id = product.id left join brand on brand.id = product_brand.brand_id where product.id = 1";
        List<Product> productList = FastCustomSqlDao.create(Product.class, sql, null).findAll();
        System.out.println(JSONObject.toJSONString(productList));
    }

    private static DataSource getDataSource() {
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setUrl("jdbc:mysql://kaifa.mysql.guo-kai.com:3306/gk-ims?useUnicode=true&characterEncoding=utf-8&useSSL=false&serverTimezone=Asia/Shanghai");
        dataSource.setUsername("gkims-kaifa");
        dataSource.setPassword("PGrsByizeD357ajR");
        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        return dataSource;
    }


    public static GenTable initTable(String tableName) {
        //初始化表信息
        String queryTableSql = "select table_name, table_comment from information_schema.tables " +
                "where table_name NOT LIKE 'qrtz_%' and table_name NOT LIKE 'gen_%' and table_schema = (select database()) and table_name = ${tableName}";
        Map<String, Object> queryTableData = new HashMap<>();
        queryTableData.put("tableName", tableName);
        GenTable genTable = FastCustomSqlDao.create(GenTable.class, queryTableSql, queryTableData).findOne();
        genTable.setClassName(StrUtil.upperFirst(StrUtil.toCamelCase(genTable.getTableName())));
        initColumnField(genTable);
        return genTable;
    }


    public static void initColumnField(GenTable table) {
        //初始化列属性字段
        String queryColumnSql = "select column_name, " +
                "(case when (is_nullable = 'no' && column_key != 'PRI') then '1' else null end) as is_required, " +
                "(case when column_key = 'PRI' then '1' else '0' end) as is_pk, " +
                "ordinal_position as sort, column_comment, (case when extra = 'auto_increment' then '1' else '0' end) as is_increment, column_type " +
                "from information_schema.columns where table_schema = (select database()) and table_name = (${tableName})  order by ordinal_position";
        Map<String, Object> queryTableData = new HashMap<>();
        queryTableData.put("tableName", table.getTableName());
        List<GenTableColumn> queryColumns = FastCustomSqlDao.create(GenTableColumn.class, queryColumnSql, queryTableData).findAll();
        List<GenTableColumn> columns = new ArrayList<>();
        for (GenTableColumn column : queryColumns) {
            String dataType = getDbType(column.getColumnType());
            String columnName = column.getColumnName();
            // 设置java字段名
            column.setJavaField(StrUtil.toCamelCase(columnName));

            if (arraysContains(GenConstants.COLUMNTYPE_STR, dataType)) {
                column.setJavaType(GenConstants.TYPE_STRING);
            } else if (arraysContains(GenConstants.COLUMNTYPE_TIME, dataType)) {
                column.setJavaType(GenConstants.TYPE_DATE);
            } else if (arraysContains(GenConstants.COLUMNTYPE_NUMBER, dataType)) {

                // 如果是浮点型 统一用BigDecimal
                String[] str = StringUtils.split(StringUtils.substringBetween(column.getColumnType(), "(", ")"), ",");
                if (str != null && str.length == 2 && Integer.parseInt(str[1]) > 0) {
                    column.setJavaType(GenConstants.TYPE_BIGDECIMAL);
                } else if (str != null && str.length == 1 && Integer.parseInt(str[0]) == 1) {
                    column.setJavaType(GenConstants.TYPE_BOOLEAN);
                }
                // 如果是整形
                else if (str != null && str.length == 1 && Integer.parseInt(str[0]) <= 10) {
                    column.setJavaType(GenConstants.TYPE_INTEGER);
                }
                // 长整形
                else {
                    column.setJavaType(GenConstants.TYPE_LONG);
                }
            }
            if (column.getPk() && table.getPkColumn() == null) {
                table.setPkColumn(column);
            }
            columns.add(column);
        }
        table.setColumns(columns);
    }

    /**
     * 校验数组是否包含指定值
     *
     * @param arr         数组
     * @param targetValue 值
     * @return 是否包含
     */
    public static boolean arraysContains(String[] arr, String targetValue) {
        return Arrays.asList(arr).contains(targetValue);
    }

    /**
     * 批量替换前缀
     *
     * @param replacementm 替换值
     * @param searchList   替换列表
     * @return 批量替换前缀
     */
    public static String replaceFirst(String replacementm, String[] searchList) {
        String text = replacementm;
        for (String searchString : searchList) {
            if (replacementm.startsWith(searchString)) {
                text = replacementm.replaceFirst(searchString, "");
                break;
            }
        }
        return text;
    }

    /**
     * 关键字替换
     *
     * @param text 需要被替换的名字
     * @return 替换后的名字
     */
    public static String replaceText(String text) {
        return RegExUtils.replaceAll(text, "(?:表|若依)", "");
    }

    /**
     * 获取数据库类型字段
     *
     * @param columnType 列类型
     * @return 截取后的列类型
     */
    public static String getDbType(String columnType) {
        if (StringUtils.indexOf(columnType, "(") > 0) {
            return StringUtils.substringBefore(columnType, "(");
        } else {
            return columnType;
        }
    }

    /**
     * 获取字段长度
     *
     * @param columnType 列类型
     * @return 截取后的列类型
     */
    public static Integer getColumnLength(String columnType) {
        if (StringUtils.indexOf(columnType, "(") > 0) {
            String length = StringUtils.substringBetween(columnType, "(", ")");
            return Integer.valueOf(length);
        } else {
            return 0;
        }
    }
}

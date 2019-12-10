package com.fast.code.util;

import cn.hutool.core.util.StrUtil;
import com.fast.code.bean.ColumnInfo;
import com.fast.code.bean.BeanInfo;
import com.fast.code.bean.FileCreateConfig;
import com.fast.code.bean.TableInfo;

import java.io.File;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;

/**
 * 数据信息读取工具类
 *
 * @author 张亚伟 https://github.com/kaixinzyw
 */
public class DbUtils {

    private static DbUtils dbUtils = new DbUtils();

    private DbUtils() {
    }

    public static DbUtils getInstance() {
        return dbUtils;
    }

    /**
     * 获取需要生成代码的表信息
     *
     * @param metaData        参数
     * @param tableNames      表名称
     * @param underline2Camel 是否进行转换
     * @return 表信息
     * @throws SQLException 错误
     */
    private Map<String, TableInfo> beanTableInfoAll = new HashMap<>();

    /**
     * 获取所有表属性信息
     *
     * @param conf       配置
     * @param metaData   数据库配置
     * @param tableNames 生成的表信息
     * @return 获取到的表属性映射
     * @throws SQLException 异常
     */
    public List<TableInfo> getAllTables(FileCreateConfig conf, DatabaseMetaData metaData, Set<String> tableNames)
            throws SQLException {
        List<TableInfo> tables = new ArrayList<TableInfo>();
        ResultSet tableRet = getTableResultSet(metaData);
        boolean underline2Camel = conf.getUnderline2CamelStr();
        while (tableRet.next()) {
            TableInfo tableInfo = new TableInfo();
            //表明
            String tableName = tableRet.getString("TABLE_NAME");
            //表注释
            String tableDesc = tableRet.getString("REMARKS");
            ////StaticLog.info("===tableName:" + tableName + "-tableDesc:" + tableDesc);
            for (String _tableName : tableNames) {

                // 字段处理,表的所有字段
                List<ColumnInfo> columns = getAllColumns(metaData, tableName);
                Set<String> packages = new HashSet<String>();
                //字段转属性
                Map<String, Object> pros = columns2Properties(columns, packages, underline2Camel);
                Map<String, String> properties = (Map<String, String>) pros.get("properties");
                List<BeanInfo> properties2 = (List<BeanInfo>) pros.get("properties2");
                Map<String, String> propertiesAnColumns = (Map<String, String>) pros.get("propertiesAnColumns");
                Map<String, String> insertPropertiesAnColumns = (Map<String, String>) pros
                        .get("insertPropertiesAnColumns");
                // 主键处理(主键唯一)
                String primaryKey = primaryKeyColumnName(metaData, tableName);

                String primaryKeyProperty = primaryKey;
                if (underline2Camel) {
                    primaryKeyProperty = StrUtil.toCamelCase(primaryKey);
                }
                Map<String, String> primaryKeyMap = new HashMap<String, String>();
                primaryKeyMap.put(primaryKey, primaryKeyProperty);

                // beanClass
                String beanName = getClassName(tableName, underline2Camel);
                tableInfo.setTableName(tableName);
                tableInfo.setTableDesc(tableDesc);
                tableInfo.setColumns(columns);
                tableInfo.setBeanName(beanName);
                tableInfo.setProperties(properties);
                tableInfo.setProperties2(properties2);
                tableInfo.setPrimaryKey(primaryKeyMap);
                tableInfo.setPackages(packages);
                tableInfo.setPropertiesAnColumns(propertiesAnColumns);
                tableInfo.setInsertPropertiesAnColumns(insertPropertiesAnColumns);
                setPrefix(conf, tableInfo);
                setPackPath(conf, tableInfo);
                beanTableInfoAll.put(beanName, tableInfo);
                if ("all".equals(_tableName) || tableName.trim().equals(_tableName)) {
                    tables.add(tableInfo);
                }
            }

        }
        return tables;
    }


    /**
     * 文件路径设置
     *
     * @param conf      配置信息
     * @param tableInfo 表信息
     */
    public void setPackPath(FileCreateConfig conf, TableInfo tableInfo) {
        String separator = File.separator;
        String resourcesbasePath = new StringBuilder().append(System.getProperty("user.dir")).append(separator).append("src").append(separator).append("main").append(separator).append("resources").append(separator).toString();
        String javabasePath = new StringBuilder().append(System.getProperty("user.dir")).append(separator).append(conf.getChildModuleName()).append(separator).append("src").append(separator).append("main").append(separator).append("java").append(separator).toString();
        String javaPath = javabasePath + conf.getBasePackage().replace(".", separator) + separator;

        String prefixPath = StrUtil.isNotEmpty(tableInfo.getPrefixName()) ? "." + tableInfo.getPrefixName() : "";

        String pojoPackPath = conf.getBasePackage() + "." + conf.getBeanPackage() + prefixPath;
//        String pojoFieldsPackPath = conf.getBasePackage() + "." + conf.getBeanPackage() + ".fast." + conf.getBeanFieldsPackage() + prefixPath;
        String pojoFastDaoPackPath = conf.getBasePackage()+ "." + conf.getBeanPackage() + "." + conf.getBeanFastDao() + prefixPath;
        String dtoPackPath = conf.getBasePackage() + "." + conf.getDtoPackage() + prefixPath;
//        String daoPackPath = conf.getBasePackage() + "." + conf.getDaoPackage() + prefixPath;
        String iservicePackPath = conf.getBasePackage() + "." + conf.getServicePackage() + prefixPath;
        String servicePackPath = conf.getBasePackage() + "." + conf.getServicePackage() + prefixPath + "." + "impl";

        tableInfo.setPojoPackPath(pojoPackPath);
        tableInfo.setPojoName(tableInfo.getBeanName());
        tableInfo.setPojoClassPackPath(tableInfo.getPojoPackPath() + "." + tableInfo.getPojoName());
        tableInfo.setPojoFilePath(javabasePath + tableInfo.getPojoPackPath().replace(".", separator) + separator + tableInfo.getPojoName() + ".java");

        tableInfo.setPojoFastDaoPackPath(pojoFastDaoPackPath);
        tableInfo.setPojoFastDaoName(tableInfo.getPojoName() + "FastDao");
        tableInfo.setPojoFastDaoClassPackPath(tableInfo.getPojoFastDaoPackPath() + "." + tableInfo.getPojoFastDaoName());
        tableInfo.setPojoFastDaoFilePath(javabasePath + tableInfo.getPojoFastDaoPackPath().replace(".", separator) + separator + tableInfo.getPojoFastDaoName() + ".java");

//        tableInfo.setPojoFieldsPackPath(pojoFieldsPackPath);
//        tableInfo.setPojoFieldsName(tableInfo.getPojoName() + "Fields");
//        tableInfo.setPojoFieldsClassPackPath(tableInfo.getPojoFieldsPackPath() + "." + tableInfo.getPojoFieldsName());
//        tableInfo.setPojoFieldsFilePath(javabasePath + tableInfo.getPojoFieldsPackPath().replace(".", separator) + separator + tableInfo.getPojoFieldsName() + ".java");

        tableInfo.setDtoPackPath(dtoPackPath);
        tableInfo.setDtoName(tableInfo.getPojoName() + "Dto");
        tableInfo.setDtoClassPackPath(tableInfo.getDtoPackPath() + "." + tableInfo.getDtoName());
        tableInfo.setDtoFilePath(javabasePath + tableInfo.getDtoPackPath().replace(".", separator) + separator + tableInfo.getDtoName() + ".java");

//        tableInfo.setDaoPackPath(daoPackPath);
//        tableInfo.setDaoName(tableInfo.getPojoName() + "Dao");
//        tableInfo.setDaoClassPackPath(tableInfo.getDaoPackPath() + "." + tableInfo.getDaoName());
//        tableInfo.setDaoFilePath(javabasePath + tableInfo.getDaoPackPath().replace(".", separator) + separator + tableInfo.getDaoName() + ".java");

        tableInfo.setIservicePackPath(iservicePackPath);
        tableInfo.setIserviceName("I" + tableInfo.getPojoName() + "Service");
        tableInfo.setIserviceClassPackPath(tableInfo.getIservicePackPath() + "." + tableInfo.getIserviceName());
        tableInfo.setIserviceFilePath(javabasePath + tableInfo.getIservicePackPath().replace(".", separator) + separator + tableInfo.getIserviceName() + ".java");

        tableInfo.setServicePackPath(servicePackPath);
        tableInfo.setServiceName(tableInfo.getPojoName() + "ServiceImpl");
        tableInfo.setServiceClassPackPath(tableInfo.getServicePackPath() + "." + tableInfo.getServiceName());
        tableInfo.setServiceFilePath(javabasePath + tableInfo.getServicePackPath().replace(".", separator) + separator + tableInfo.getServiceName() + ".java");
    }

    /**
     * 表前缀设置
     *
     * @param conf      配置
     * @param tableInfo 表信息
     */
    public void setPrefix(FileCreateConfig conf, TableInfo tableInfo) {
        String prefixName = "";
        String fileName = tableInfo.getBeanName();
        /**
         * 有表名类别
         */
        if (conf.getPrefix()) {
            if (StrUtil.isNotEmpty(conf.getPrefixName())) {
                prefixName = conf.getPrefixName();
                if (tableInfo.getTableName().indexOf(prefixName) == 0) {
                    fileName = fileName.substring(conf.getPrefixName().length());
                }
            } else {
                Integer prefixIndex = tableInfo.getTableName().indexOf("_");
                if (prefixIndex < 0) {
                    prefixIndex = tableInfo.getTableName().indexOf("-");
                }
                if (prefixIndex > -1) {
                    prefixName = tableInfo.getBeanName().substring(0, prefixIndex).toLowerCase();
                    fileName = fileName.substring(prefixName.length());
                }
            }
            if (!conf.getPrefixFileDir()) {
                prefixName = "";
            }
        }
        tableInfo.setPrefixName(prefixName);
        tableInfo.setBeanName(fileName);
    }

    /**
     * 返回一个与特定数据库的连接
     *
     * @param conf 配置
     * @return 数据库连接
     * @throws ClassNotFoundException 错误
     */
    public Connection getConnection(FileCreateConfig conf) throws ClassNotFoundException {
        Connection connection = null;
        try {
            String driverClass = conf.getDriverClass();
            String url = conf.getUrl();
            String user = conf.getUser();
            String password = conf.getPassword();
            Class.forName(driverClass);
            connection = DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }

    /**
     * 只做单主键代码的生成
     *
     * @param metaData  参数
     * @param tableName 表名称
     * @return 主键
     * @throws SQLException 错误
     */
    public String primaryKeyColumnName(DatabaseMetaData metaData, String tableName) throws SQLException {
        String primaryKeyColumnName = null;
        String catalog = metaData.getConnection().getCatalog();
        ResultSet primaryKeyResultSet = metaData.getPrimaryKeys(catalog, null, tableName);
        while (primaryKeyResultSet.next()) {
            primaryKeyColumnName = primaryKeyResultSet.getString("COLUMN_NAME");
            break;
        }
        if (primaryKeyColumnName == null) {
            primaryKeyColumnName = "id";
        }
        return primaryKeyColumnName;
    }

    /**
     * 表字段转换为属性字段
     *
     * @param columns         参数
     * @param packages        参数
     * @param underline2Camel 是否大小写转换
     * @return 结果
     */
    private Map<String, Object> columns2Properties(List<ColumnInfo> columns, Set<String> packages,
                                                   boolean underline2Camel) {
        Map<String, String> properties = new HashMap<String, String>();
        List<BeanInfo> properties2 = new ArrayList<>();
        Map<String, String> propertiesAnColumns = new HashMap<String, String>();
        Map<String, String> insertPropertiesAnColumns = new HashMap<String, String>();

        for (ColumnInfo entry : columns) {
            // 字段名
            String columnName = entry.getColumnName();
            // 字段类型
            String columnType = entry.getColumnType();
            // 字段类型
            String columnRemarks = entry.getColumnRemarks();
            String propertyName = columnName;
            if (underline2Camel) {
                propertyName = StrUtil.toCamelCase(columnName);
            }
            String propertyType = getFieldType(columnType, packages);
            properties.put(propertyName, propertyType);
            BeanInfo beanInfo = new BeanInfo();
            beanInfo.setPropertyName(propertyName);
            beanInfo.setPropertyType(propertyType);
            beanInfo.setPropertyDesc(columnRemarks);
            properties2.add(beanInfo);
            propertiesAnColumns.put(propertyName, columnName);
            if (!excludeInsertProperties(propertyName)) {
                insertPropertiesAnColumns.put(propertyName, columnName);
            }
        }

        Map<String, Object> pros = new HashMap<>();
        pros.put("properties", properties);
        pros.put("properties2", properties2);
        pros.put("propertiesAnColumns", propertiesAnColumns);
        pros.put("insertPropertiesAnColumns", insertPropertiesAnColumns);

        return pros;
    }

    /**
     * 判断字段是否是id等信息
     *
     * @param propertyName 属性名
     * @return 是否包含
     */
    public boolean excludeInsertProperties(String propertyName) {
        return "id".equals(propertyName) || "createTime".equals(propertyName) || "updateTime".equals(propertyName)
                || "delFlag".equals(propertyName);
    }

    /**
     * 获取表所有字段
     *
     * @param metaData  参数
     * @param tableName 表名称
     * @return 字段集
     * @throws SQLException SQL错误
     */
    public List<ColumnInfo> getAllColumns(DatabaseMetaData metaData, String tableName) throws SQLException {
        String columnName;
        String columnType;
        String remarks;
        ResultSet colRet = metaData.getColumns(metaData.getConnection().getCatalog(), null, tableName, "%");
        List<ColumnInfo> columns = new ArrayList<>();
        while (colRet.next()) {
            columnName = colRet.getString("COLUMN_NAME");
            columnType = colRet.getString("TYPE_NAME");
            int datasize = colRet.getInt("COLUMN_SIZE");
            int digits = colRet.getInt("DECIMAL_DIGITS");
            int nullable = colRet.getInt("NULLABLE");
            remarks = colRet.getString("remarks");
            ColumnInfo info = new ColumnInfo();
            info.setColumnName(columnName);
            info.setColumnType(columnType);
            info.setColumnRemarks(remarks);
            columns.add(info);
            // //StaticLog.info(
            //        remarks + "-" + columnName + "-" + columnType + "-" + datasize + "-" + digits + "-" + nullable);
        }
        return columns;
    }


    /**
     * 获取TableResultSet
     *
     * @param metaData 参数
     * @return TableResultSet
     * @throws SQLException SQL错误
     */
    public ResultSet getTableResultSet(DatabaseMetaData metaData) throws SQLException {
        String catalog = metaData.getConnection().getCatalog();
        ResultSet tableRet = metaData.getTables(catalog, null, null, new String[]{"TABLE"});
        return tableRet;
    }

    /**
     * 获取DatabaseMetaData
     *
     * @param connection 连接
     * @return DatabaseMetaData
     * @throws SQLException 错误
     */
    public DatabaseMetaData getMetaData(Connection connection) throws SQLException {
        DatabaseMetaData metaData = connection.getMetaData();
        return metaData;
    }

    /**
     * 如果table名是t_开头，则去掉t_,其他_变驼峰，第一个字母大写。
     *
     * @param tableName       表名称
     * @param underline2Camel 是否下划线转换
     * @return 转换结果
     */
    public static String getClassName(String tableName, boolean underline2Camel) {
        String res = tableName;
        // 去t_
        if (res.startsWith("t_")) {
            res = res.substring(2);
        }

        if (underline2Camel) {
            // 变驼峰
            res = StrUtil.toCamelCase(res);
        }
        // 首字符大写
        res = res.substring(0, 1).toUpperCase() + res.substring(1);
        return res;
    }

    /**
     * 设置字段类型 MySql数据类型
     *
     * @param columnType 列类型字符串
     * @param packages   封装包信息
     * @return 数据类型
     */
    public static String getFieldType(String columnType, Set<String> packages) {

        columnType = columnType.toLowerCase();
        if ("varchar".equals(columnType) || "nvarchar".equals(columnType) || columnType.equals("char")
                || columnType.equals("text") || columnType.equals("mediumtext"))
        // columnType.equals("tinytext")||columnType.equals("mediumtext")||columnType.equals("longtext")
        {
            return "String";
        } else if (columnType.equals("tinyblob") || columnType.equals("blob") || columnType.equals("mediumblob")
                || columnType.equals("longblob")) {
            return "byte[]";
        } else if (columnType.equals("datetime") || columnType.equals("date") || columnType.equals("timestamp")
                || columnType.equals("time") || columnType.equals("year")) {
            packages.add("import java.util.Date;");
            return "Date";
        } else if (columnType.equals("bit")) {
            return "Boolean";
        } else if (columnType.equals("bit") || columnType.equals("int") || columnType.equals("tinyint")
                || columnType.equals("smallint")) {
            return "Integer";
        } else if (columnType.equals("int unsigned")) {
            return "Integer";
        } else if (columnType.equals("bigint unsigned") || columnType.equals("bigint")) {
            return "Long";
        } else if (columnType.equals("float")) {
            return "Float";
        } else if (columnType.equals("double")) {
            return "Double";
        } else if (columnType.equals("decimal")) {
            packages.add("import java.math.BigDecimal;");
            return "BigDecimal";
        }
        return "ErrorType";
    }

    /**
     * 设置类标题注释
     *
     * @param packages  参数
     * @param className 类名
     */
    public static void getTitle(StringBuilder packages, String className) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日");
        packages.append("\r\n/**\r\n");
        packages.append("*\r\n");
        packages.append("* 标题: " + className + "<br/>\r\n");
        packages.append("* 说明: <br/>\r\n");
        packages.append("*\r\n");
        packages.append("* 作成信息: DATE: " + format.format(new Date()) + " NAME: author\r\n");
        packages.append("*\r\n");
        packages.append("* 修改信息<br/>\r\n");
        packages.append("* 修改日期 修改者 修改ID 修改内容<br/>\r\n");
        packages.append("*\r\n");
        packages.append("*/\r\n");
    }

}

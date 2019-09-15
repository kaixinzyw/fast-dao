package com.fast.db.template.code;



import com.fast.db.template.code.bean.TableInfo;
import com.fast.db.template.code.bean.CodeCreateParameter;
import com.fast.db.template.code.bean.Conf;
import com.fast.db.template.code.creator.FileCreator;
import com.fast.db.template.code.factory.SimpleFactory;
import com.fast.db.template.code.util.DbUtils;
import freemarker.template.TemplateException;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

/**
 * 代码生成执行类
 *
 * @author zhangjh
 */
public class TableFileCreateUtils {



    /**
     * @throws SQLException
     * @throws ClassNotFoundException
     * @throws IOException
     * @throws TemplateException
     */
    public void create(CodeCreateParameter codeParameter) throws SQLException, ClassNotFoundException, IOException, TemplateException {
        //基础信息
        Conf conf = new Conf().getConf(codeParameter);
        //表集合
        List<TableInfo> tableInfos = getTableInfos(conf);
        ////System.out.println("tableInfos ==>" + tableInfos);
        //生成文件
        createFile(conf, tableInfos);


    }

    /**
     * 需要生成代码的表
     *
     * @param conf
     * @return
     * @throws ClassNotFoundException
     * @throws SQLException
     */
    private List<TableInfo> getTableInfos(Conf conf) throws ClassNotFoundException, SQLException {
    	boolean underline2Camel = conf.isUnderline2Camel();
        Connection connection = DbUtils.getInstance().getConnection(conf);
        DatabaseMetaData metaData = DbUtils.getInstance().getMetaData(connection);
        List<String> tableNames = Arrays.asList(conf.getTables().split(","));
        return DbUtils.getInstance().getAllTables(metaData, tableNames,underline2Camel);
    }

    /**
     * @param conf
     * @param tableInfos
     * @throws IOException
     * @throws TemplateException
     */
    private void createFile(Conf conf, List<TableInfo> tableInfos) throws IOException, TemplateException {
        List<String> modules = conf.getModules();
        FileCreator creator = null;
        for (TableInfo tableInfo : tableInfos) {
            for (String module : modules) {
                creator = SimpleFactory.create(module, conf);
                creator.createFile(tableInfo);
            }
        }
    }




}

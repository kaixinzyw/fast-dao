package com.fast.generator;


import cn.hutool.core.collection.CollUtil;
import com.fast.generator.util.DbUtils;
import com.fast.generator.bean.FileCreateConfig;
import com.fast.generator.bean.TableInfo;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;

import java.io.*;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 模板代码生成执行类
 * 使用Freemarker进行模板文件生成
 * @author 张亚伟 https://github.com/kaixinzyw
 */
public class TableFileCreateUtils {

    /**
     * 生成文件
     * @param codeParameter 重要:请参考参数设置
     */
    public static void create(FileCreateConfig codeParameter){
        try {
            //表信息
            List<TableInfo> tableInfos = getTableInfos(codeParameter);
            //生成文件
            createFile(codeParameter, tableInfos);
        }catch (Exception e) {
            e.printStackTrace();
        }



    }

    /**
     * 需要生成代码的表
     *
     * @param conf 配置
     * @return 表信息
     * @throws ClassNotFoundException 错误
     * @throws SQLException           错误
     */
    private static List<TableInfo> getTableInfos(FileCreateConfig conf) throws ClassNotFoundException, SQLException {
        boolean underline2Camel = conf.getUnderline2CamelStr();
        Connection connection = DbUtils.getInstance().getConnection(conf);
        DatabaseMetaData metaData = DbUtils.getInstance().getMetaData(connection);

        Set<String> tableNames = conf.getCreateTables();
        if (CollUtil.isEmpty(tableNames)) {
            tableNames = CollUtil.newHashSet("all");
        }
        return DbUtils.getInstance().getAllTables(conf, metaData, tableNames);
    }

    /**
     * @param conf       配置
     * @param tableInfos 表信息
     * @throws IOException       错误
     * @throws TemplateException 错误
     */
    private static void createFile(FileCreateConfig conf, List<TableInfo> tableInfos) throws IOException, TemplateException {
        Configuration cfg = new Configuration(Configuration.VERSION_2_3_22);
        cfg.setClassLoaderForTemplateLoading(TableFileCreateUtils.class.getClassLoader(), "templates");
        cfg.setDefaultEncoding("UTF-8");
        cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);

        Set<String> modules = conf.getNeedModules();
        Map<String, Object> root = new HashMap<String, Object>();
        root.put("conf", conf);
        for (TableInfo tableInfo : tableInfos) {
            root.put("table", tableInfo);
            for (String module : modules) {
                if (FileCreateConfig.CodeCreateModule.POJO.codeModule.equals(module)) {
                    Template temp = cfg.getTemplate("Pojo.ftl");
                    createFile(conf, tableInfo.getPojoFilePath(), root, temp);
                }
//                else if (FileCreateConfig.CodeCreateModule.FastPojo.codeModule.equals(module)) {
//                    Template temp = cfg.getTemplate("FastPojo.ftl");
//                    createFile(conf, tableInfo.getFastPojoFilePath(), root, temp);
//                }
                else if (FileCreateConfig.CodeCreateModule.FastDAO.codeModule.equals(module)) {
                    Template temp = cfg.getTemplate("PojoFastDao.ftl");
                    createFile(conf, tableInfo.getPojoFastDaoFilePath(), root, temp);
                } else if (FileCreateConfig.CodeCreateModule.Service.codeModule.equals(module)) {
                    Template temp = cfg.getTemplate("IService.ftl");
                    createFile(conf, tableInfo.getIserviceFilePath(), root, temp);
                } else if (FileCreateConfig.CodeCreateModule.ServiceImpl.codeModule.equals(module)) {
                    Template temp = cfg.getTemplate("ServiceImpl.ftl");
                    createFile(conf, tableInfo.getServiceFilePath(), root, temp);
                }  else if (FileCreateConfig.CodeCreateModule.DTO.codeModule.equals(module)) {
                    Template temp = cfg.getTemplate("Dto.ftl");
                    createFile(conf, tableInfo.getDtoFilePath(), root, temp);
                }  else if (FileCreateConfig.CodeCreateModule.DAO.codeModule.equals(module)) {
                    Template temp = cfg.getTemplate("Dao.ftl");
                    createFile(conf, tableInfo.getDaoFilePath(), root, temp);
                }
//                else if (FileCreateConfig.CodeCreateModule.PojoFields.codeModule.equals(module)) {
//                    Template temp = cfg.getTemplate("PojoFields.ftl");
//                    createFile(conf, tableInfo.getPojoFieldsFilePath(), root, temp);
//                }
//                else if (FileCreateConfig.CodeCreateModule.Dao.codeModule.equals(module)) {
//                    Template temp = cfg.getTemplate("Dao.ftl");
//                    createFile(conf, tableInfo.getDaoFilePath(), root, temp);
//                }
            }
        }
    }

    /**
     * @param filePath 文件路径
     * @param root     data
     * @param temp     模板
     * @throws IOException       文件创建是吧
     * @throws TemplateException 模板读取失败
     */
    private static void createFile(FileCreateConfig conf, String filePath, Map<String, Object> root, Template temp) throws IOException, TemplateException {
        String separator = File.separator;
        boolean replaceFile = conf.getReplaceFile();
        String fileName = filePath.substring(filePath.lastIndexOf(separator) + 1);
        String subPath;
        subPath = filePath.substring(0, filePath.lastIndexOf(separator));
        File directory = new File(subPath);
        if (!directory.exists()) {
            directory.mkdirs();
        }
        File file = new File(filePath);
        boolean needCreatFile = false;
        if (!file.exists()) {
            file.createNewFile();
            needCreatFile = true;
        } else {
            if (replaceFile) {
                file.delete();
                file.createNewFile();
                needCreatFile = true;
            }
        }
        if (needCreatFile) {
            OutputStream os = new FileOutputStream(file);
            Writer out = new OutputStreamWriter(os);
            temp.process(root, out);
        }
    }

}

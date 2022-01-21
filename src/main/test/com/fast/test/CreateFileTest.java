package com.fast.test;
import com.fast.generator.TableFileCreateUtils;
import com.fast.generator.bean.FileCreateConfig;

/**
 * 文件创建
 */
public class CreateFileTest {
    public static void main(String[] args) {
        FileCreateConfig config = new FileCreateConfig();
        //数据库连接
        config.setDBInfo("jdbc:mysql://127.0.0.1:3306/my_test?useUnicode=true&characterEncoding=utf-8&serverTimezone=Asia/Shanghai","root","kaixinzyw","com.mysql.cj.jdbc.Driver");
        //文件生成的包路径
        config.setBasePackage("com.fast.test");
        //选择生成的文件
        config.setNeedModules(FileCreateConfig.CodeCreateModule.All);
        //是否生成表前缀
        config.setPrefix(false,false,null);
        //是否使用lombok插件,默认false
        config.setUseLombok(true);
        //是否在DTO上使用Swagger2注解,默认false
        config.setUseDTOSwagger2(true);
        //是否在POJO上使用Swagger2注解,默认false
        config.setUsePOJOSwagger2(true);
        //DOT是否继承POJO
        config.setDtoExtendsPOJO(true);
        //是否下划线转大小写,默认true
        config.setUnderline2CamelStr(true);
        //是否覆盖原文件,默认false
        config.setReplaceFile(true);
        //需要生成的表名 (可选值,具体表名"tab1","tab2"或all)
        config.setCreateTables("user");
        //生成代码
        TableFileCreateUtils.create(config);
    }
}

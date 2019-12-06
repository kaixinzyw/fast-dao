package com.fast.demo;
import com.fast.code.TableFileCreateUtils;
import com.fast.code.bean.FileCreateConfig;

public class CreateFileTest {
    public static void main(String[] args) {
        FileCreateConfig config = new FileCreateConfig();
        //数据库连接
        config.setDBInfo("jdbc:mysql://127.0.0.1:3306/fast-test?useUnicode=true&characterEncoding=utf-8&serverTimezone=UTC","root","kaixin001","com.mysql.cj.jdbc.Driver");
        //文件生成的包路径
        config.setBasePackage("com.fast.demo");
        //是否生成表前缀
        config.setPrefix(false,false,null);
        //是否使用lombok插件
        config.setUseLombok(false);
        //是否下划线转大小写,默认true
        config.setUnderline2CamelStr(true);
        //是否覆盖原文件,默认false
        config.setReplaceFile(false);
        //项目多模块空间
//        config.setChildModuleName("service");
        //需要生成的表名 (可选值,具体表名"tab1,tab2"或all)
        config.setCreateTables("all");
        //生成代码
        TableFileCreateUtils.create(config);
    }
}

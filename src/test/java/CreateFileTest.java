//import com.fast.db.template.code.TableFileCreateUtils;
//import com.fast.db.template.code.bean.FileCreateConfig;
//import freemarker.template.TemplateException;
//
//import java.io.IOException;
//import java.sql.SQLException;
//
//public class CreateFileTest {
//    public static void main(String[] args) {
//        FileCreateConfig config = new FileCreateConfig();
//        //数据库连接
//        config.setDBInfo("jdbc:mysql://127.0.0.1:3306/zyw_test?useUnicode=true&characterEncoding=utf-8&serverTimezone=UTC","root","kaixin001","com.mysql.cj.jdbc.Driver");
//        //文件生成的包路径
//        config.setBasePackage("com.db.test");
//        //需要生成的文件 (可选值Base<生成Pojo类,Pojo查询模板,Pojo字段集>,Service<Service接口>,ServiceImpl<Service实现类>,Dto<Dto对象>,Dao<Dao对象>, All<上述所有文件>)
//        config.setNeedModules(FileCreateConfig.CodeCreateModule.All);
//        //是否生成表前缀
//        config.setPrefix(false,false,null);
//        //是否使用lombok插件
//        config.setUseLombok(true);
//        //是否下划线转大小写,默认true
//        config.setUnderline2CamelStr(true);
//        //是否覆盖原文件,默认false
//        config.setReplaceFile(false);
//        //项目多模块空间
////        config.setChildModuleName("service");
//        //需要生成的表名 (可选值,具体表名或all)
//        config.setCreateTables("all");
//        //生成代码
//        TableFileCreateUtils.create(config);
//    }
//
//    public static void demo(){
//        FileCreateConfig fileCreateConfig = new FileCreateConfig();
//        //数据库连接
//        fileCreateConfig.setDBInfo("jdbc:mysql://127.0.0.1:3306/zyw_test?useUnicode=true&characterEncoding=utf-8&serverTimezone=UTC","root","kaixin001","com.mysql.cj.jdbc.Driver");
//        //文件生成的包路径
//        fileCreateConfig.setBasePackage("com.db.test");
//        //生成代码
//        TableFileCreateUtils.create(fileCreateConfig);
//    }
//}

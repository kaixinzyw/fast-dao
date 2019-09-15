import com.fast.db.template.code.TableFileCreateUtils;
import com.fast.db.template.code.bean.CodeCreateParameter;
import freemarker.template.TemplateException;

import java.io.IOException;
import java.sql.SQLException;

public class FileTest {
    public static void main(String[] args) throws ClassNotFoundException, SQLException, TemplateException, IOException {
        CodeCreateParameter codeParameter = new CodeCreateParameter();
        //数据库驱动
        codeParameter.setDriverClass("com.mysql.cj.jdbc.Driver");

        codeParameter.setUrl("jdbc:mysql://127.0.0.1:3306/test?useUnicode=true&characterEncoding=utf-8&useSSL=false&serverTimezone=UTC");
        codeParameter.setUser("root");
        codeParameter.setPassword("kaixinzyw");
        codeParameter.setBasePackage("com.example.demo");


        codeParameter.setNeedModules(CodeCreateParameter.CodeCreateModule.All);
        codeParameter.setPrefix(false);
        codeParameter.setPrefixName("user");
        codeParameter.setCreateTables("user_test");
        codeParameter.setDeletedField("deleted");
        codeParameter.setNoDeletedDefaults("false");

        codeParameter.setUnderline2CamelStr(true);
        //生成代码
        new TableFileCreateUtils().create(codeParameter);
    }
}

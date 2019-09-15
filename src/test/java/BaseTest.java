//import cn.hutool.json.JSONUtil;
//import com.example.demo.dao.UserTestDao;
//import com.example.demo.pojo.UserTest;
//import com.example.demo.pojo.template.fields.UserTestFields;
//import com.fast.jdbc.config.AutomaticParameterSetting;
//import com.fast.jdbc.config.FastDBConnection;
//import com.fast.jdbc.config.PrimaryKeyTypeEnum;
//import com.fast.jdbc.template.FastExample;
//import com.fast.jdbc.utils.page.PageInfo;
//import org.junit.Test;
//import org.springframework.jdbc.core.JdbcTemplate;
//import org.springframework.jdbc.datasource.DriverManagerDataSource;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class BaseTest {
//
//
//    @Test
//    public void insert(){
//        create();
//        UserTest userTest = new UserTest();
//        userTest.setName("insert");
//        userTest.setAge(1);
//        UserTestDao userTestDao = new UserTestDao();
//        Boolean insert = userTestDao.insert(userTest);
//        //System.out.println(insert);
//    }
//
//    @Test
//    public void inrtList() {
//        create();
//        long time = System.currentTimeMillis();
//        List<UserTest> users = new ArrayList<>();
//        for (int i = 1; i <= 100; i++) {
//            UserTest user = new UserTest();
//            user.setName("张亚伟" + i);
//            user.setAge(i);
//            users.add(user);
//        }
//        UserTestDao userTestDao = new UserTestDao();
//        Integer integer = userTestDao.inrtList(users);
//        //System.out.println(integer);
//        //System.out.println("执行数据共用" + (System.currentTimeMillis() - time));
//
//    }
//
//    @Test
//    public void deleteByPrimaryKey(){
//        create();
//        UserTestDao userTestDao = new UserTestDao();
//        Boolean aBoolean = userTestDao.deleteByPrimaryKey("ec37288a7c504047b09cc6a27c55fec5");
//        //System.out.println(aBoolean);
//    }
//
//    @Test
//    public void findByPrimaryKey(){
//        create();
//        UserTestDao userTestDao = new UserTestDao();
//        UserTest byIn = userTestDao.findByPrimaryKey("ec37288a7c504047b09cc6a27c55fec5");
//        //System.out.println(JSONUtil.toJsonStr(byIn));
//    }
//
//    @Test
//    public void findByIn(){
//        create();
//        UserTestDao userTestDao = new UserTestDao();
//        List<UserTest> byIn = userTestDao.findByIn(UserTestFields.Name, "张亚伟1", "张亚伟2");
//        //System.out.println(JSONUtil.toJsonStr(byIn));
//    }
//
//
//    @Test
//    public void findAll(){
//        create();
//        UserTestDao userTestDao = new UserTestDao();
//        List<UserTest> byIn = userTestDao.findAll();
//        //System.out.println(JSONUtil.toJsonStr(byIn));
//    }
//
//    @Test
//    public void updateByPrimaryKeySelective(){
//        create();
//        UserTest userTest = new UserTest();
//        userTest.setId("ec37288a7c504047b09cc6a27c55fec5");
//        userTest.setName("updateByPrimaryKeySelective");
//
//        UserTestDao userTestDao = new UserTestDao();
//        Boolean update = userTestDao.updateByPrimaryKeySelective(userTest);
//        //System.out.println(update);
//    }
//
//    @Test
//    public void findOne(){
//        create();
//        UserTest userTest = new UserTest();
//        userTest.setName("张亚伟1");
//
//        FastExample fastExample = new FastExample();
//        fastExample.field(UserTestFields.Name).like("%张亚伟%").or().equal("张亚伟2");
//        fastExample.field(UserTestFields.Age).in(1,2,3,4,5,6,7).or().equal(1);
//        fastExample.equalObject(userTest);
//
//
//        UserTestDao userTestDao = new UserTestDao();
//        UserTest byIn = userTestDao.findOne(fastExample);
//        //System.out.println(JSONUtil.toJsonStr(byIn));
//    }
//
//
//    @Test
//    public void findAll2(){
//        create();
//
//        FastExample fastExample = new FastExample();
//        fastExample.field(UserTestFields.Name).like("%张亚伟%");
//        fastExample.field(UserTestFields.Age).less(4);
//
//
//        UserTestDao userTestDao = new UserTestDao();
//        List<UserTest> all = userTestDao.findAll(fastExample);
//        //System.out.println(JSONUtil.toJsonStr(all));
//    }
//
//    @Test
//    public void findCount(){
//        create();
//
//        FastExample fastExample = new FastExample();
//        fastExample.field(UserTestFields.Name).like("%张亚伟%");
//        fastExample.field(UserTestFields.Age).less(4);
//
//
//        UserTestDao userTestDao = new UserTestDao();
//        Integer count = userTestDao.findCount(fastExample);
//        //System.out.println(count);
//    }
//
//    @Test
//    public void findPage(){
//        create();
//
//        UserTest userTest = new UserTest();
//        userTest.setName("张亚伟1");
//
//        FastExample fastExample = new FastExample();
//        fastExample.field(UserTestFields.Name).like("%张亚伟%");
//        fastExample.field(UserTestFields.Age).less(4);
//
//
//        UserTestDao userTestDao = new UserTestDao();
//        PageInfo<UserTest> page = userTestDao.findPage(fastExample, 1, 10);
//        //System.out.println(page);
//    }
//
//    @Test
//    public void update(){
//        create();
//
//        UserTest userTest = new UserTest();
//        userTest.setName("update");
//
//        FastExample fastExample = new FastExample();
//        fastExample.field(UserTestFields.Name).like("%张亚伟%");
//        fastExample.field(UserTestFields.Age).less(5);
//
//
//        UserTestDao userTestDao = new UserTestDao();
//        Integer count = userTestDao.update(userTest,fastExample);
//        //System.out.println(count);
//    }
//
//
//    @Test
//    public void updateSelective(){
//        create();
//
//        UserTest userTest = new UserTest();
//        userTest.setName("update");
//
//        FastExample fastExample = new FastExample();
//        fastExample.field(UserTestFields.Name).like("%张亚伟%");
//        fastExample.field(UserTestFields.Age).less(5);
//
//
//        UserTestDao userTestDao = new UserTestDao();
//        Integer count = userTestDao.updateSelective(userTest,fastExample);
//        //System.out.println(count);
//    }
//
//    @Test
//    public void delete(){
//        create();
//
//        FastExample fastExample = new FastExample();
//        fastExample.field(UserTestFields.Name).like("%张亚伟%");
//        fastExample.field(UserTestFields.Age).less(5);
//
//
//        UserTestDao userTestDao = new UserTestDao();
//        Integer count = userTestDao.delete(fastExample);
//        //System.out.println(count);
//    }
//
//
//
//
//
//
//
//
//    private void create() {
//        AutomaticParameterSetting.setPrimaryKeyField("id");
//        AutomaticParameterSetting.setPrimaryKeyType(PrimaryKeyTypeEnum.UUID.getType());
//        AutomaticParameterSetting.setCreateTimeField("createTime");
//        AutomaticParameterSetting.setUpdateTimeField("updateTime");
//        AutomaticParameterSetting.setDeleteField("deleted");
//        AutomaticParameterSetting.setDeleteValue(true);
//        AutomaticParameterSetting.setNoDeleteValue(false);
//
//        DriverManagerDataSource dataSource = new DriverManagerDataSource();
//        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
//        dataSource.setUrl("jdbc:mysql://127.0.0.1:3306/user?useUnicode=true&characterEncoding=utf-8&useSSL=false&serverTimezone=UTC");
//        dataSource.setUsername("root");
//        dataSource.setPassword("kaixin001");
//        FastDBConnection.setJdbcTemplate(new JdbcTemplate(dataSource));
//    }
//
//}

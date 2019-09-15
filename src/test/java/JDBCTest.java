//import cn.hutool.json.JSONUtil;
//import com.alibaba.fastjson.JSONObject;
//import com.example.demo.pojo.UserTest;
//import com.example.demo.pojo.template.UserTestTemplate;
//import com.fast.jdbc.config.AutomaticParameterSetting;
//import com.fast.jdbc.config.FastDBConnection;
//import com.fast.jdbc.config.PrimaryKeyTypeEnum;
//import com.fast.jdbc.template.FastExample;
//import com.fast.jdbc.utils.page.PageInfo;
//import org.junit.Test;
//import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
//import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
//import org.springframework.jdbc.datasource.DriverManagerDataSource;
//
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.List;
//import java.util.concurrent.TimeUnit;
//
//public class JDBCTest {
//
//    @Test
//    public void test2() {
//        create();
//        long time = System.currentTimeMillis();
//        for (int i = 1; i <= 1000000; i++) {
//            UserTest user = new UserTest();
//            user.setName("张亚伟" + i);
//            user.setAge(i);
//            UserTestTemplate.fastFunction().insert(user);
//        }
//        //System.out.println("执行数据共用" + (System.currentTimeMillis() - time));
//    }
//
//    @Test
//    public void test2_1() {
//        create();
//        long time = System.currentTimeMillis();
//        List<UserTest> users = new ArrayList<>();
//        for (int i = 1; i <= 10; i++) {
//            UserTest user = new UserTest();
//            user.setName("张亚伟" + i);
//            user.setAge(i);
//            users.add(user);
//        }
//        UserTestTemplate.fastFunction().insertList(users);
//
//        //System.out.println("执行数据共用" + (System.currentTimeMillis() - time));
//
//    }
//
//    @Test
//    public void test3() {
//        create();
//        int i = 0;
//        while (i < 10) {
//            long time = System.currentTimeMillis();
//            List<UserTest> all = UserTestTemplate.fastFunction().findAll();
//            //System.out.println("执行数据共用" + (System.currentTimeMillis() - time));
//
//            time = System.currentTimeMillis();
//            all = UserTestTemplate.fastFunction().findAll();
//            //System.out.println("执行缓存共用" + (System.currentTimeMillis() - time));
//
//            UserTest userTest = new UserTest();
//            userTest.setId("0695135f05ff4acf9986ce20854986d5");
//            userTest.setAge(-1);
//            UserTestTemplate.fastFunction().updateByPrimaryKey(userTest);
//            i++;
//        }
//
//    }
//
//    @Test
//    public void test4() {
//        create();
//        UserTest user = new UserTest();
//        user.setName("张亚伟1");
//        UserTestTemplate userTemplate = new UserTestTemplate();
//        //数据中所有
//        userTemplate.fastEqualToUserTest(user);
//        userTemplate.fastName().like("张亚伟%");
//        userTemplate.fastAge().in(1, 2, 3);
//        userTemplate.fastDeleted().in(0);
//        userTemplate.fastName().in("张亚伟1", "张亚伟2", "张亚伟3");
//
//        user = userTemplate.dbTemplate().findOne();
//        //System.out.println(JSONObject.toJSONString(user));
//    }
//
//    @Test
//    public void test5() {
//        create();
//        UserTest user = new UserTest();
//        user.setName("张亚伟1");
//        UserTestTemplate userTemplate = new UserTestTemplate();
//        userTemplate.fastEqualToUserTest(user);
//        userTemplate.fastName().like("张亚伟%");
//        userTemplate.fastAge().in(1, 2, 3);
//        userTemplate.fastDeleted().in(false);
//        userTemplate.fastName().in("张亚伟1", "张亚伟2", "张亚伟3");
//        userTemplate.fastCreateTime().in(new Date(), new Date(), new Date());
//        List<UserTest> all = userTemplate.dbTemplate().findAll();
//        //System.out.println(all.size());
//        //System.out.println(JSONUtil.toJsonStr(all));
//    }
//
//    @Test
//    public void test6() {
//        create();
//        UserTestTemplate userTemplate = new UserTestTemplate();
//        userTemplate.fastName().like("张亚伟%");
//        userTemplate.fastAge().in(1, 2, 3);
//        userTemplate.fastDeleted().in(0);
//        userTemplate.fastName().in("张亚伟1", "张亚伟2", "张亚伟3");
//        Integer count = userTemplate.dbTemplate().findCount();
//        //System.out.println(count);
//    }
//
//    @Test
//    public void test7() {
//        create();
//        UserTest userTest = new UserTest();
//        userTest.setAge(18);
//
//        //查询条件包含了目前所有的常用操作,一下列举几个作为参考
//        UserTestTemplate ut = new UserTestTemplate();
//        ut.fastEqualToUserTest(userTest);//对象中所有不为空参数都作为AND条件
//        ut.fastAge().lessOrEqual(20); //指定字段小于等于查询
//        ut.fastName().equal("张亚伟");//指定字段AND条件
//        ut.fastAge().orderByAsc();//指定字段排序
//        PageInfo<UserTest> userPageInfo = ut.dbTemplate().findPage(1, 10);
//        //System.out.println(JSONUtil.toJsonStr(userPageInfo));
//    }
//
//    @Test
//    public void test8() {
//        create();
//        UserTest user = new UserTest();
//        user.setName("张亚伟-1");
//        user.setAge(-1);
//        user.setId("85f1ba1da50740afb8274084e33a0cb4");
//        //System.out.println(UserTestTemplate.fastFunction().updateByPrimaryKeySelective(user));
//    }
//
//    @Test
//    public void test9() {
//        create();
//        UserTest user = new UserTest();
//        user.setAge(-2);
//        user.setName("张亚伟-2");
//
//        UserTestTemplate userTemplate = new UserTestTemplate();
//        userTemplate.fastName().equal("张亚伟2");
//
//        //System.out.println(userTemplate.dbTemplate().updateSelective(user));
//    }
//
//    @Test
//    public void test10() {
//        create();
//        UserTest user = new UserTest();
//        user.setAge(-22);
//        user.setName("张亚伟in");
//        user.setDeleted(false);
//        UserTestTemplate UserTestTemplate = new UserTestTemplate();
//        UserTestTemplate.fastName().in("张亚伟-2");
//
//        //System.out.println(UserTestTemplate.dbTemplate().update(user));
//    }
//
//    @Test
//    public void test11() {
//        create();
//        UserTestTemplate userTemplate = new UserTestTemplate();
//        userTemplate.fastId().equal("0041b89142fd459b88392414724ddbcc");
//        //System.out.println(userTemplate.dbTemplate().deleteDisk());
//    }
//
//    @Test
//    public void test12() {
//        create();
//        //System.out.println(UserTestTemplate.fastFunction().deleteByPrimaryKey("c6a6861527ac4fe7a2de29831fa5a81b"));
//    }
//
//    @Test
//    public void test13() {
//        create();
//        UserTestTemplate UserTestTemplate = new UserTestTemplate();
//        UserTestTemplate.fastName().like("张亚伟%");
//        //System.out.println(UserTestTemplate.dbTemplate().deleteDisk());
//    }
//
//    @Test
//    public void test14() {
//        create();
//        FastExample fastExample = new FastExample();
//        List<UserTest> age = UserTestTemplate.fastFunction().findByIn("age", 1, 2, null);
//
//        //System.out.println(JSONUtil.toJsonStr(age));
//    }
//
//    @Test
//    public void test15() {
//        create();
//        List<UserTest> user = UserTestTemplate.fastFunction().findByIn("id", "579427c5959a4e73841f437d1897abb6", "58092420f83d4fbe85481f8d9c9382ce");
//        //System.out.println(user);
//    }
//
//    @Test
//    public void test16() {
//        create();
//
//        UserTest userTest = new UserTest();
//        userTest.setName("张亚伟1");
//
//        UserTestTemplate userTemplate = new UserTestTemplate();
//        userTemplate.fastEqualToUserTest(userTest);
//        userTemplate.fastName().or().equal("张亚伟2");
//        userTemplate.fastName().orderByAsc();
//        userTemplate.fastName().showField();
//        userTemplate.fastAge().showField();
//        List<UserTest> all = userTemplate.dbTemplate().findAll();
//        //System.out.println(JSONUtil.toJsonStr(all));
//    }
//
//    @Test
//    public void test17() {
//        create();
//        UserTest user = new UserTest();
//        user.setId("30433cbbdf594aeb86bcbc350b140c17");
//        user.setName("张亚伟-1");
//        user.setAge(-1);
//        UserTest b = UserTestTemplate.fastFunction().findByPrimaryKey("1206947dc88045059cb0e6f4fe3c60a5");
//        //System.out.println(b);
//    }
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
//        dataSource.setUrl("jdbc:mysql://127.0.0.1:3306/test?useUnicode=true&characterEncoding=utf-8&useSSL=false&serverTimezone=UTC");
//        dataSource.setUsername("root");
//        dataSource.setPassword("kaixinzyw");
//        FastDBConnection.createJdbcTemplate(dataSource);
//
//
//        JedisConnectionFactory factory = new JedisConnectionFactory();
//        factory.setHostName("127.0.0.1");
//        factory.setPort(6379);
//
////        FastDBConnection.openCache(10L, TimeUnit.SECONDS, factory);
//        FastDBConnection.openCache(10L, TimeUnit.SECONDS);
//    }
//
//}

//import com.alibaba.druid.pool.DruidDataSource;
//import com.alibaba.fastjson.JSONObject;
//import com.db.test.pojo.UserTest;
//import com.db.test.pojo.UserTest2;
//import com.db.test.pojo.template.UserTest2Template;
//import com.db.test.pojo.template.UserTestTemplate;
//import com.fast.db.template.dao.jdbc.SpringJDBCMySqlImpl;
//import com.fast.db.template.utils.page.PageInfo;
//import org.junit.FixMethodOrder;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.junit.runners.MethodSorters;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.junit4.SpringRunner;
//
//import javax.sql.DataSource;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//@RunWith(SpringRunner.class)
//@SpringBootTest(classes = Application.class)
//@FixMethodOrder(value = MethodSorters.NAME_ASCENDING)
//public class SqlTest {
//
//    private static Long id1;
//    private static Long id2;
//    private static Long id3;
//    private static Long id4;
//    private static Long id5;
//    private static Long id6;
//    private static Long id19;
//    private static Long id20;
//
//    public DataSource zywTest2() {
//        DruidDataSource dataSource = new DruidDataSource();
//        dataSource.setUrl("jdbc:mysql://127.0.0.1:3306/zyw_test2?useUnicode=true&characterEncoding=utf-8&useSSL=false&serverTimezone=UTC");
//        dataSource.setUsername("root");
//        dataSource.setPassword("kaixin001");
//        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
//        return dataSource;
//
//    }
//
//    //    @Test
//    public void test_aa_insert() throws InterruptedException {
//
//        for (int i = 1; i <= 100; i++) {
//
//            new Thread(() -> {
//                UserTest user = new UserTest();
//                user.setName("张亚伟Thread");
//                user.setAge(-1);
//                UserTestTemplate.create().dao().insert(user);
//
//                SpringJDBCMySqlImpl.dataSource(zywTest2());
//
//                UserTest2 user2 = new UserTest2();
//                user2.setName("UserTest2张亚伟Thread");
//                user2.setAge(-1);
//                UserTest2Template.create().dao().insert(user2);
//            }).start();
//        }
//        Thread.sleep(10000);
//    }
//
//    @Test
//    public void test_a_insert() {
//
//        int x = 0;
//        for (int i = 1; i <= 20; i++) {
//            UserTest user = new UserTest();
//            user.setName("张亚伟" + i);
//            user.setAge(i);
//            UserTestTemplate.create().dao().insert(user);
//
//
//            UserTest2 user2 = new UserTest2();
//            user2.setName("UserTest2张亚伟" + i);
//            user2.setAge(i);
//            Boolean success = UserTest2Template.create().dao().insert(user2);
//            if (i == 1) {
//                id1 = user.getId();
//            }
//            if (i == 2) {
//                id2 = user.getId();
//            }
//            if (i == 3) {
//                id3 = user.getId();
//            }
//            if (i == 4) {
//                id4 = user.getId();
//            }
//            if (i == 5) {
//                id5 = user.getId();
//            }
//            if (i == 6) {
//                id6 = user.getId();
//            }
//
//            if (i == 19) {
//                id19 = user.getId();
//            }
//            if (i == 20) {
//                id20 = user.getId();
//            }
//            x++;
//        }
//
//    }
//
//    @Test
//    public void test_b_findByPrimaryKey() {
//        UserTest one = UserTestTemplate.create().id(id1).dao().findOne();
//
//    }
//
//
//    @Test
//    public void test_c_findSQL() {
//        for (int x = 0; x < 200; x++) {
//            Map<String, Object> params = new HashMap<>();
//            params.put("userName", "张亚伟19");
//            params.put("age", 20);
//
//            UserTestTemplate testTemplate = UserTestTemplate.create();
//            testTemplate.andSql("name = #{userName}", params);
//            testTemplate.orSQL("age = #{age}", params);
//            testTemplate.age().orderByDesc();
//            testTemplate.closeLogicDeleteProtect();
//            UserTest one = testTemplate.dao().findOne();
//        }
//    }
//
//    @Test
//    public void test_c_findOne() throws InterruptedException {
//        UserTest userTest = new UserTest();
//        userTest.setName("张亚伟10");
//        UserTestTemplate testTemplate = UserTestTemplate.create();
//        testTemplate.equalPojo(userTest);
//        testTemplate.age().orderByAsc();
//
//        UserTest2 userTest2 = new UserTest2();
//        userTest2.setName("UserTest2张亚伟10");
//        UserTest2Template testTemplate2 = UserTest2Template.create();
//        testTemplate2.equalPojo(userTest2);
//        testTemplate2.age().orderByAsc();
//
//
//        for (int i = 0; i < 200; i++) {
//            new Thread(() -> {
//                UserTest one = testTemplate.dao().findOne();
//                if (!one.getName().equals("张亚伟10")) {
//                    throw new RuntimeException("查询错误");
//                }
//                UserTest2 one2 = testTemplate2.dao().findOne();
//                if (!one2.getName().equals("UserTest2张亚伟10")) {
//                    throw new RuntimeException("查询错误");
//                }
//            }).start();
//        }
//        Thread.sleep(1000);
//
//
//    }
//
//    @Test
//    public void test_d_findByIn() {
//
//        List<UserTest> byIn = UserTestTemplate.create().name().in("张亚伟2", "张亚伟3", "张亚伟4").dao().findAll();
//
//
//    }
//
//
//    @Test
//    public void test_e_updateByPrimaryKeyOverwrite() {
//
//        UserTest userTest = new UserTest();
//        userTest.setId(id1);
//        userTest.setName("updateByPrimaryKeyOverwrite");
//        Integer updateCount = UserTestTemplate.create().id(userTest.getId()).dao().updateOverwrite(userTest);
//
//    }
//
//    @Test
//    public void test_f_updateByPrimaryKey() {
//
//        UserTest userTest = new UserTest();
//        userTest.setId(id2);
//        userTest.setName("updateByPrimaryKey");
//        Integer updateCount = UserTestTemplate.create().id(userTest.getId()).dao().update(userTest);
//
//
//    }
//
//
//    @Test
//    public void test_g_updateOverwrite() {
//
//        UserTest userTest = new UserTest();
//        userTest.setName("updateOverwrite");
//        userTest.setId(id3);
//        UserTestTemplate template = UserTestTemplate.create();
//        template.id().valEqual(id3);
//        Integer update = template.dao().updateOverwrite(userTest);
//
//
//    }
//
//
//    @Test
//    public void test_h_update() {
//
//        UserTest userTest = new UserTest();
//        userTest.setName("update");
//        UserTestTemplate template = UserTestTemplate.create();
//        template.id().valEqual(id4);
//        Integer update = template.dao().update(userTest);
//
//
//    }
//
//    @Test
//    public void test_i_deleteByPrimaryKey() {
//
//        Integer delCount = UserTestTemplate.create().id(id5).dao().delete();
//
//
//    }
//
//    @Test
//    public void test_g_deleteByPrimaryKeyDisk() {
//
//        UserTestTemplate.create().id(id6).dao().deleteDisk();
//
//
//    }
//
//    @Test
//    public void test_k_delete() {
//
//        UserTestTemplate template = UserTestTemplate.create();
//        template.name().valEqual("张亚伟7");
//        Integer delete = template.dao().delete();
//
//
//    }
//
//    @Test
//    public void test_l_deleteDisk() {
//
//        UserTestTemplate template = UserTestTemplate.create();
//        template.name().like("%张亚伟8%");
//        Integer delete = template.dao().deleteDisk();
//
//
//    }
//
//    @Test
//    public void test_m_findAll() {
//
//        List<UserTest> all = UserTestTemplate.create().dao().findAll();
//
//
//    }
//
//
//    @Test
//    public void test_n_findPage() {
//
//
//        UserTestTemplate userTestTemplate = UserTestTemplate.create();
//        userTestTemplate.name().like("%张亚伟%");
//        userTestTemplate.age().lessOrEqual(100);
//        userTestTemplate.age().orderByAsc();
//        PageInfo<UserTest> page = userTestTemplate.dao().findPage(1, 2);
////        PageInfo<UserTest> page = UserTestTemplate.UnconditionalOperating.findPage(1, 2);
//        System.out.println("findPage执行成功,查询到的数据为" + JSONObject.toJSONString(page, true));
//
//    }
//
//
//    @Test
//    public void test_o_FieldOperating() {
//        UserTest up = UserTestTemplate.create().id(id19).dao().findOne();
//        up.setName("FieldOperatingUpDate");
//        UserTestTemplate.create().age(up.getAge()).dao().updateOverwrite(up);
//
//        UserTest del = UserTestTemplate.create().id(id20).dao().findOne();
//        UserTestTemplate.create().age().valEqual(del.getAge()).dao().findAll();
//        UserTestTemplate.create().name().like("%" + del.getName() + "%").hideField().dao().findAll();
//
//
//        UserTestTemplate.create().name().like("%" + del.getName() + "%").showField().dao().findAll();
//
//        UserTestTemplate.create().id(del.getId()).dao().delete();
//
//        UserTestTemplate.create().name().like("%" + del.getName() + "%").hideField().dao().findAll();
//
//        UserTestTemplate template = UserTestTemplate.create();
//        template.closeLogicDeleteProtect();
//        template.name().like("%" + del.getName() + "%").hideField().dao().findAll();
//
//        UserTestTemplate.create().name().like("%" + del.getName() + "%").showField().dao().deleteDisk();
//
//
//    }
//
//
//}

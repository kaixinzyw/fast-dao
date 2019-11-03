//package com.my.test;
//
//import com.alibaba.fastjson.JSONObject;
//import com.fast.utils.page.PageInfo;
//import com.my.test.pojo.User;
//import com.my.test.pojo.fast.dao.UserFastDao;
//import org.junit.FixMethodOrder;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.junit.runners.MethodSorters;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.junit4.SpringRunner;
//
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//@RunWith(SpringRunner.class)
//@SpringBootTest(classes = Application.class)
//@FixMethodOrder(value = MethodSorters.NAME_ASCENDING)
//public class SqlTest {
//
//
//
//    @Test
//    public void test_a_insert() {
//        for (int i = 1; i <= 20; i++) {
//            User user = new User();
//            user.setUserName("张亚伟" + i);
//            user.setAge(i);
//            UserFastDao.create().dao().insert(user);
//        }
//
//    }
//
//    @Test
//    public void test_b_findByPrimaryKey() {
//        User one = UserFastDao.create().age().orderByAsc().dao().findOne();
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
//            UserFastDao testTemplate = UserFastDao.create();
//            testTemplate.andSql("userName = #{userName}", params);
//            testTemplate.orSQL("age = #{age}", params);
//            testTemplate.age().orderByDesc();
//            testTemplate.closeLogicDeleteProtect();
//            User one = testTemplate.dao().findOne();
//        }
//    }
//
//    @Test
//    public void test_c_findOne() throws InterruptedException {
//        User User = new User();
//        User.setUserName("张亚伟10");
//        UserFastDao testTemplate = UserFastDao.create();
//        testTemplate.equalPojo(User);
//        testTemplate.age().orderByAsc();
//
//
//
//        for (int i = 0; i < 100; i++) {
////            new Thread(() -> {
//            User one = testTemplate.age(1).dao().findOne();
//            if (!one.getUserName().equals("张亚伟10")) {
//                throw new RuntimeException("查询错误");
//            }
//
//            if (i == 50) {
//                UserFastDao.create().id(one.getId()).dao().update(one);
//            }
////            }).start();
//        }
////        Thread.sleep(1000);
//
//
//    }
//
//    @Test
//    public void test_d_findByIn() {
//        List<User> byIn = UserFastDao.create().userName().in("张亚伟11", "张亚伟12", "张亚伟13").dao().findAll();
//    }
//
//
//    @Test
//    public void test_e_updateByPrimaryKeyOverwrite() {
//        User one = UserFastDao.create().age(1).dao().findOne();
//        User u = new User();
//        u.setId(one.getId());
//        u.setUserName("updateByPrimaryKeyOverwrite");
//        Integer updateCount = UserFastDao.create().id(u.getId()).dao().updateOverwrite(u);
//
//    }
//
//    @Test
//    public void test_f_updateByPrimaryKey() {
//        User one = UserFastDao.create().age(2).dao().findOne();
//        User u = new User();
//        u.setId(one.getId());
//        u.setUserName("updateByPrimaryKey");
//        Integer updateCount = UserFastDao.create().id(u.getId()).dao().update(u);
//
//
//    }
//
//
//    @Test
//    public void test_g_updateOverwrite() {
//        User one = UserFastDao.create().age(3).dao().findOne();
//
//        User u = new User();
//        u.setUserName("updateOverwrite");
//        u.setId(one.getId());
//
//        Integer update = UserFastDao.create().id(one.getId()).dao().updateOverwrite(u);
//    }
//
//
//    @Test
//    public void test_h_update() {
//        User one = UserFastDao.create().age(4).dao().findOne();
//
//        User u = new User();
//        u.setUserName("update");
//
//        Integer update = UserFastDao.create().id(one.getId()).dao().update(u);
//
//
//    }
//
//    @Test
//    public void test_i_deleteByPrimaryKey() {
//
//        Integer delCount = UserFastDao.create().age(5).dao().delete();
//
//
//    }
//
//    @Test
//    public void test_g_deleteByPrimaryKeyDisk() {
//
//        UserFastDao.create().age(6).dao().deleteDisk();
//
//
//    }
//
//    @Test
//    public void test_k_delete() {
//
//        Integer delete = UserFastDao.create().userName("张亚伟7").dao().delete();
//
//
//    }
//
//    @Test
//    public void test_l_deleteDisk() {
//        Integer delete =  UserFastDao.create().userName("张亚伟8").dao().deleteDisk();
//    }
//
//    @Test
//    public void test_m_findAll() {
//
//        List<User> all = UserFastDao.create().dao().findAll();
//
//
//    }
//
//
//    @Test
//    public void test_n_findPage() {
//
//        UserFastDao fastDao = UserFastDao.create();
//        fastDao.userName().like("%张亚伟%");
//        fastDao.age().lessOrEqual(100);
//        fastDao.age().orderByDesc();
//        PageInfo<User> page = fastDao.dao().findPage(1, 2);
////        PageInfo<User> page = fastDao.UnconditionalOperating.findPage(1, 2);
//        System.out.println("findPage执行成功,查询到的数据为" + JSONObject.toJSONString(page, true));
//
//    }
//
//
//    @Test
//    public void test_o_FieldOperating() {
//        User up = UserFastDao.create().age(19).dao().findOne();
//        up.setUserName("FieldUpDate");
//        UserFastDao.create().age(up.getAge()).dao().update(up);
//
//        User del = UserFastDao.create().age(20).dao().findOne();
//        UserFastDao.create().age().valEqual(del.getAge()).dao().findAll();
//        UserFastDao.create().userName().like("%" + del.getUserName() + "%").hideField().dao().findAll();
//
//
//        UserFastDao.create().userName().like("%" + del.getUserName() + "%").showField().dao().findAll();
//
//        UserFastDao.create().id(del.getId()).dao().delete();
//
//        UserFastDao.create().userName().like("%" + del.getUserName() + "%").hideField().dao().findAll();
//
//        UserFastDao template = UserFastDao.create();
//        template.closeLogicDeleteProtect();
//        template.userName().like("%" + del.getUserName() + "%").hideField().dao().findAll();
//
//        UserFastDao.create().userName().like("%" + del.getUserName() + "%").showField().dao().deleteDisk();
//
//
//    }
//
//
//}

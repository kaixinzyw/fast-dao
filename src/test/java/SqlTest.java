//package com.db.test;
//
//import com.alibaba.fastjson.JSONObject;
//import com.db.test.dao.UserFastDao;
//import com.db.test.dto.UserDto;
//import com.db.test.pojo.User;
//import com.fast.config.FastDaoConfig;
//import com.fast.fast.FastCustomSqlDao;
//import com.fast.utils.BeanCopyUtil;
//import com.fast.utils.page.PageInfo;
//import org.junit.FixMethodOrder;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.junit.runners.MethodSorters;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.junit4.SpringRunner;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//@RunWith(SpringRunner.class)
//@SpringBootTest(classes = Application.class)
//@FixMethodOrder(value = MethodSorters.NAME_ASCENDING)
//public class SqlTest {
//
//    static {
//        FastSetConfig.fastDaoConfig();
//    }
//
//    @Test
//    public void test_a_insertList() {
//        List<User> userList = new ArrayList<>();
//        for (int i = 100; i <= 200; i++) {
//            User user = new User();
//            user.setUserName("张亚伟" + i);
//            user.setAge(i);
//            userList.add(user);
//        }
//        List<User> ls = UserFastDao.create().dao().insertList(userList);
//        System.out.println(JSONObject.toJSONString(ls,true));
//    }
//
//    @Test
//    public void test_a_insert() {
//        for (int i = 1; i <= 20; i++) {
//            User user = new User();
//            user.setUserName("张亚伟" + i);
//            user.setAge(i);
//            User insert = UserFastDao.create().dao().insert(user);
//            System.out.println(JSONObject.toJSONString(insert,true));
//        }
//    }
//
//    @Test
//    public void test_b_findByAge() {
//        UserFastDao.create().age().distinctField().orderByAsc().dao().findAll();
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
//            testTemplate.andSql("user_name = :userName ", params);
//            testTemplate.orSQL("age = :age ", params);
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
//        testTemplate.equalObject(User);
//        testTemplate.age().orderByAsc();
//        for (int i = 0; i < 100; i++) {
////            new Thread(() -> {
//            User one = testTemplate.dao().findOne();
//            if (!one.getUserName().equals("张亚伟10")) {
//                throw new RuntimeException("查询错误");
//            }
//            if (i == 50) {
//                UserFastDao.create().id(one.getId()).dao().update(one);
//            }
////            }).start();
//        }
////        Thread.sleep(1000);
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
//    public void test_e_updateByAgeOverwrite() {
//        User one = UserFastDao.create().age(1).dao().findOne();
//        User u = new User();
//        u.setId(one.getId());
//        u.setUserName("updateByAgeOverwrite");
//        Integer updateCount = UserFastDao.create().id(u.getId()).dao().updateOverwrite(u);
//
//
//    }
//
//    @Test
//    public void test_f_updateByAge() {
//        User one = UserFastDao.create().age(2).dao().findOne();
//        User u = new User();
//        u.setId(one.getId());
//        u.setUserName("updateByAge");
//        Integer updateCount = UserFastDao.create().id(u.getId()).dao().update(u);
//    }
//
//
//    @Test
//    public void test_g_updateOverwrite() {
//        User one = UserFastDao.create().age(3).dao().findOne();
//        User u = new User();
//        u.setUserName("updateOverwrite");
//        u.setId(one.getId());
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
//    }
//
//    @Test
//    public void test_i_deleteByAge() {
//        Integer delCount = UserFastDao.create().age(5).dao().delete();
//    }
//
//    @Test
//    public void test_g_deleteByAgeDisk() {
//        UserFastDao.create().age(6).dao().deleteDisk();
//    }
//
//    @Test
//    public void test_k_delete() {
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
//        List<User> all = UserFastDao.create().dao().findAll();
//    }
//
//
//    @Test
//    public void test_n_findPage() {
//        UserFastDao fastDao = UserFastDao.create();
//        fastDao.userName().likeRight("张").distinctField();
//        fastDao.age().lessOrEqual(100);
//        fastDao.age().orderByDesc().distinctField();
//        PageInfo<User> page = fastDao.dao().findPage(1, 2);
////        PageInfo<User> page = fastDao.UnconditionalOperating.findPage(1, 2);
//        System.out.println("findPage执行成功,查询到的数据为" + JSONObject.toJSONString(page, true));
//        PageInfo<UserDto> ds = BeanCopyUtil.cope(page, UserDto.class);
//
//    }
//
//
//    @Test
//    public void test_o_FieldOperating() {
//        User up = UserFastDao.create().age(18).dao().findOne();
//        up.setUserName("FieldUpDate");
//        up.setId(null);
//        UserFastDao.create().age(up.getAge()).dao().update(up);
//
//        User del = UserFastDao.create().age(19).dao().findOne();
//        UserFastDao.create().age().valEqual(del.getAge()).dao().findAll();
//        UserFastDao.create().age().isNull().dao().findAll();
//        UserFastDao.create().age().notNull().dao().findAll();
//        UserFastDao.create().userName().like(del.getUserName()).hideField().dao().findAll();
//        UserFastDao.create().userName().like(del.getUserName()).showField().dao().findAll();
//        UserFastDao.create().userName().like(del.getUserName()).hideField().dao().findAll();
//
//        UserFastDao template = UserFastDao.create();
//        template.closeLogicDeleteProtect();
//        template.userName().like(del.getUserName()).hideField().dao().findAll();
//
//        UserFastDao.create().userName().likeRight("15").showField().dao().deleteDisk();
//        UserFastDao.create().age(20).dao().delete();
//        UserFastDao.create().age(20).dao().findAll();
//
//        List<User> us = UserFastDao.create().userName().notLike("张").dao().findAll();
//        PageInfo<User> 张 = UserFastDao.create().userName().notLike("张").dao().findPage(1, 10);
//
//    }
//
//
//    @Test
//    public void test_p_CustomSql(){
//        String sql = "SELECT * FROM user WHERE `user_name` LIKE :userName ";
//        HashMap<String, Object> params = new HashMap<>();
//        params.put("userName","%张亚伟%");
//        List<User> all = FastCustomSqlDao.create(User.class, sql, params).findAll();
//    }
//
//}

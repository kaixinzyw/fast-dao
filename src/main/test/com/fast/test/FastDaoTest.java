package com.fast.test;

import com.alibaba.fastjson.JSONObject;
import com.fast.fast.FastDao;
import com.fast.generator.GenTable;
import com.fast.generator.GenUtils;
import com.fast.test.pojo.User;
import com.fast.test.pojo.fast.UserFastDAO;
import com.fast.fast.FastCustomSqlDao;
import com.fast.utils.page.PageInfo;

import java.util.*;

/**
 * FastDao测试
 */
public class FastDaoTest {

    static {
        //加载配置文件
        FastSetConfigTest.fastDaoConfig();
    }

    public static void main(String[] args) {
        test_a_insert();
        test_a_insertList();
        test_b_findByAge();
        test_c_findSQL();
        test_c_findOne();
        test_d_findByIn();
        test_e_updateByAgeOverwrite();
        test_f_updateByAge();
        test_g_updateOverwrite();
        test_h_update();
        test_i_deleteByAge();
        test_g_deleteByAgeDisk();
        test_k_delete();
        test_l_deleteDisk();
        test_m_findAll();
        test_n_findPage();
        test_o_FieldOperating();
        test_p_CustomSql();
        customUpdateColumns();
        selectObject();
        getTableInfo();
        switchTest();
    }


    public static void switchTest(){

        UserFastDAO fastDAO1 = UserFastDAO.create();
        fastDAO1.id(320L);
        FastDao<User> dao = fastDAO1.dao();
        dao.findPage(1,1);
        dao.findAll();
        dao.findOne();
        dao.findCount();


    }

    public static void getTableInfo(){
        GenTable genTable = GenUtils.getGenTable("user");
        System.out.println(JSONObject.toJSONString(genTable,true));
    }





        public static void selectObject(){
        FastUserTestQuery userTest = new FastUserTestQuery();
        userTest.setId(10L);
        userTest.setCreateTime(new Date());

        Map<String,Object> data = new HashMap<>();
        data.put("id", 10);
        data.put("createTime", new Date());
        UserFastDAO userFastDAO = UserFastDAO.create(userTest);
        userFastDAO.orLeftBracket();
        userFastDAO.age().or().less(10);
        userFastDAO.orLeftBracket();
        userFastDAO.userName().like("A");
        userFastDAO.createTime().greater(new Date());
        userFastDAO.rightBracket();
        userFastDAO.andLeftBracket();
        userFastDAO.userName().like("B");
        userFastDAO.createTime().greater(new Date());
        userFastDAO.rightBracket();
        userFastDAO.rightBracket();
        userFastDAO.age().greater(10);
        userFastDAO.userName().or().valEqual("C;delete from fast_user_test;");
        userFastDAO.dao().findAll();
//        fastUserTestFastDAO.dao().findPage(1,10);
    }

    public static void customUpdateColumns() {
//        FastUserTestFastDao.create().age().customizeUpdateValue().thisAdd("#{age}",Collections.singletonMap("age",5)).greaterOrEqual(10).dao().update(null);
//        FastUserTestFastDao.create().age().customizeUpdateValue().thisSbu("#{age}", Collections.singletonMap("age",5)).greaterOrEqual(10).dao().update(null);
//        FastUserTestFastDao.create().age().customizeUpdateValue().thisMul("#{age}", Collections.singletonMap("age",10)).greaterOrEqual(10).dao().update(null);
//        FastUserTestFastDao.create().age().customizeUpdateValue().thisDiv("#{age}", Collections.singletonMap("age",10)).greaterOrEqual(10).dao().update(null);
//        FastUserTestFastDao.create().age().customizeUpdateValue().thisModulo("#{age}", Collections.singletonMap("age",2)).greaterOrEqual(10).dao().update(null);
//        FastUserTestFastDao.create().age().customizeUpdateValue().customize("age + 1", null).dao().update(null);
        UserFastDAO.create().age().customizeUpdateValue().customize("age - #{age}",Collections.singletonMap("age",0)).notNull().dao().update(null);
    }

    public static void test_a_insertList() {
        List<User> userList = new ArrayList<>();
        for (int i = 100; i <= 102; i++) {
            User user = new User();
            user.setUserName("FastDao" + i);
            user.setAge(i);
            userList.add(user);
        }
        List<User> ls = UserFastDAO.create().dao().insertList(userList);
        System.out.println(JSONObject.toJSONString(ls, true));
    }

    public static void test_a_insert() {
        for (int i = 1; i <= 20; i++) {
            User user = new User();
            user.setUserName("FastDao" + i);
            user.setAge(i);
            User insert = UserFastDAO.create().dao().insert(user);
        }
    }

    public static void test_b_findByAge() {
        UserFastDAO.create().age(1).age().distinctField().age().orderByAsc().dao().findAll();
    }


    public static void test_c_findSQL() {
        for (int x = 0; x < 2; x++) {
            Map<String, Object> params = new HashMap<>();
            params.put("userName", "FastDao19");
            params.put("age", 20);

            UserFastDAO testTemplate = UserFastDAO.create();
            testTemplate.andSql("user_name = #{userName}", params);
            testTemplate.orSQL("age = #{age}", params);
            testTemplate.age().orderByDesc();
            testTemplate.closeLogicDeleteProtect();
            User one = testTemplate.dao().findOne();
        }
    }


    public static void test_c_findOne() {
        User User = new User();
        User.setUserName("FastDao10");
        UserFastDAO testTemplate = UserFastDAO.create();
        testTemplate.equalObject(User);
        testTemplate.age().orderByAsc();
        for (int i = 0; i < 10; i++) {
//            new Thread(() -> {
            User one = testTemplate.dao().findOne();
            if (!one.getUserName().equals("FastDao10")) {
                throw new RuntimeException("查询错误");
            }
            if (i == 50) {
                UserFastDAO.create().id(one.getId()).dao().update(one);
            }
//            }).start();
        }
//        Thread.sleep(1000);

    }


    public static void test_d_findByIn() {
        List<User> byIn = UserFastDAO.create().userName().in("FastDao11", "FastDao12", "FastDao13").dao().findAll();
    }


    public static void test_e_updateByAgeOverwrite() {
        User one = UserFastDAO.create().age(1).dao().findOne();
        User u = new User();
        u.setId(one.getId());
        u.setUserName("updateByAgeOverwrite");
        Integer updateCount = UserFastDAO.create().id(u.getId()).dao().updateOverwrite(u);


    }


    public static void test_f_updateByAge() {
        User one = UserFastDAO.create().age(2).dao().findOne();
        User u = new User();
        u.setId(one.getId());
        u.setUserName("updateByAge");
        Integer updateCount = UserFastDAO.create().id(u.getId()).dao().update(u);
    }


    public static void test_g_updateOverwrite() {
        User one = UserFastDAO.create().age(3).dao().findOne();
        User u = new User();
        u.setUserName("updateOverwrite");
        u.setId(one.getId());
        Integer update = UserFastDAO.create().id(one.getId()).dao().updateOverwrite(u);
    }


    public static void test_h_update() {
        User one = UserFastDAO.create().age(4).dao().findOne();

        User u = new User();
        u.setUserName("update");

        Integer update = UserFastDAO.create().id(one.getId()).dao().update(u);
    }


    public static void test_i_deleteByAge() {
        Integer delCount = UserFastDAO.create().age(5).dao().delete();
    }


    public static void test_g_deleteByAgeDisk() {
        UserFastDAO userFastDao = UserFastDAO.create();
        userFastDao.closeLogicDeleteProtect();
        userFastDao.age(6).dao().delete();
    }


    public static void test_k_delete() {
        Integer delete = UserFastDAO.create().userName("FastDao7").dao().delete();


    }


    public static void test_l_deleteDisk() {
        UserFastDAO userFastDao = UserFastDAO.create();
        userFastDao.closeLogicDeleteProtect();
        Integer delete = userFastDao.userName("FastDao8").dao().delete();
    }


    public static void test_m_findAll() {
        List<User> all = UserFastDAO.create().dao().findAll();
    }


    public static void test_n_findPage() {
        UserFastDAO query = UserFastDAO.create();
        query.userName().likeRight("fast");
        query.age().less(30);
        query.createTime().orderByDesc();
        query.age().sumField();
        query.createTime().sumField();
        PageInfo<User> page = query.dao().findPage(1, 10);
//        Integer count = query.dao().findCount();
//        FastUserTest one = query.dao().findOne();
//        List<FastUserTest> userList = query.dao().findAll();
//        System.out.println("findPage执行成功,查询到的数据为" + JSONObject.toJSONString(page, true));

    }


    public static void test_o_FieldOperating() {
        User up = UserFastDAO.create().age(18).dao().findOne();
        up.setUserName("FieldUpDate");
        up.setId(null);
        UserFastDAO.create().age(up.getAge()).dao().update(up);

        User del = UserFastDAO.create().age(19).dao().findOne();
        UserFastDAO.create().age().valEqual(del.getAge()).dao().findAll();
        UserFastDAO.create().age().isNull().dao().findAll();
        UserFastDAO.create().age().notNull().dao().findAll();
        UserFastDAO.create().userName().like(del.getUserName()).userName().hideField().dao().findAll();
        UserFastDAO.create().userName().like(del.getUserName()).userName().showField().dao().findAll();
        UserFastDAO.create().userName().like(del.getUserName()).userName().hideField().dao().findAll();

        UserFastDAO template = UserFastDAO.create();
        template.closeLogicDeleteProtect();
        template.userName().like(del.getUserName()).userName().hideField().dao().findAll();

        UserFastDAO.create().userName().likeRight("15").userName().showField().dao().delete();
        UserFastDAO.create().age(20).dao().delete();
        UserFastDAO.create().age(20).dao().findAll();
        UserFastDAO.create().age().between(1, 10).dao().findPage(1, 10);

        List<User> us = UserFastDAO.create().userName().notLike("Fast").dao().findAll();
        PageInfo<User> Fast = UserFastDAO.create().userName().notLike("Fast").dao().findPage(1, 10);

    }


    public static void test_p_CustomSql() {
        String sql = "SELECT * FROM user WHERE `user_name` LIKE #{userName}";
        HashMap<String, Object> params = new HashMap<>();
        params.put("userName", "%FastDao%");
        PageInfo<User> page = FastCustomSqlDao.create(User.class, sql, params).findPage(1, 10);
    }



}

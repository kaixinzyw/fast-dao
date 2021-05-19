package com.fast.demo;

import com.alibaba.fastjson.JSONObject;
import com.fast.demo.pojo.FastUserTest;
import com.fast.demo.pojo.fast.FastUserTestFastDAO;
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
//        test_a_insert();
//        test_a_insertList();
//        test_b_findByAge();
//        test_c_findSQL();
//        test_c_findOne();
//        test_d_findByIn();
//        test_e_updateByAgeOverwrite();
//        test_f_updateByAge();
//        test_g_updateOverwrite();
//        test_h_update();
//        test_i_deleteByAge();
//        test_g_deleteByAgeDisk();
//        test_k_delete();
//        test_l_deleteDisk();
//        test_m_findAll();
//        test_n_findPage();
//        test_o_FieldOperating();
        test_p_CustomSql();
        customUpdateColumns();
        selectObject();
    }


    public static void selectObject(){
        FastUserTestQuery userTest = new FastUserTestQuery();
        userTest.setUserName("FastDao10");
        userTest.setAgee(10);

        Map<String,Object> data = new HashMap<>();
        data.put("age", 10);
        data.put("userName","FastDao10");
        FastUserTestFastDAO fastUserTestFastDAO = FastUserTestFastDAO.create(userTest);
        fastUserTestFastDAO.dao().findPage(1,10);
    }

    public static void customUpdateColumns() {
//        FastUserTestFastDao.create().age().customizeUpdateValue().thisAdd("#{age}",Collections.singletonMap("age",5)).greaterOrEqual(10).dao().update(null);
//        FastUserTestFastDao.create().age().customizeUpdateValue().thisSbu("#{age}", Collections.singletonMap("age",5)).greaterOrEqual(10).dao().update(null);
//        FastUserTestFastDao.create().age().customizeUpdateValue().thisMul("#{age}", Collections.singletonMap("age",10)).greaterOrEqual(10).dao().update(null);
//        FastUserTestFastDao.create().age().customizeUpdateValue().thisDiv("#{age}", Collections.singletonMap("age",10)).greaterOrEqual(10).dao().update(null);
//        FastUserTestFastDao.create().age().customizeUpdateValue().thisModulo("#{age}", Collections.singletonMap("age",2)).greaterOrEqual(10).dao().update(null);
//        FastUserTestFastDao.create().age().customizeUpdateValue().customize("age + 1", null).dao().update(null);
        FastUserTestFastDAO.create().age().customizeUpdateValue().customize("age - #{age}",Collections.singletonMap("age",0)).notNull().dao().update(null);
    }

    public static void test_a_insertList() {
        List<FastUserTest> userList = new ArrayList<>();
        for (int i = 100; i <= 102; i++) {
            FastUserTest user = new FastUserTest();
            user.setUserName("FastDao" + i);
            user.setAge(i);
            userList.add(user);
        }
        List<FastUserTest> ls = FastUserTestFastDAO.create().dao().insertList(userList);
        System.out.println(JSONObject.toJSONString(ls, true));
    }

    public static void test_a_insert() {
        for (int i = 1; i <= 20; i++) {
            FastUserTest user = new FastUserTest();
            user.setUserName("FastDao" + i);
            user.setAge(i);
            FastUserTest insert = FastUserTestFastDAO.create().dao().insert(user);
        }
    }

    public static void test_b_findByAge() {
        FastUserTestFastDAO.create().age().distinctField().orderByAsc().dao().findAll();
    }


    public static void test_c_findSQL() {
        for (int x = 0; x < 2; x++) {
            Map<String, Object> params = new HashMap<>();
            params.put("userName", "FastDao19");
            params.put("age", 20);

            FastUserTestFastDAO testTemplate = FastUserTestFastDAO.create();
            testTemplate.andSql("user_name = #{userName}", params);
            testTemplate.orSQL("age = #{age}", params);
            testTemplate.age().orderByDesc();
            testTemplate.closeLogicDeleteProtect();
            FastUserTest one = testTemplate.dao().findOne();
        }
    }


    public static void test_c_findOne() {
        FastUserTest FastUserTest = new FastUserTest();
        FastUserTest.setUserName("FastDao10");
        FastUserTestFastDAO testTemplate = FastUserTestFastDAO.create();
        testTemplate.equalObject(FastUserTest);
        testTemplate.age().orderByAsc();
        for (int i = 0; i < 10; i++) {
//            new Thread(() -> {
            FastUserTest one = testTemplate.dao().findOne();
            if (!one.getUserName().equals("FastDao10")) {
                throw new RuntimeException("查询错误");
            }
            if (i == 50) {
                FastUserTestFastDAO.create().id(one.getId()).dao().update(one);
            }
//            }).start();
        }
//        Thread.sleep(1000);

    }


    public static void test_d_findByIn() {
        List<FastUserTest> byIn = FastUserTestFastDAO.create().userName().in("FastDao11", "FastDao12", "FastDao13").dao().findAll();
    }


    public static void test_e_updateByAgeOverwrite() {
        FastUserTest one = FastUserTestFastDAO.create().age(1).dao().findOne();
        FastUserTest u = new FastUserTest();
        u.setId(one.getId());
        u.setUserName("updateByAgeOverwrite");
        Integer updateCount = FastUserTestFastDAO.create().id(u.getId()).dao().updateOverwrite(u);


    }


    public static void test_f_updateByAge() {
        FastUserTest one = FastUserTestFastDAO.create().age(2).dao().findOne();
        FastUserTest u = new FastUserTest();
        u.setId(one.getId());
        u.setUserName("updateByAge");
        Integer updateCount = FastUserTestFastDAO.create().id(u.getId()).dao().update(u);
    }


    public static void test_g_updateOverwrite() {
        FastUserTest one = FastUserTestFastDAO.create().age(3).dao().findOne();
        FastUserTest u = new FastUserTest();
        u.setUserName("updateOverwrite");
        u.setId(one.getId());
        Integer update = FastUserTestFastDAO.create().id(one.getId()).dao().updateOverwrite(u);
    }


    public static void test_h_update() {
        FastUserTest one = FastUserTestFastDAO.create().age(4).dao().findOne();

        FastUserTest u = new FastUserTest();
        u.setUserName("update");

        Integer update = FastUserTestFastDAO.create().id(one.getId()).dao().update(u);
    }


    public static void test_i_deleteByAge() {
        Integer delCount = FastUserTestFastDAO.create().age(5).dao().delete();
    }


    public static void test_g_deleteByAgeDisk() {
        FastUserTestFastDAO userFastDao = FastUserTestFastDAO.create();
        userFastDao.closeLogicDeleteProtect();
        userFastDao.age(6).dao().delete();
    }


    public static void test_k_delete() {
        Integer delete = FastUserTestFastDAO.create().userName("FastDao7").dao().delete();


    }


    public static void test_l_deleteDisk() {
        FastUserTestFastDAO userFastDao = FastUserTestFastDAO.create();
        userFastDao.closeLogicDeleteProtect();
        Integer delete = userFastDao.userName("FastDao8").dao().delete();
    }


    public static void test_m_findAll() {
        List<FastUserTest> all = FastUserTestFastDAO.create().dao().findAll();
    }


    public static void test_n_findPage() {
        FastUserTestFastDAO query = new FastUserTestFastDAO();
        query.userName().likeRight("fast");
        query.age().less(30);
        query.createTime().orderByDesc();
        query.age().sumField();
        PageInfo<FastUserTest> page = query.dao().findPage(1, 10);
//        Integer count = query.dao().findCount();
//        FastUserTest one = query.dao().findOne();
//        List<FastUserTest> userList = query.dao().findAll();
//        System.out.println("findPage执行成功,查询到的数据为" + JSONObject.toJSONString(page, true));

    }


    public static void test_o_FieldOperating() {
        FastUserTest up = FastUserTestFastDAO.create().age(18).dao().findOne();
        up.setUserName("FieldUpDate");
        up.setId(null);
        FastUserTestFastDAO.create().age(up.getAge()).dao().update(up);

        FastUserTest del = FastUserTestFastDAO.create().age(19).dao().findOne();
        FastUserTestFastDAO.create().age().valEqual(del.getAge()).dao().findAll();
        FastUserTestFastDAO.create().age().isNull().dao().findAll();
        FastUserTestFastDAO.create().age().notNull().dao().findAll();
        FastUserTestFastDAO.create().userName().like(del.getUserName()).hideField().dao().findAll();
        FastUserTestFastDAO.create().userName().like(del.getUserName()).showField().dao().findAll();
        FastUserTestFastDAO.create().userName().like(del.getUserName()).hideField().dao().findAll();

        FastUserTestFastDAO template = FastUserTestFastDAO.create();
        template.closeLogicDeleteProtect();
        template.userName().like(del.getUserName()).hideField().dao().findAll();

        FastUserTestFastDAO.create().userName().likeRight("15").showField().dao().delete();
        FastUserTestFastDAO.create().age(20).dao().delete();
        FastUserTestFastDAO.create().age(20).dao().findAll();
        FastUserTestFastDAO.create().age().between(1, 10).dao().findPage(1, 10);

        List<FastUserTest> us = FastUserTestFastDAO.create().userName().notLike("Fast").dao().findAll();
        PageInfo<FastUserTest> Fast = FastUserTestFastDAO.create().userName().notLike("Fast").dao().findPage(1, 10);

    }


    public static void test_p_CustomSql() {
        String sql = "SELECT * FROM fast_user_test WHERE `user_name` LIKE #{userName}";
        HashMap<String, Object> params = new HashMap<>();
        params.put("userName", "%FastDao%");
        PageInfo<FastUserTest> page = FastCustomSqlDao.create(FastUserTest.class, sql, params).findPage(1, 10);
    }



}

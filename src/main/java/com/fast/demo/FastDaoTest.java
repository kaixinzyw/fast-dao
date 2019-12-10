package com.fast.demo;
import com.alibaba.fastjson.JSONObject;
import com.fast.demo.pojo.FastUserTest;
import com.fast.demo.pojo.fast.FastUserTestFastDao;
import com.fast.fast.FastCustomSqlDao;
import com.fast.utils.page.PageInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    }

    public static void test_a_insertList() {
        List<FastUserTest> userList = new ArrayList<>();
        for (int i = 100; i <= 200; i++) {
            FastUserTest user = new FastUserTest();
            user.setUserName("FastDao" + i);
            user.setAge(i);
            userList.add(user);
        }
        List<FastUserTest> ls = FastUserTestFastDao.create().dao().insertList(userList);
        System.out.println(JSONObject.toJSONString(ls,true));
    }

    public static void test_a_insert() {
        for (int i = 1; i <= 20; i++) {
            FastUserTest user = new FastUserTest();
            user.setUserName("FastDao" + i);
            user.setAge(i);
            FastUserTest insert = FastUserTestFastDao.create().dao().insert(user);
            System.out.println(JSONObject.toJSONString(insert,true));
        }
    }

    public static void test_b_findByAge() {
        FastUserTestFastDao.create().age().distinctField().orderByAsc().dao().findAll();
    }


    public static void test_c_findSQL() {
        for (int x = 0; x < 200; x++) {
            Map<String, Object> params = new HashMap<>();
            params.put("userName", "FastDao19");
            params.put("age", 20);

            FastUserTestFastDao testTemplate = FastUserTestFastDao.create();
            testTemplate.andSql("user_name = :userName ", params);
            testTemplate.orSQL("age = :age ", params);
            testTemplate.age().orderByDesc();
            testTemplate.closeLogicDeleteProtect();
            FastUserTest one = testTemplate.dao().findOne();
        }
    }


    public static void test_c_findOne() {
        FastUserTest FastUserTest = new FastUserTest();
        FastUserTest.setUserName("FastDao10");
        FastUserTestFastDao testTemplate = FastUserTestFastDao.create();
        testTemplate.equalObject(FastUserTest);
        testTemplate.age().orderByAsc();
        for (int i = 0; i < 100; i++) {
//            new Thread(() -> {
            FastUserTest one = testTemplate.dao().findOne();
            if (!one.getUserName().equals("FastDao10")) {
                throw new RuntimeException("查询错误");
            }
            if (i == 50) {
                FastUserTestFastDao.create().id(one.getId()).dao().update(one);
            }
//            }).start();
        }
//        Thread.sleep(1000);

    }


    public static void test_d_findByIn() {
        List<FastUserTest> byIn = FastUserTestFastDao.create().userName().in("FastDao11", "FastDao12", "FastDao13").dao().findAll();
    }



    public static void test_e_updateByAgeOverwrite() {
        FastUserTest one = FastUserTestFastDao.create().age(1).dao().findOne();
        FastUserTest u = new FastUserTest();
        u.setId(one.getId());
        u.setUserName("updateByAgeOverwrite");
        Integer updateCount = FastUserTestFastDao.create().id(u.getId()).dao().updateOverwrite(u);


    }


    public static void test_f_updateByAge() {
        FastUserTest one = FastUserTestFastDao.create().age(2).dao().findOne();
        FastUserTest u = new FastUserTest();
        u.setId(one.getId());
        u.setUserName("updateByAge");
        Integer updateCount = FastUserTestFastDao.create().id(u.getId()).dao().update(u);
    }



    public static void test_g_updateOverwrite() {
        FastUserTest one = FastUserTestFastDao.create().age(3).dao().findOne();
        FastUserTest u = new FastUserTest();
        u.setUserName("updateOverwrite");
        u.setId(one.getId());
        Integer update = FastUserTestFastDao.create().id(one.getId()).dao().updateOverwrite(u);
    }



    public static void test_h_update() {
        FastUserTest one = FastUserTestFastDao.create().age(4).dao().findOne();

        FastUserTest u = new FastUserTest();
        u.setUserName("update");

        Integer update = FastUserTestFastDao.create().id(one.getId()).dao().update(u);
    }


    public static void test_i_deleteByAge() {
        Integer delCount = FastUserTestFastDao.create().age(5).dao().delete();
    }


    public static void test_g_deleteByAgeDisk() {
        FastUserTestFastDao userFastDao = FastUserTestFastDao.create();
        userFastDao.closeLogicDeleteProtect();
        userFastDao.age(6).dao().delete();
    }


    public static void test_k_delete() {
        Integer delete = FastUserTestFastDao.create().userName("FastDao7").dao().delete();


    }


    public static void test_l_deleteDisk() {
        FastUserTestFastDao userFastDao = FastUserTestFastDao.create();
        userFastDao.closeLogicDeleteProtect();
        Integer delete =  userFastDao.userName("FastDao8").dao().delete();
    }


    public static void test_m_findAll() {
        List<FastUserTest> all = FastUserTestFastDao.create().dao().findAll();
    }



    public static void test_n_findPage() {
        FastUserTestFastDao fastDao = FastUserTestFastDao.create();
        fastDao.userName().likeRight("Fast").distinctField();
        fastDao.age().lessOrEqual(100);
        fastDao.age().orderByDesc().distinctField();
        PageInfo<FastUserTest> page = fastDao.dao().findPage(1, 2);
//        PageInfo<FastUserTest> page = fastDao.UnconditionalOperating.findPage(1, 2);
        System.out.println("findPage执行成功,查询到的数据为" + JSONObject.toJSONString(page, true));

    }



    public static void test_o_FieldOperating() {
        FastUserTest up = FastUserTestFastDao.create().age(18).dao().findOne();
        up.setUserName("FieldUpDate");
        up.setId(null);
        FastUserTestFastDao.create().age(up.getAge()).dao().update(up);

        FastUserTest del = FastUserTestFastDao.create().age(19).dao().findOne();
        FastUserTestFastDao.create().age().valEqual(del.getAge()).dao().findAll();
        FastUserTestFastDao.create().age().isNull().dao().findAll();
        FastUserTestFastDao.create().age().notNull().dao().findAll();
        FastUserTestFastDao.create().userName().like(del.getUserName()).hideField().dao().findAll();
        FastUserTestFastDao.create().userName().like(del.getUserName()).showField().dao().findAll();
        FastUserTestFastDao.create().userName().like(del.getUserName()).hideField().dao().findAll();

        FastUserTestFastDao template = FastUserTestFastDao.create();
        template.closeLogicDeleteProtect();
        template.userName().like(del.getUserName()).hideField().dao().findAll();

        FastUserTestFastDao.create().userName().likeRight("15").showField().dao().delete();
        FastUserTestFastDao.create().age(20).dao().delete();
        FastUserTestFastDao.create().age(20).dao().findAll();
        FastUserTestFastDao.create().age().between(1,10).dao().findPage(1,10);

        List<FastUserTest> us = FastUserTestFastDao.create().userName().notLike("Fast").dao().findAll();
        PageInfo<FastUserTest> Fast = FastUserTestFastDao.create().userName().notLike("Fast").dao().findPage(1, 10);

    }



    public static void test_p_CustomSql(){
        String sql = "SELECT * FROM fast_user_test WHERE `user_name` LIKE :userName ";
        HashMap<String, Object> params = new HashMap<>();
        params.put("userName","%FastDao%");
        List<FastUserTest> all = FastCustomSqlDao.create(FastUserTest.class, sql, params).findAll();
    }

}

package com.fast.test;

import cn.hutool.core.collection.CollUtil;
import com.alibaba.fastjson.JSONObject;
import com.fast.fast.FastCustomSqlDao;
import com.fast.fast.JoinFastDao;
import com.fast.test.pojo.User;
import com.fast.test.pojo.UserLog;
import com.fast.test.pojo.UserType;
import com.fast.test.pojo.fast.UserFastDAO;
import com.fast.test.pojo.fast.UserLogFastDAO;
import com.fast.test.pojo.fast.UserTypeFastDAO;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JoinFastDaoTest {

    static {
        //加载配置文件
        FastSetConfigTest.fastDaoConfig();
    }


    public static void main(String[] args) {
        manyToOn();
        onToMany();
        findInfo();
        testFastDaoJoin();
        fastCustomSqlDao();
    }

    public static void fastCustomSqlDao() {
        String sql = "SELECT u.id,u.user_type_id,t.id,t.type_name FROM user u LEFT JOIN user_type t on u.user_type_id = t.id where u.id=${id}";
        Map<String, Object> data = new HashMap<>();
        data.put("id", 1);
        List<CustomSqlUserDTO> all = FastCustomSqlDao.create(CustomSqlUserDTO.class, sql, data).findAll();
        System.out.println(JSONObject.toJSONString(all));
//        List<FastUserTestDTO> userTestDTOS = FastCustomSqlDao.create(FastUserTestDTO.class, sql, data).findAll();
    }

    public static void testFastDaoJoin() {
        UserLogFastDAO logFastDAO = UserLogFastDAO.create().userId(1L).id(1L);
        UserTypeFastDAO typeFastDAO = UserTypeFastDAO.create().typeName().in(CollUtil.newArrayList("类型1", "类型2"));
        UserFastDAO userFastDAO = UserFastDAO.create().userName().like("用户1").age().lessOrEqual(1);
        List<User> list = userFastDAO.leftJoin(typeFastDAO, typeFastDAO.id(), userFastDAO.userTypeId())
                .leftJoinNotQuery(logFastDAO).on(logFastDAO.userId(), userFastDAO.id()).dao().findAll();
        System.out.println(JSONObject.toJSONString(list, true));
    }

    public static void findInfo() {
        UserFastDAO userFastDAO = UserFastDAO.create();
        UserTypeFastDAO typeFastDAO = UserTypeFastDAO.create();
        UserLogFastDAO logFastDAO = UserLogFastDAO.create();

        FastCustomSqlDao<UserDTO> dao = JoinFastDao.create(UserDTO.class, userFastDAO)
                .leftJoin(typeFastDAO).on(typeFastDAO.id(), userFastDAO.userTypeId())
                .leftJoin(logFastDAO).on(logFastDAO.userId(), userFastDAO.id())
                .dao();
        List<UserDTO> infoList = dao.findAll();
        System.out.println(JSONObject.toJSONString(infoList, true));
    }


    public static void manyToOn() {
        UserFastDAO userFastDAO = UserFastDAO.create().userTypeId().notNull();
        UserTypeFastDAO typeFastDAO = UserTypeFastDAO.create();

        List<UserDTO> page = JoinFastDao.create(UserDTO.class, userFastDAO)
                .leftJoin(typeFastDAO).on(typeFastDAO.id(), userFastDAO.userTypeId()).dao().findAll();
        System.out.println(JSONObject.toJSONString(page, true));
    }

    public static void onToMany() {
        UserTypeFastDAO typeFastDAO = UserTypeFastDAO.create();
        UserFastDAO userFastDAO = UserFastDAO.create();
        List<UserTypeDTO> page = JoinFastDao.create(UserTypeDTO.class, typeFastDAO)
                .leftJoin(userFastDAO).on(userFastDAO.userTypeId(), typeFastDAO.id()).dao().findAll();
        System.out.println(JSONObject.toJSONString(page, true));
    }

    public static void addData() {
        User user1 = new User();
        user1.setUserTypeId(1L);
        user1.setDeleted(false);
        user1.setCreateTime(new Date());
        user1.setUpdateTime(new Date());
        user1.setId(1L);
        user1.setUserName("用户1");
        user1.setAge(1);
        User user2 = new User();
        user2.setUserTypeId(1L);
        user2.setDeleted(false);
        user2.setCreateTime(new Date());
        user2.setUpdateTime(new Date());
        user2.setId(2L);
        user2.setUserName("用户2");
        user2.setAge(2);
        User user3 = new User();
        user3.setUserTypeId(2L);
        user3.setDeleted(false);
        user3.setCreateTime(new Date());
        user3.setUpdateTime(new Date());
        user3.setId(3L);
        user3.setUserName("用户3");
        user3.setAge(3);
        UserFastDAO.create().dao().insertList(CollUtil.newArrayList(user1, user2, user3));

        UserType userType1 = new UserType();
        userType1.setDeleted(false);
        userType1.setCreateTime(new Date());
        userType1.setTypeName("类型1");
        userType1.setUpdateTime(new Date());
        userType1.setId(1L);
        UserType userType2 = new UserType();
        userType2.setDeleted(false);
        userType2.setCreateTime(new Date());
        userType2.setTypeName("类型2");
        userType2.setUpdateTime(new Date());
        userType2.setId(2L);
        UserTypeFastDAO.create().dao().insertList(CollUtil.newArrayList(userType1, userType2));

        UserLog userLog1 = new UserLog();
        userLog1.setDeleted(false);
        userLog1.setCreateTime(new Date());
        userLog1.setUpdateTime(new Date());
        userLog1.setId(1L);
        userLog1.setLogInfo("日志1");
        userLog1.setUserId(1L);
        UserLog userLog2 = new UserLog();
        userLog2.setDeleted(false);
        userLog2.setCreateTime(new Date());
        userLog2.setUpdateTime(new Date());
        userLog2.setId(2L);
        userLog2.setLogInfo("日志2");
        userLog2.setUserId(1L);
        UserLogFastDAO.create().dao().insertList(CollUtil.newArrayList(userLog1, userLog2));
    }
}

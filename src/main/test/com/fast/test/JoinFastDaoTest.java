package com.fast.test;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import com.alibaba.fastjson.JSONObject;
import com.fast.config.FastDaoConfig;
import com.fast.config.SqlLogLevel;
import com.fast.fast.FastCustomSqlDao;
import com.fast.fast.JoinFastDao;
import com.fast.test.pojo.User;
import com.fast.test.pojo.UserLog;
import com.fast.test.pojo.UserType;
import com.fast.test.pojo.UserTypeInfo;
import com.fast.test.pojo.fast.UserFastDAO;
import com.fast.test.pojo.fast.UserLogFastDAO;
import com.fast.test.pojo.fast.UserTypeFastDAO;
import com.fast.test.pojo.fast.UserTypeInfoFastDAO;
import com.fast.utils.page.PageInfo;

import java.util.*;

public class JoinFastDaoTest {

    static {
        //加载配置文件
        FastSetConfigTest.fastDaoConfig();
    }

    public static void setTestData() {
        List<User> userList = new ArrayList<>();
        List<UserType> userTypeList = new ArrayList<>();
        List<UserTypeInfo> userTypeInfoList = new ArrayList<>();
        List<UserLog> userLogList = new ArrayList<>();
        FastDaoConfig.openSqlPrint(SqlLogLevel.OFF, true, true);
        for (int i = 1; i <= 1000000; i++) {
            User user = new User();
            user.setId(Long.parseLong(i + ""));
            user.setTypeId(Long.parseLong(i + ""));
            user.setUserName("User" + i);
            user.setAge(i);

            UserType userType = new UserType();
            userType.setId(Long.parseLong(i + ""));
            userType.setTypeName("Type" + i);

            UserTypeInfo typeInfo = new UserTypeInfo();
            typeInfo.setUserTypeId(Long.parseLong(i + ""));
            typeInfo.setTypeInfo("TypeInfo" + i);
            typeInfo.setId(Long.parseLong(i + ""));

            UserLog log = new UserLog();
            log.setId(Long.parseLong(i + ""));
            log.setLogInfo("Log" + i);
            log.setUserId(Long.parseLong(i + ""));

            userList.add(user);
            userTypeList.add(userType);
            userTypeInfoList.add(typeInfo);
            userLogList.add(log);
            System.out.println("添加数据:" + i);
        }
        System.out.println("-----------------开始数据插入-----------------");
        UserFastDAO.create().dao().insertList(userList);
        UserTypeFastDAO.create().dao().insertList(userTypeList);
        UserTypeInfoFastDAO.create().dao().insertList(userTypeInfoList);
        UserLogFastDAO.create().dao().insertList(userLogList);
    }


    public static void main(String[] args) {
//        setTestData();
        findInfo();
//        manyToOn();
//        onToMany();
//        testFastDaoJoin();
//        fastCustomSqlDao();
    }

    public static void fastCustomSqlDao() {
        String sql = "SELECT u.id,u.user_name,u.type_id,t.id,t.type_name FROM user u LEFT JOIN user_type t on u.type_id = t.id where u.id=${id}";
        Map<String, Object> data = new HashMap<>();
        data.put("id", 1);
        PageInfo<CustomSqlUserDTO> all = FastCustomSqlDao.create(CustomSqlUserDTO.class, sql, data).findPage(1,10);
        System.out.println(JSONObject.toJSONString(all));
    }

    public static void testFastDaoJoin() {
        UserLogFastDAO logFastDAO = UserLogFastDAO.create().userId(1L).id(1L);
        UserTypeFastDAO typeFastDAO = UserTypeFastDAO.create().typeName().in(CollUtil.newArrayList("Type1", "Type2"));
        UserFastDAO userFastDAO = UserFastDAO.create().userName().like("User1").age().lessOrEqual(1);
        List<User> list = userFastDAO.leftJoin(typeFastDAO, typeFastDAO.id(), userFastDAO.typeId())
                .leftJoin(logFastDAO).on(logFastDAO.userId(), userFastDAO.id()).dao().findAll();
        System.out.println(JSONObject.toJSONString(list, true));
    }

    public static void findInfo() {
//        //设置用户查询条件
//        UserFastDAO userFastDAO = UserFastDAO.create().userName().like("User").age().greaterOrEqual(1).id().between(1, 10);
//        //设置用户类型查询条件
//        UserTypeFastDAO typeFastDAO = UserTypeFastDAO.create().typeName().likeRight("Type").deleted(false);
//        //设置用户日志1查询条件
//        UserLogFastDAO logFastDAO = UserLogFastDAO.create();
//        //设置用户日志2查询条件
//        UserLogFastDAO logFastDAO2 = UserLogFastDAO.create().id(2L);
//        //多表查询
//        List<UserDTO> userList = JoinFastDao.create(UserDTO.class, userFastDAO)
//                .leftJoin(typeFastDAO).on(typeFastDAO.id(), userFastDAO.typeId())
//                .leftJoin(logFastDAO).on(logFastDAO.userId(), userFastDAO.id())
//                .leftJoin(logFastDAO2, "log2").on(logFastDAO.userId(), userFastDAO.id()).and("log2.`deleted` = ${log2Deleted}", MapUtil.of("log2Deleted", false))
//                .dao().findAll();
        //设置用户查询条件
        UserFastDAO userFastDAO = UserFastDAO.create(UserFastDAO.class).age().lessOrEqual(100).userName().likeRight("User").userName().hideField().createTime().orderByDesc().updateTime().orderByAsc();
        //设置用户类型查询条件
        UserTypeFastDAO typeFastDAO = UserTypeFastDAO.create().typeName().likeRight("Type").id().showField().createTime().orderByDesc();
        UserTypeInfoFastDAO typeInfoFastDAO = UserTypeInfoFastDAO.create().closeLogicDeleteProtect().typeInfo("TypeInfo_1").createTime().orderByDesc();
        //设置用户日志1查询条件
        UserLogFastDAO logFastDAO = UserLogFastDAO.create().logInfo("Log1");
        //设置用户日志2查询条件
        UserLogFastDAO logFastDAO2 = UserLogFastDAO.create().closeLogicDeleteProtect().logInfo("Log2").updateTime().orderByAsc();
        //多表查询
        PageInfo<UserDTO> userList = JoinFastDao.create(UserDTO.class, userFastDAO)
                .leftJoin(typeFastDAO).on(typeFastDAO.id(), userFastDAO.typeId())
                .leftJoin(typeInfoFastDAO).on(typeInfoFastDAO.userTypeId(),typeFastDAO.id())
                .leftJoin(logFastDAO).on(logFastDAO.userId(), userFastDAO.id())
                .leftJoin(logFastDAO2,"log2").on(logFastDAO.userId(), userFastDAO.id()).and("log2.`deleted` = ${log2Deleted}", MapUtil.of("log2Deleted", false))
                .dao().findPage(1,10);
        System.out.println(JSONObject.toJSONString(userList, true));
    }


    public static void manyToOn() {
        UserFastDAO userFastDAO = UserFastDAO.create().typeId().notNull();
        UserTypeFastDAO typeFastDAO = UserTypeFastDAO.create();

        List<UserDTO> page = JoinFastDao.create(UserDTO.class, userFastDAO)
                .leftJoin(typeFastDAO).on(typeFastDAO.id(), userFastDAO.typeId()).dao().findAll();
        System.out.println(JSONObject.toJSONString(page, true));
    }

    public static void onToMany() {
        UserTypeFastDAO typeFastDAO = UserTypeFastDAO.create();
        UserFastDAO userFastDAO = UserFastDAO.create();
        List<UserTypeDTO> page = JoinFastDao.create(UserTypeDTO.class, typeFastDAO)
                .leftJoin(userFastDAO).on(userFastDAO.typeId(), typeFastDAO.id()).dao().findAll();
        System.out.println(JSONObject.toJSONString(page, true));
    }

    public static void addData() {
        User user1 = new User();
        user1.setTypeId(1L);
        user1.setDeleted(false);
        user1.setCreateTime(new Date());
        user1.setUpdateTime(new Date());
        user1.setId(1L);
        user1.setUserName("用户1");
        user1.setAge(1);
        User user2 = new User();
        user2.setTypeId(1L);
        user2.setDeleted(false);
        user2.setCreateTime(new Date());
        user2.setUpdateTime(new Date());
        user2.setId(2L);
        user2.setUserName("用户2");
        user2.setAge(2);
        User user3 = new User();
        user3.setTypeId(2L);
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

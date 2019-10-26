//import com.alibaba.fastjson.JSONObject;
//import com.db.test.dto.UserTest2Dto;
//import com.fast.db.template.template.FastCustomSqlDao;
//import com.fast.db.template.utils.page.PageInfo;
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
//public class CustomSqlTest {
//
//
//    @Test
//    public void joinQueryTest() {
//
//        String sql = "SELECT u2.`id` as id, u2.`name` as name, u1.`name` as u1Name,  u2.`age` as age, u2.`create_time` as createTime, u2.`update_time` as updateTime, u2.`deleted` as deleted\n" +
//                "FROM user_test2 u2 LEFT JOIN user_test u1 on u1.id=  u2.user_test_id\n" +
//                "WHERE (u2.`deleted` = FALSE) \n" +
//                "AND ( u1.id=  u2.user_test_id) \n" +
//                "AND u2.name = #{u2Name}";
//        Map<String, Object> params = new HashMap<>();
//        params.put("u2Name", "UserTest2张亚伟11");
//        List<UserTest2Dto> all = FastCustomSqlDao.create(UserTest2Dto.class, sql, params).findAll();
//        UserTest2Dto one = FastCustomSqlDao.create(UserTest2Dto.class, sql, params).findOne();
//        PageInfo<UserTest2Dto> page = FastCustomSqlDao.create(UserTest2Dto.class, sql, params).findPage(1, 1);
//        System.out.println(JSONObject.toJSONString(page, true));
//
//    }
//
//    @Test
//    public void joinUpdateTest() {
//
//        String sql = "UPDATE user_test2 as u2 SET age = 111\n" +
//                "WHERE  (exists(SELECT * FROM user_test as u1 WHERE  u2.user_test_id = u1.id))\n" +
//                "AND u2.name = #{u2Name}";
//        Map<String, Object> params = new HashMap<>();
//        params.put("u2Name", "UserTest2张亚伟11");
//        Integer update = FastCustomSqlDao.create(UserTest2Dto.class, sql, params).update();
//        System.out.println(JSONObject.toJSONString(update, true));
//
//    }
//
//}

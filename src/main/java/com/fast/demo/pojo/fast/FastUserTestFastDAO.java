package com.fast.demo.pojo.fast;

import com.fast.demo.pojo.FastUserTest;
import com.fast.base.BaseFastDAO;
import com.fast.condition.FastExample;
import java.util.Date;

/**
* FAST-DAO 测试表
*/
public class FastUserTestFastDAO extends BaseFastDAO<FastUserTest> {

    public static FastUserTestFastDAO create(){return new FastUserTestFastDAO();}
    public static FastUserTestFastDAO create(Object object) {
        FastUserTestFastDAO fastDao = new FastUserTestFastDAO();
        fastDao.equalObject(object);
        return fastDao;
    }

    public FastExample.Criteria<FastUserTest> deleted(Boolean... deleteds){return fastExample.field("deleted").valEqual(deleteds);}
    public FastExample.Criteria<FastUserTest> createTime(Date... createTimes){return fastExample.field("createTime").valEqual(createTimes);}
    public FastExample.Criteria<FastUserTest> updateTime(Date... updateTimes){return fastExample.field("updateTime").valEqual(updateTimes);}
    public FastExample.Criteria<FastUserTest> id(Long... ids){return fastExample.field("id").valEqual(ids);}
    public FastExample.Criteria<FastUserTest> userName(String... userNames){return fastExample.field("userName").valEqual(userNames);}
    public FastExample.Criteria<FastUserTest> age(Integer... ages){return fastExample.field("age").valEqual(ages);}

}
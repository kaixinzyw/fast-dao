package com.fast.demo.pojo.fast;

import com.fast.demo.pojo.FastUserTest;
import com.fast.base.BaseFastDao;
import com.fast.condition.FastExample;
import java.util.Date;

/**
* @author 2019-12-7 0:31:12
*/
public class FastUserTestFastDao extends BaseFastDao<FastUserTest> {

    public static FastUserTestFastDao create(){return new FastUserTestFastDao();}
    public static FastUserTestFastDao create(Object object) {
        FastUserTestFastDao fastDao = new FastUserTestFastDao();
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
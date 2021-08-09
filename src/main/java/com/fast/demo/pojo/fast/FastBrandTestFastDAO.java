package com.fast.demo.pojo.fast;

import com.fast.demo.pojo.FastBrandTest;
import com.fast.base.BaseFastDAO;
import com.fast.condition.FastExample;
import java.util.Date;

/**
* 品牌表
*/
public class FastBrandTestFastDAO extends BaseFastDAO<FastBrandTest> {

    public static FastBrandTestFastDAO create(){return new FastBrandTestFastDAO();}
    public static FastBrandTestFastDAO create(Object object) {
        FastBrandTestFastDAO fastDao = new FastBrandTestFastDAO();
        fastDao.equalObject(object);
        return fastDao;
    }

    public FastExample.Criteria<FastBrandTest> no(String... nos){return fastExample.field("no").valEqual(nos);}
    public FastExample.Criteria<FastBrandTest> brandName(String... brandNames){return fastExample.field("brandName").valEqual(brandNames);}
    public FastExample.Criteria<FastBrandTest> createOrg(Long... createOrgs){return fastExample.field("createOrg").valEqual(createOrgs);}
    public FastExample.Criteria<FastBrandTest> remark(String... remarks){return fastExample.field("remark").valEqual(remarks);}
    public FastExample.Criteria<FastBrandTest> updateTime(Date... updateTimes){return fastExample.field("updateTime").valEqual(updateTimes);}
    public FastExample.Criteria<FastBrandTest> brandStatus(Integer... brandStatuss){return fastExample.field("brandStatus").valEqual(brandStatuss);}
    public FastExample.Criteria<FastBrandTest> createBy(String... createBys){return fastExample.field("createBy").valEqual(createBys);}
    public FastExample.Criteria<FastBrandTest> deleted(Boolean... deleteds){return fastExample.field("deleted").valEqual(deleteds);}
    public FastExample.Criteria<FastBrandTest> updateBy(String... updateBys){return fastExample.field("updateBy").valEqual(updateBys);}
    public FastExample.Criteria<FastBrandTest> createTime(Date... createTimes){return fastExample.field("createTime").valEqual(createTimes);}
    public FastExample.Criteria<FastBrandTest> grade(String... grades){return fastExample.field("grade").valEqual(grades);}
    public FastExample.Criteria<FastBrandTest> brandSort(Integer... brandSorts){return fastExample.field("brandSort").valEqual(brandSorts);}
    public FastExample.Criteria<FastBrandTest> id(Long... ids){return fastExample.field("id").valEqual(ids);}

}
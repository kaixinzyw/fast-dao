package com.fast.demo.pojo.fast;

import com.fast.demo.pojo.Brand;
import com.fast.base.BaseFastDAO;
import com.fast.condition.FastExample;
import java.util.Date;

/**
* 品牌表
*/
public class BrandFastDAO extends BaseFastDAO<Brand> {

    public static BrandFastDAO create(){return new BrandFastDAO();}
    public static BrandFastDAO create(Object object) {
        BrandFastDAO fastDao = new BrandFastDAO();
        fastDao.equalObject(object);
        return fastDao;
    }

    public FastExample.Criteria<Brand> no(String... nos){return fastExample.field("no").valEqual(nos);}
    public FastExample.Criteria<Brand> brandName(String... brandNames){return fastExample.field("brandName").valEqual(brandNames);}
    public FastExample.Criteria<Brand> createOrg(Long... createOrgs){return fastExample.field("createOrg").valEqual(createOrgs);}
    public FastExample.Criteria<Brand> remark(String... remarks){return fastExample.field("remark").valEqual(remarks);}
    public FastExample.Criteria<Brand> updateTime(Date... updateTimes){return fastExample.field("updateTime").valEqual(updateTimes);}
    public FastExample.Criteria<Brand> brandStatus(Integer... brandStatuss){return fastExample.field("brandStatus").valEqual(brandStatuss);}
    public FastExample.Criteria<Brand> createBy(String... createBys){return fastExample.field("createBy").valEqual(createBys);}
    public FastExample.Criteria<Brand> deleted(Boolean... deleteds){return fastExample.field("deleted").valEqual(deleteds);}
    public FastExample.Criteria<Brand> updateBy(String... updateBys){return fastExample.field("updateBy").valEqual(updateBys);}
    public FastExample.Criteria<Brand> createTime(Date... createTimes){return fastExample.field("createTime").valEqual(createTimes);}
    public FastExample.Criteria<Brand> grade(String... grades){return fastExample.field("grade").valEqual(grades);}
    public FastExample.Criteria<Brand> brandSort(Integer... brandSorts){return fastExample.field("brandSort").valEqual(brandSorts);}
    public FastExample.Criteria<Brand> id(Long... ids){return fastExample.field("id").valEqual(ids);}

}
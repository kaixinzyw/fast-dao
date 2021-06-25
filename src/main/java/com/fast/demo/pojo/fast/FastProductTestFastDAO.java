package com.fast.demo.pojo.fast;

import com.fast.base.BaseFastDAO;
import com.fast.condition.FastExample;

import java.util.Date;

/**
* 产品表
*/
public class FastProductTestFastDAO extends BaseFastDAO<FastProductTestFastDAO> {

    public static FastProductTestFastDAO create(){return new FastProductTestFastDAO();}
    public static FastProductTestFastDAO create(Object object) {
        FastProductTestFastDAO fastDao = new FastProductTestFastDAO();
        fastDao.equalObject(object);
        return fastDao;
    }

    public FastExample.Criteria<FastProductTestFastDAO> no(String... nos){return fastExample.field("no").valEqual(nos);}
    public FastExample.Criteria<FastProductTestFastDAO> varietyForm(String... varietyForms){return fastExample.field("varietyForm").valEqual(varietyForms);}
    public FastExample.Criteria<FastProductTestFastDAO> productStatus(Integer... productStatuss){return fastExample.field("productStatus").valEqual(productStatuss);}
    public FastExample.Criteria<FastProductTestFastDAO> measureUnit(String... measureUnits){return fastExample.field("measureUnit").valEqual(measureUnits);}
    public FastExample.Criteria<FastProductTestFastDAO> remark(String... remarks){return fastExample.field("remark").valEqual(remarks);}
    public FastExample.Criteria<FastProductTestFastDAO> updateTime(Date... updateTimes){return fastExample.field("updateTime").valEqual(updateTimes);}
    public FastExample.Criteria<FastProductTestFastDAO> productName(String... productNames){return fastExample.field("productName").valEqual(productNames);}
    public FastExample.Criteria<FastProductTestFastDAO> specs(String... specss){return fastExample.field("specs").valEqual(specss);}
    public FastExample.Criteria<FastProductTestFastDAO> createBy(String... createBys){return fastExample.field("createBy").valEqual(createBys);}
    public FastExample.Criteria<FastProductTestFastDAO> deleted(Boolean... deleteds){return fastExample.field("deleted").valEqual(deleteds);}
    public FastExample.Criteria<FastProductTestFastDAO> updateBy(String... updateBys){return fastExample.field("updateBy").valEqual(updateBys);}
    public FastExample.Criteria<FastProductTestFastDAO> createTime(Date... createTimes){return fastExample.field("createTime").valEqual(createTimes);}
    public FastExample.Criteria<FastProductTestFastDAO> productSort(Integer... productSorts){return fastExample.field("productSort").valEqual(productSorts);}
    public FastExample.Criteria<FastProductTestFastDAO> id(Long... ids){return fastExample.field("id").valEqual(ids);}
    public FastExample.Criteria<FastProductTestFastDAO> useOrgs(String... useOrgss){return fastExample.field("useOrgs").valEqual(useOrgss);}
    public FastExample.Criteria<FastProductTestFastDAO> productType(String... productTypes){return fastExample.field("productType").valEqual(productTypes);}

}
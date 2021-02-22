package com.fast.demo.pojo.fast;

import com.fast.demo.pojo.Product;
import com.fast.base.BaseFastDAO;
import com.fast.condition.FastExample;
import java.util.Date;

/**
* 产品表
*/
public class ProductFastDAO extends BaseFastDAO<Product> {

    public static ProductFastDAO create(){return new ProductFastDAO();}
    public static ProductFastDAO create(Object object) {
        ProductFastDAO fastDao = new ProductFastDAO();
        fastDao.equalObject(object);
        return fastDao;
    }

    public FastExample.Criteria<Product> no(String... nos){return fastExample.field("no").valEqual(nos);}
    public FastExample.Criteria<Product> varietyForm(String... varietyForms){return fastExample.field("varietyForm").valEqual(varietyForms);}
    public FastExample.Criteria<Product> productStatus(Integer... productStatuss){return fastExample.field("productStatus").valEqual(productStatuss);}
    public FastExample.Criteria<Product> measureUnit(String... measureUnits){return fastExample.field("measureUnit").valEqual(measureUnits);}
    public FastExample.Criteria<Product> remark(String... remarks){return fastExample.field("remark").valEqual(remarks);}
    public FastExample.Criteria<Product> updateTime(Date... updateTimes){return fastExample.field("updateTime").valEqual(updateTimes);}
    public FastExample.Criteria<Product> productName(String... productNames){return fastExample.field("productName").valEqual(productNames);}
    public FastExample.Criteria<Product> specs(String... specss){return fastExample.field("specs").valEqual(specss);}
    public FastExample.Criteria<Product> createBy(String... createBys){return fastExample.field("createBy").valEqual(createBys);}
    public FastExample.Criteria<Product> deleted(Boolean... deleteds){return fastExample.field("deleted").valEqual(deleteds);}
    public FastExample.Criteria<Product> updateBy(String... updateBys){return fastExample.field("updateBy").valEqual(updateBys);}
    public FastExample.Criteria<Product> createTime(Date... createTimes){return fastExample.field("createTime").valEqual(createTimes);}
    public FastExample.Criteria<Product> productSort(Integer... productSorts){return fastExample.field("productSort").valEqual(productSorts);}
    public FastExample.Criteria<Product> id(Long... ids){return fastExample.field("id").valEqual(ids);}
    public FastExample.Criteria<Product> useOrgs(String... useOrgss){return fastExample.field("useOrgs").valEqual(useOrgss);}
    public FastExample.Criteria<Product> productType(String... productTypes){return fastExample.field("productType").valEqual(productTypes);}

}
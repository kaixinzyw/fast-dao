package com.fast.demo.pojo.fast;

import com.fast.base.BaseFastDAO;
import com.fast.condition.FastExample;
import com.fast.demo.pojo.FastProductBrandTest;

/**
* 品种品牌关联
*/
public class FastProductBrandTestFastDAO extends BaseFastDAO<FastProductBrandTest> {

    public static FastProductBrandTestFastDAO create(){return new FastProductBrandTestFastDAO();}
    public static FastProductBrandTestFastDAO create(Object object) {
        FastProductBrandTestFastDAO fastDao = new FastProductBrandTestFastDAO();
        fastDao.equalObject(object);
        return fastDao;
    }

    public FastExample.Criteria<FastProductBrandTest> productId(Long... productIds){return fastExample.field("productId").valEqual(productIds);}
    public FastExample.Criteria<FastProductBrandTest> brandId(Long... brandIds){return fastExample.field("brandId").valEqual(brandIds);}

}
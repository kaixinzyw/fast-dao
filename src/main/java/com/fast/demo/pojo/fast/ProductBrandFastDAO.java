package com.fast.demo.pojo.fast;

import com.fast.demo.pojo.ProductBrand;
import com.fast.base.BaseFastDAO;
import com.fast.condition.FastExample;

/**
* 品种品牌关联
*/
public class ProductBrandFastDAO extends BaseFastDAO<ProductBrand> {

    public static ProductBrandFastDAO create(){return new ProductBrandFastDAO();}
    public static ProductBrandFastDAO create(Object object) {
        ProductBrandFastDAO fastDao = new ProductBrandFastDAO();
        fastDao.equalObject(object);
        return fastDao;
    }

    public FastExample.Criteria<ProductBrand> productId(Long... productIds){return fastExample.field("productId").valEqual(productIds);}
    public FastExample.Criteria<ProductBrand> brandId(Long... brandIds){return fastExample.field("brandId").valEqual(brandIds);}

}
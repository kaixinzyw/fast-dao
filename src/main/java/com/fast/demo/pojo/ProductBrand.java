package com.fast.demo.pojo;

import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.Column;
import java.io.Serializable;

/**
* 品种品牌关联
*/
@Table(name = "product_brand")
public class ProductBrand implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
    *品牌主键
    */
    @Column(name = "brand_id")
    private Long brandId;

    /**
    *品种主键
    */
    @Column(name = "product_id")
    private Long productId;


    public Long getProductId() {
        return this.productId;
    }
    public ProductBrand setProductId(Long productId) {
        this.productId = productId;
        return this;
    }

    public Long getBrandId() {
        return this.brandId;
    }
    public ProductBrand setBrandId(Long brandId) {
        this.brandId = brandId;
        return this;
    }

}

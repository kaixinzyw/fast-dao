package com.fast.demo.pojo;

import javax.persistence.Column;
import javax.persistence.Table;
import java.io.Serializable;

/**
* 品种品牌关联
*/
@Table(name = "product_brand")
public class FastProductBrandTest implements Serializable {

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


    public Long getBrandId() {
        return brandId;
    }

    public void setBrandId(Long brandId) {
        this.brandId = brandId;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }
}

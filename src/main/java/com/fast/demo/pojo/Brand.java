package com.fast.demo.pojo;

import com.fast.condition.many.FastManyToMany;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
* 品牌表
*/
@Table(name = "brand")
public class Brand implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
    *主键
    */
    @Id
    @Column(name = "id")
    private Long id;

    /**
    *品牌编号
    */
    @Column(name = "no")
    private String no;

    /**
    *品牌名称
    */
    @Column(name = "brand_name")
    private String brandName;

    /**
    *品级
    */
    @Column(name = "grade")
    private String grade;

    /**
    *品牌排序
    */
    @Column(name = "brand_sort")
    private Integer brandSort;

    /**
    *产品状态
    */
    @Column(name = "brand_status")
    private Integer brandStatus;

    /**
    *备注
    */
    @Column(name = "remark")
    private String remark;

    /**
    *创建者组织
    */
    @Column(name = "create_org")
    private Long createOrg;

    /**
    *创建人
    */
    @Column(name = "create_by")
    private String createBy;

    /**
    *更新人
    */
    @Column(name = "update_by")
    private String updateBy;

    /**
    *创建时间
    */
    @Column(name = "create_time")
    private Date createTime;

    /**
    *更新时间
    */
    @Column(name = "update_time")
    private Date updateTime;

    /**
    *是否删除
    */
    @Column(name = "deleted")
    private Boolean deleted;

    @FastManyToMany(joinEntity=ProductBrand.class,relationalEntity = Product.class)
    private List<Product> productList;


    public String getNo() {
        return this.no;
    }
    public Brand setNo(String no) {
        this.no = no;
        return this;
    }

    public String getBrandName() {
        return this.brandName;
    }
    public Brand setBrandName(String brandName) {
        this.brandName = brandName;
        return this;
    }

    public Long getCreateOrg() {
        return this.createOrg;
    }
    public Brand setCreateOrg(Long createOrg) {
        this.createOrg = createOrg;
        return this;
    }

    public String getRemark() {
        return this.remark;
    }
    public Brand setRemark(String remark) {
        this.remark = remark;
        return this;
    }

    public Date getUpdateTime() {
        return this.updateTime;
    }
    public Brand setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
        return this;
    }

    public Integer getBrandStatus() {
        return this.brandStatus;
    }
    public Brand setBrandStatus(Integer brandStatus) {
        this.brandStatus = brandStatus;
        return this;
    }

    public String getCreateBy() {
        return this.createBy;
    }
    public Brand setCreateBy(String createBy) {
        this.createBy = createBy;
        return this;
    }

    public Boolean getDeleted() {
        return this.deleted;
    }
    public Brand setDeleted(Boolean deleted) {
        this.deleted = deleted;
        return this;
    }

    public String getUpdateBy() {
        return this.updateBy;
    }
    public Brand setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
        return this;
    }

    public Date getCreateTime() {
        return this.createTime;
    }
    public Brand setCreateTime(Date createTime) {
        this.createTime = createTime;
        return this;
    }

    public String getGrade() {
        return this.grade;
    }
    public Brand setGrade(String grade) {
        this.grade = grade;
        return this;
    }

    public Integer getBrandSort() {
        return this.brandSort;
    }
    public Brand setBrandSort(Integer brandSort) {
        this.brandSort = brandSort;
        return this;
    }

    public Long getId() {
        return this.id;
    }
    public Brand setId(Long id) {
        this.id = id;
        return this;
    }

    public List<Product> getProductList() {
        return productList;
    }

    public void setProductList(List<Product> productList) {
        this.productList = productList;
    }
}

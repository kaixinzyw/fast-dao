package com.fast.demo.pojo;

import com.fast.condition.many.FastManyToMany;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
* 产品表
*/
@Table(name = "product")
public class Product implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
    *主键
    */
    @Id
    @Column(name = "id")
    private Long id;

    /**
    *产品编号
    */
    @Column(name = "no")
    private String no;



    /**
     *主键
     */
    @Id
    @Column(name = "brand_id")
    private Long brandId;


    /**
    *名称
    */
    @Column(name = "product_name")
    private String productName;

    /**
    *规格
    */
    @Column(name = "specs")
    private String specs;

    /**
    *产品
    */
    @Column(name = "product_type")
    private String productType;

    /**
    *形态
    */
    @Column(name = "variety_form")
    private String varietyForm;

    /**
    *主计量单位
    */
    @Column(name = "measure_unit")
    private String measureUnit;

    /**
    *备注
    */
    @Column(name = "remark")
    private String remark;

    /**
    *
    */
    @Column(name = "use_orgs")
    private String useOrgs;

    /**
    *产品排序
    */
    @Column(name = "product_sort")
    private Integer productSort;

    /**
    *产品状态
    */
    @Column(name = "product_status")
    private Integer productStatus;

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
    
    @FastManyToMany(joinEntity = ProductBrand.class,relationalEntity = Brand.class)
    private List<Brand> brandList;

    public String getNo() {
        return this.no;
    }
    public Product setNo(String no) {
        this.no = no;
        return this;
    }

    public String getVarietyForm() {
        return this.varietyForm;
    }
    public Product setVarietyForm(String varietyForm) {
        this.varietyForm = varietyForm;
        return this;
    }

    public Integer getProductStatus() {
        return this.productStatus;
    }
    public Product setProductStatus(Integer productStatus) {
        this.productStatus = productStatus;
        return this;
    }

    public String getMeasureUnit() {
        return this.measureUnit;
    }
    public Product setMeasureUnit(String measureUnit) {
        this.measureUnit = measureUnit;
        return this;
    }

    public String getRemark() {
        return this.remark;
    }
    public Product setRemark(String remark) {
        this.remark = remark;
        return this;
    }

    public Date getUpdateTime() {
        return this.updateTime;
    }
    public Product setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
        return this;
    }

    public String getProductName() {
        return this.productName;
    }
    public Product setProductName(String productName) {
        this.productName = productName;
        return this;
    }

    public String getSpecs() {
        return this.specs;
    }
    public Product setSpecs(String specs) {
        this.specs = specs;
        return this;
    }

    public String getCreateBy() {
        return this.createBy;
    }
    public Product setCreateBy(String createBy) {
        this.createBy = createBy;
        return this;
    }

    public Boolean getDeleted() {
        return this.deleted;
    }
    public Product setDeleted(Boolean deleted) {
        this.deleted = deleted;
        return this;
    }

    public String getUpdateBy() {
        return this.updateBy;
    }
    public Product setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
        return this;
    }

    public Date getCreateTime() {
        return this.createTime;
    }
    public Product setCreateTime(Date createTime) {
        this.createTime = createTime;
        return this;
    }

    public Integer getProductSort() {
        return this.productSort;
    }
    public Product setProductSort(Integer productSort) {
        this.productSort = productSort;
        return this;
    }

    public Long getId() {
        return this.id;
    }
    public Product setId(Long id) {
        this.id = id;
        return this;
    }

    public String getUseOrgs() {
        return this.useOrgs;
    }
    public Product setUseOrgs(String useOrgs) {
        this.useOrgs = useOrgs;
        return this;
    }

    public String getProductType() {
        return this.productType;
    }
    public Product setProductType(String productType) {
        this.productType = productType;
        return this;
    }

    public List<Brand> getBrandList() {
        return brandList;
    }

    public void setBrandList(List<Brand> brandList) {
        this.brandList = brandList;
    }

    public Long getBrandId() {
        return brandId;
    }

    public void setBrandId(Long brandId) {
        this.brandId = brandId;
    }
}

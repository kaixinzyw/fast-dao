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
public class FastProductTest implements Serializable {

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
    
    @FastManyToMany(joinEntity = FastProductBrandTest.class,relationalEntity = FastBrandTest.class)
    private List<FastBrandTest> fastBrandTestList;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public Long getBrandId() {
        return brandId;
    }

    public void setBrandId(Long brandId) {
        this.brandId = brandId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getSpecs() {
        return specs;
    }

    public void setSpecs(String specs) {
        this.specs = specs;
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public String getVarietyForm() {
        return varietyForm;
    }

    public void setVarietyForm(String varietyForm) {
        this.varietyForm = varietyForm;
    }

    public String getMeasureUnit() {
        return measureUnit;
    }

    public void setMeasureUnit(String measureUnit) {
        this.measureUnit = measureUnit;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getUseOrgs() {
        return useOrgs;
    }

    public void setUseOrgs(String useOrgs) {
        this.useOrgs = useOrgs;
    }

    public Integer getProductSort() {
        return productSort;
    }

    public void setProductSort(Integer productSort) {
        this.productSort = productSort;
    }

    public Integer getProductStatus() {
        return productStatus;
    }

    public void setProductStatus(Integer productStatus) {
        this.productStatus = productStatus;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public List<FastBrandTest> getFastBrandTestList() {
        return fastBrandTestList;
    }

    public void setFastBrandTestList(List<FastBrandTest> fastBrandTestList) {
        this.fastBrandTestList = fastBrandTestList;
    }
}

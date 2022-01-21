package com.fast.test.pojo;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/**
* 用户类型信息
*/
@Table(name = "user_type_info")
public class UserTypeInfo implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
    *主键
    */
    @Id
    @Column(name = "id")
    private Long id;

    /**
    *用户类型ID
    */
    @Column(name = "user_type_id")
    private Long userTypeId;

    /**
    *类型信息
    */
    @Column(name = "type_info")
    private String typeInfo;

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


    public Long getUserTypeId() {
        return this.userTypeId;
    }
    public UserTypeInfo setUserTypeId(Long userTypeId) {
        this.userTypeId = userTypeId;
        return this;
    }

    public String getTypeInfo() {
        return this.typeInfo;
    }
    public UserTypeInfo setTypeInfo(String typeInfo) {
        this.typeInfo = typeInfo;
        return this;
    }

    public Boolean getDeleted() {
        return this.deleted;
    }
    public UserTypeInfo setDeleted(Boolean deleted) {
        this.deleted = deleted;
        return this;
    }

    public Date getCreateTime() {
        return this.createTime;
    }
    public UserTypeInfo setCreateTime(Date createTime) {
        this.createTime = createTime;
        return this;
    }

    public Date getUpdateTime() {
        return this.updateTime;
    }
    public UserTypeInfo setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
        return this;
    }

    public Long getId() {
        return this.id;
    }
    public UserTypeInfo setId(Long id) {
        this.id = id;
        return this;
    }

}

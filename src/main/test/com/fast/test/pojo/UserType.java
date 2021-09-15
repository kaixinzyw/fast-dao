package com.fast.test.pojo;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/**
* 用户类型
*/
@Table(name = "user_type")
public class UserType implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
    *主键
    */
    @Id
    @Column(name = "id")
    private Long id;

    /**
    *用户名
    */
    @Column(name = "type_name")
    private String typeName;

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


    public Boolean getDeleted() {
        return this.deleted;
    }
    public UserType setDeleted(Boolean deleted) {
        this.deleted = deleted;
        return this;
    }

    public Date getCreateTime() {
        return this.createTime;
    }
    public UserType setCreateTime(Date createTime) {
        this.createTime = createTime;
        return this;
    }

    public String getTypeName() {
        return this.typeName;
    }
    public UserType setTypeName(String typeName) {
        this.typeName = typeName;
        return this;
    }

    public Date getUpdateTime() {
        return this.updateTime;
    }
    public UserType setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
        return this;
    }

    public Long getId() {
        return this.id;
    }
    public UserType setId(Long id) {
        this.id = id;
        return this;
    }

}

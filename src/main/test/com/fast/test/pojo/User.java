package com.fast.test.pojo;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/**
* 用户
*/
@Table(name = "user")
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
    *主键
    */
    @Id
    @Column(name = "id")
    private Long id;

    /**
    *用户类型
    */
    @Column(name = "type_id")
    private Long typeId;

    /**
    *用户名
    */
    @Column(name = "user_name")
    private String userName;

    /**
    *年龄
    */
    @Column(name = "age")
    private Integer age;

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
    public User setDeleted(Boolean deleted) {
        this.deleted = deleted;
        return this;
    }

    public Date getCreateTime() {
        return this.createTime;
    }
    public User setCreateTime(Date createTime) {
        this.createTime = createTime;
        return this;
    }

    public Long getTypeId() {
        return this.typeId;
    }
    public User setTypeId(Long typeId) {
        this.typeId = typeId;
        return this;
    }

    public Date getUpdateTime() {
        return this.updateTime;
    }
    public User setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
        return this;
    }

    public Long getId() {
        return this.id;
    }
    public User setId(Long id) {
        this.id = id;
        return this;
    }

    public String getUserName() {
        return this.userName;
    }
    public User setUserName(String userName) {
        this.userName = userName;
        return this;
    }

    public Integer getAge() {
        return this.age;
    }
    public User setAge(Integer age) {
        this.age = age;
        return this;
    }

}

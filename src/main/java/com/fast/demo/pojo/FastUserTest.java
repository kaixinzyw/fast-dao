package com.fast.demo.pojo;

import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.Column;
import java.io.Serializable;
import java.util.Date;

/**
* FAST-DAO 测试表
*/
@Table(name = "fast_user_test")
public class FastUserTest implements Serializable {

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
    public FastUserTest setDeleted(Boolean deleted) {
        this.deleted = deleted;
        return this;
    }

    public Date getCreateTime() {
        return this.createTime;
    }
    public FastUserTest setCreateTime(Date createTime) {
        this.createTime = createTime;
        return this;
    }

    public Date getUpdateTime() {
        return this.updateTime;
    }
    public FastUserTest setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
        return this;
    }

    public Long getId() {
        return this.id;
    }
    public FastUserTest setId(Long id) {
        this.id = id;
        return this;
    }

    public String getUserName() {
        return this.userName;
    }
    public FastUserTest setUserName(String userName) {
        this.userName = userName;
        return this;
    }

    public Integer getAge() {
        return this.age;
    }
    public FastUserTest setAge(Integer age) {
        this.age = age;
        return this;
    }

}

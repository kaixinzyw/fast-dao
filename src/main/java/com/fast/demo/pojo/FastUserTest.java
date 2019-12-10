package com.fast.demo.pojo;

import com.fast.cache.FastStatisCache;

import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.Column;
import java.io.Serializable;
import java.util.Date;

/**
* 实体bean 
* @author 2019-12-7 0:31:12
*/
@Table(name = "fast_user_test")
@FastStatisCache
public class FastUserTest implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
    *
    */
    @Id
    @Column(name = "id")
    private Long id;
    /**
    *
    */
    @Column(name = "user_name")
    private String userName;
    /**
    *
    */
    @Column(name = "age")
    private Integer age;
    /**
    *
    */
    @Column(name = "create_time")
    private Date createTime;
    /**
    *
    */
    @Column(name = "update_time")
    private Date updateTime;
    /**
    *
    */
    @Column(name = "deleted")
    private Boolean deleted;

    public  Boolean getDeleted() {
        return this.deleted;
    }
    public  void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public  Date getCreateTime() {
        return this.createTime;
    }
    public  void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public  Date getUpdateTime() {
        return this.updateTime;
    }
    public  void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public  Long getId() {
        return this.id;
    }
    public  void setId(Long id) {
        this.id = id;
    }

    public  String getUserName() {
        return this.userName;
    }
    public  void setUserName(String userName) {
        this.userName = userName;
    }

    public  Integer getAge() {
        return this.age;
    }
    public  void setAge(Integer age) {
        this.age = age;
    }

}

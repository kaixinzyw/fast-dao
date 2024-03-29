package com.fast.test;


import java.io.Serializable;
import java.util.Date;

/**
* 实体bean 
* @author 2019-12-14 1:39:04
*/
public class FastUserTestQuery implements Serializable {


    private static final long serialVersionUID = -5269407877536439007L;
    /**
    *主键ID
    */
    private Long id;
    /**
    *用户名
    */
    private String userName;
    /**
    *年龄
    */
    private Integer agee;
    /**
    *创建时间
    */
    private Date createTime;
    /**
    *更新时间
    */
    private Date updateTime;
    /**
    *是否逻辑删除
    */
    private Boolean deleted;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Integer getAgee() {
        return agee;
    }

    public void setAgee(Integer agee) {
        this.agee = agee;
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
}

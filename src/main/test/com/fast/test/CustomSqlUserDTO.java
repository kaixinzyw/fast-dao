package com.fast.test;

import com.fast.dao.many.FastJoinQuery;
import com.fast.test.pojo.UserType;

import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/**
 * FAST-DAO 测试表
 */
@Table(name = "u")
public class CustomSqlUserDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @FastJoinQuery(thisColumnName = "user_type_id", joinTableAlias = "t", joinColumnName = "id")
    private UserType userType;

    /**
     * 主键
     */
    private Long id;

    /**
     * 用户类型
     */
    private Long userTypeId;

    /**
     * 用户名
     */
    private String userName;

    /**
     * 年龄
     */
    private Integer age;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 是否删除
     */
    private Boolean deleted;

    public UserType getUserType() {
        return userType;
    }

    public void setUserType(UserType userType) {
        this.userType = userType;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserTypeId() {
        return userTypeId;
    }

    public void setUserTypeId(Long userTypeId) {
        this.userTypeId = userTypeId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
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

package com.fast.test;

import com.fast.dao.many.FastJoinQuery;
import com.fast.fast.TableAlias;
import com.fast.test.pojo.User;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * FAST-DAO 测试表
 */
@TableAlias("user_type")
public class UserTypeDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @FastJoinQuery
    private List<User> userList;

    /**
     *主键
     */
    private Long id;

    /**
     *用户名
     */
    private String typeName;

    /**
     *创建时间
     */
    private Date createTime;

    /**
     *更新时间
     */
    private Date updateTime;

    /**
     *是否删除
     */
    private Boolean deleted;

    public List<User> getUserList() {
        return userList;
    }

    public void setUserList(List<User> userList) {
        this.userList = userList;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
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

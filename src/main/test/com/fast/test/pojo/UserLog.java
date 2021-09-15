package com.fast.test.pojo;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/**
* 用户日志
*/
@Table(name = "user_log")
public class UserLog implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
    *主键
    */
    @Id
    @Column(name = "id")
    private Long id;

    /**
    *用户ID
    */
    @Column(name = "user_id")
    private Long userId;

    /**
    *日志内容
    */
    @Column(name = "log_info")
    private String logInfo;

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
    public UserLog setDeleted(Boolean deleted) {
        this.deleted = deleted;
        return this;
    }

    public Date getCreateTime() {
        return this.createTime;
    }
    public UserLog setCreateTime(Date createTime) {
        this.createTime = createTime;
        return this;
    }

    public Date getUpdateTime() {
        return this.updateTime;
    }
    public UserLog setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
        return this;
    }

    public Long getId() {
        return this.id;
    }
    public UserLog setId(Long id) {
        this.id = id;
        return this;
    }

    public String getLogInfo() {
        return this.logInfo;
    }
    public UserLog setLogInfo(String logInfo) {
        this.logInfo = logInfo;
        return this;
    }

    public Long getUserId() {
        return this.userId;
    }
    public UserLog setUserId(Long userId) {
        this.userId = userId;
        return this;
    }

}

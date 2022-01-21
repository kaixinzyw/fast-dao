package com.fast.test;

import com.fast.dao.many.FastJoinQuery;
import com.fast.mapper.ColumnAlias;
import com.fast.mapper.TableAlias;
import com.fast.mapper.NotQuery;
import com.fast.test.pojo.User;
import com.fast.test.pojo.UserLog;

import java.io.Serializable;
import java.util.List;

/**
 * 用户多表查询DTO
 */
@TableAlias("user_test")
public class UserDTO extends User implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotQuery
    private Boolean deleted;

    @FastJoinQuery
    private List<UserLog> userLogList;

    @FastJoinQuery("log2")
    private List<UserLog> userLogList2;

    @FastJoinQuery("user_type")
    private UserTypeDTO userType;

    @TableAlias("user_type")
    private String typeName;

    @TableAlias("user_type")
    @ColumnAlias("id")
    private Long userTypeId;

    public List<UserLog> getUserLogList() {
        return userLogList;
    }

    public void setUserLogList(List<UserLog> userLogList) {
        this.userLogList = userLogList;
    }

    public UserTypeDTO getUserType() {
        return userType;
    }

    public void setUserType(UserTypeDTO userType) {
        this.userType = userType;
    }

    public List<UserLog> getUserLogList2() {
        return userLogList2;
    }

    public void setUserLogList2(List<UserLog> userLogList2) {
        this.userLogList2 = userLogList2;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public Long getUserTypeId() {
        return userTypeId;
    }

    public void setUserTypeId(Long userTypeId) {
        this.userTypeId = userTypeId;
    }
}

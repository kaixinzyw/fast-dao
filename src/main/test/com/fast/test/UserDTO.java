package com.fast.test;

import com.fast.dao.many.FastJoinQuery;
import com.fast.fast.TableAlias;
import com.fast.test.pojo.User;
import com.fast.test.pojo.UserLog;
import com.fast.test.pojo.UserType;

import java.io.Serializable;
import java.util.List;

/**
 * 商品信息
 */
@TableAlias("user")
public class UserDTO extends User implements Serializable {

    private static final long serialVersionUID = 1L;

    @FastJoinQuery(joinColumnName = "user_id")
    private List<UserLog> userLogList;
    @FastJoinQuery
    private UserType userType;

    @TableAlias("user_type")
    private String typeName;


    public List<UserLog> getUserLogList() {
        return userLogList;
    }

    public void setUserLogList(List<UserLog> userLogList) {
        this.userLogList = userLogList;
    }

    public UserType getUserType() {
        return userType;
    }

    public void setUserType(UserType userType) {
        this.userType = userType;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

}

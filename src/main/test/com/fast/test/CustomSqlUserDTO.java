package com.fast.test;

import com.fast.dao.many.FastJoinQuery;
import com.fast.test.pojo.User;
import com.fast.test.pojo.UserType;

import javax.persistence.Table;
import java.io.Serializable;

/**
 * FAST-DAO 测试表
 */
@Table(name = "u")
public class CustomSqlUserDTO extends User implements Serializable {

    private static final long serialVersionUID = 1L;

    @FastJoinQuery(thisTableAlias = "u", thisColumnName = "type_id", joinTableAlias = "t", joinColumnName = "id")
    private UserType userType;

    public UserType getUserType() {
        return userType;
    }

    public void setUserType(UserType userType) {
        this.userType = userType;
    }
}

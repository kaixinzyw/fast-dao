package com.fast.test;

import com.fast.mapper.TableAlias;
import com.fast.test.pojo.UserTypeInfo;

import java.io.Serializable;

/**
* 用户类型信息
*/
@TableAlias("user_type_info")
public class UserTypeInfoDTO extends UserTypeInfo implements Serializable {
    private static final long serialVersionUID = 1L;
}

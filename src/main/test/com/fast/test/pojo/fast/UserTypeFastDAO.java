package com.fast.test.pojo.fast;

import com.fast.base.BaseFastDAO;
import com.fast.condition.FastExample;
import com.fast.test.pojo.UserType;

import java.util.Date;

/**
* 用户类型
*/
public class UserTypeFastDAO extends BaseFastDAO<UserType, UserTypeFastDAO> {
    private static final long serialVersionUID = 1585326395866133296L;

    private UserTypeFastDAO(){super.fastExample=new FastExample<>(UserType.class,this);}
    public static UserTypeFastDAO create(){return new UserTypeFastDAO();}
    public static UserTypeFastDAO create(Object object) {return new UserTypeFastDAO().equalObject(object);}

    /**
    *主键
    */
    public UserTypeFastDAO id(Long id){return fastExample.field("id").valEqual(id);}
    public FastExample.FieldCriteria<UserType,UserTypeFastDAO> id(){return fastExample.field("id");}

    /**
    *用户名
    */
    public UserTypeFastDAO typeName(String typeName){return fastExample.field("typeName").valEqual(typeName);}
    public FastExample.FieldCriteria<UserType,UserTypeFastDAO> typeName(){return fastExample.field("typeName");}

    /**
    *创建时间
    */
    public UserTypeFastDAO createTime(Date createTime){return fastExample.field("createTime").valEqual(createTime);}
    public FastExample.FieldCriteria<UserType,UserTypeFastDAO> createTime(){return fastExample.field("createTime");}

    /**
    *更新时间
    */
    public UserTypeFastDAO updateTime(Date updateTime){return fastExample.field("updateTime").valEqual(updateTime);}
    public FastExample.FieldCriteria<UserType,UserTypeFastDAO> updateTime(){return fastExample.field("updateTime");}

    /**
    *是否删除
    */
    public UserTypeFastDAO deleted(Boolean deleted){return fastExample.field("deleted").valEqual(deleted);}
    public FastExample.FieldCriteria<UserType,UserTypeFastDAO> deleted(){return fastExample.field("deleted");}

}
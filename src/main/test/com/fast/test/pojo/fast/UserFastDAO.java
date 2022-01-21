package com.fast.test.pojo.fast;

import com.fast.base.BaseFastDAO;
import com.fast.condition.FastExample;
import com.fast.test.pojo.User;

import java.util.Date;

/**
* 用户
*/
public class UserFastDAO extends BaseFastDAO<User, UserFastDAO> {
    private static final long serialVersionUID = 4606434576374089267L;

    private UserFastDAO(){super.fastExample=new FastExample<>(User.class,this);}
    public static UserFastDAO create(){return new UserFastDAO();}
    public static UserFastDAO create(Object object) {return new UserFastDAO().equalObject(object);}

    /**
    *主键
    */
    public UserFastDAO id(Long id){return fastExample.field("id").valEqual(id);}
    public FastExample.FieldCriteria<User,UserFastDAO> id(){return fastExample.field("id");}

    /**
    *用户类型
    */
    public UserFastDAO typeId(Long typeId){return fastExample.field("typeId").valEqual(typeId);}
    public FastExample.FieldCriteria<User,UserFastDAO> typeId(){return fastExample.field("typeId");}

    /**
    *用户名
    */
    public UserFastDAO userName(String userName){return fastExample.field("userName").valEqual(userName);}
    public FastExample.FieldCriteria<User,UserFastDAO> userName(){return fastExample.field("userName");}

    /**
    *年龄
    */
    public UserFastDAO age(Integer age){return fastExample.field("age").valEqual(age);}
    public FastExample.FieldCriteria<User,UserFastDAO> age(){return fastExample.field("age");}

    /**
    *创建时间
    */
    public UserFastDAO createTime(Date createTime){return fastExample.field("createTime").valEqual(createTime);}
    public FastExample.FieldCriteria<User,UserFastDAO> createTime(){return fastExample.field("createTime");}

    /**
    *更新时间
    */
    public UserFastDAO updateTime(Date updateTime){return fastExample.field("updateTime").valEqual(updateTime);}
    public FastExample.FieldCriteria<User,UserFastDAO> updateTime(){return fastExample.field("updateTime");}

    /**
    *是否删除
    */
    public UserFastDAO deleted(Boolean deleted){return fastExample.field("deleted").valEqual(deleted);}
    public FastExample.FieldCriteria<User,UserFastDAO> deleted(){return fastExample.field("deleted");}

}
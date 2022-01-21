package com.fast.test.pojo.fast;

import com.fast.base.BaseFastDAO;
import com.fast.condition.FastExample;
import com.fast.test.pojo.UserTypeInfo;

import java.util.Date;

/**
* 用户类型信息
*/
public class UserTypeInfoFastDAO extends BaseFastDAO<UserTypeInfo, UserTypeInfoFastDAO> {
    private static final long serialVersionUID = -4849623278074106855L;

    private UserTypeInfoFastDAO(){super.fastExample=new FastExample<>(UserTypeInfo.class,this);}
    public static UserTypeInfoFastDAO create(){return new UserTypeInfoFastDAO();}
    public static UserTypeInfoFastDAO create(Object object) {return new UserTypeInfoFastDAO().equalObject(object);}

    /**
    *主键
    */
    public UserTypeInfoFastDAO id(Long id){return fastExample.field("id").valEqual(id);}
    public FastExample.FieldCriteria<UserTypeInfo,UserTypeInfoFastDAO> id(){return fastExample.field("id");}

    /**
    *用户类型ID
    */
    public UserTypeInfoFastDAO userTypeId(Long userTypeId){return fastExample.field("userTypeId").valEqual(userTypeId);}
    public FastExample.FieldCriteria<UserTypeInfo,UserTypeInfoFastDAO> userTypeId(){return fastExample.field("userTypeId");}

    /**
    *类型信息
    */
    public UserTypeInfoFastDAO typeInfo(String typeInfo){return fastExample.field("typeInfo").valEqual(typeInfo);}
    public FastExample.FieldCriteria<UserTypeInfo,UserTypeInfoFastDAO> typeInfo(){return fastExample.field("typeInfo");}

    /**
    *创建时间
    */
    public UserTypeInfoFastDAO createTime(Date createTime){return fastExample.field("createTime").valEqual(createTime);}
    public FastExample.FieldCriteria<UserTypeInfo,UserTypeInfoFastDAO> createTime(){return fastExample.field("createTime");}

    /**
    *更新时间
    */
    public UserTypeInfoFastDAO updateTime(Date updateTime){return fastExample.field("updateTime").valEqual(updateTime);}
    public FastExample.FieldCriteria<UserTypeInfo,UserTypeInfoFastDAO> updateTime(){return fastExample.field("updateTime");}

    /**
    *是否删除
    */
    public UserTypeInfoFastDAO deleted(Boolean deleted){return fastExample.field("deleted").valEqual(deleted);}
    public FastExample.FieldCriteria<UserTypeInfo,UserTypeInfoFastDAO> deleted(){return fastExample.field("deleted");}

}
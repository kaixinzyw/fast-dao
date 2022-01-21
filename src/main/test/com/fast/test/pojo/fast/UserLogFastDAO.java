package com.fast.test.pojo.fast;

import com.fast.base.BaseFastDAO;
import com.fast.condition.FastExample;
import com.fast.test.pojo.UserLog;

import java.util.Date;

/**
* 用户日志
*/
public class UserLogFastDAO extends BaseFastDAO<UserLog, UserLogFastDAO> {
    private static final long serialVersionUID = -6250092823755961834L;

    private UserLogFastDAO(){super.fastExample=new FastExample<>(UserLog.class,this);}
    public static UserLogFastDAO create(){return new UserLogFastDAO();}
    public static UserLogFastDAO create(Object object) {return new UserLogFastDAO().equalObject(object);}

    /**
    *主键
    */
    public UserLogFastDAO id(Long id){return fastExample.field("id").valEqual(id);}
    public FastExample.FieldCriteria<UserLog,UserLogFastDAO> id(){return fastExample.field("id");}

    /**
    *用户ID
    */
    public UserLogFastDAO userId(Long userId){return fastExample.field("userId").valEqual(userId);}
    public FastExample.FieldCriteria<UserLog,UserLogFastDAO> userId(){return fastExample.field("userId");}

    /**
    *日志内容
    */
    public UserLogFastDAO logInfo(String logInfo){return fastExample.field("logInfo").valEqual(logInfo);}
    public FastExample.FieldCriteria<UserLog,UserLogFastDAO> logInfo(){return fastExample.field("logInfo");}

    /**
    *创建时间
    */
    public UserLogFastDAO createTime(Date createTime){return fastExample.field("createTime").valEqual(createTime);}
    public FastExample.FieldCriteria<UserLog,UserLogFastDAO> createTime(){return fastExample.field("createTime");}

    /**
    *更新时间
    */
    public UserLogFastDAO updateTime(Date updateTime){return fastExample.field("updateTime").valEqual(updateTime);}
    public FastExample.FieldCriteria<UserLog,UserLogFastDAO> updateTime(){return fastExample.field("updateTime");}

    /**
    *是否删除
    */
    public UserLogFastDAO deleted(Boolean deleted){return fastExample.field("deleted").valEqual(deleted);}
    public FastExample.FieldCriteria<UserLog,UserLogFastDAO> deleted(){return fastExample.field("deleted");}

}
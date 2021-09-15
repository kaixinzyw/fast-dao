package com.fast.utils;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpStatus;

import java.io.Serializable;

/**
 * 通用返回对象
 * @author zyw
 */
public class FastResult<T>  implements Serializable{
	

	private int code;
    
    private String message;
    
    private T data;

    protected FastResult() {
    }

    protected FastResult(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }
    
    public Boolean isSuccess() {
    	return  HttpStatus.HTTP_OK==code ? Boolean.TRUE : Boolean.FALSE;
    }

    public static <T> FastResult<T> success(T data) {
        return new FastResult<T>(HttpStatus.HTTP_OK, StrUtil.EMPTY, data);
    }
    

    public static <T> FastResult<T> successMsg(String msg) {
        return new FastResult<T>(HttpStatus.HTTP_OK, msg, null);
    }


    public static <T> FastResult<T> success(T data, String message) {
        return new FastResult<T>(HttpStatus.HTTP_OK, message, data);
    }

    public static <T> FastResult<T> failure(T data) {
        return new FastResult<T>(HttpStatus.HTTP_INTERNAL_ERROR, StrUtil.EMPTY, data);
    }


    public static <T> FastResult<T> failureMsg(String msg) {
        return new FastResult<T>(HttpStatus.HTTP_INTERNAL_ERROR, msg, null);
    }


    public static <T> FastResult<T> failure(T data, String message) {
        return new FastResult<T>(HttpStatus.HTTP_INTERNAL_ERROR, message, data);
    }


    public static <T> FastResult<T> customize(int code, T data) {
        return new FastResult<T>(code, StrUtil.EMPTY, data);
    }


    public static <T> FastResult<T> customizeMsg(int code, String msg) {
        return new FastResult<T>(code, msg, null);
    }


    public static <T> FastResult<T> customize(int code, T data, String message) {
        return new FastResult<T>(code, message, data);
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}

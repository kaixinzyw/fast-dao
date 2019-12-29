package com.fast.utils;

import cn.hutool.http.HttpStatus;

import java.io.Serializable;

/**
 * @author 张亚伟 https://github.com/kaixinzyw
 */
public class FastResult<T> implements Serializable {

    private static final long serialVersionUID = 6330060397747273715L;

    private int code = HttpStatus.HTTP_OK;
    private String msg;
    private T data;

    public static FastResult error(String msg) {
        FastResult result = new FastResult();
        result.setCode(HttpStatus.HTTP_INTERNAL_ERROR);
        result.setMsg(msg);
        return result;
    }

    public static <T>FastResult<T> error(T data) {
        FastResult<T> result = new FastResult<T>();
        result.setCode(HttpStatus.HTTP_INTERNAL_ERROR);
        result.setData(data);
        return result;
    }

    public static <T>FastResult<T> error(T data, String msg) {
        FastResult<T> result = new FastResult<T>();
        result.setCode(HttpStatus.HTTP_INTERNAL_ERROR);
        result.setData(data);
        result.setMsg(msg);
        return result;
    }

    public static FastResult success(String msg) {
        FastResult result = new FastResult();
        result.setCode(HttpStatus.HTTP_OK);
        result.setMsg(msg);
        return result;
    }

    public static <T>FastResult<T> success(T data) {
        FastResult<T> result = new FastResult<T>();
        result.setCode(HttpStatus.HTTP_OK);
        result.setData(data);
        return result;
    }

    public static <T>FastResult<T> success(T data, String msg) {
        FastResult<T> result = new FastResult<T>();
        result.setCode(HttpStatus.HTTP_OK);
        result.setData(data);
        result.setMsg(msg);
        return result;
    }

    public static FastResult customize(int code) {
        FastResult result = new FastResult();
        result.setCode(code);
        return result;
    }

    public static FastResult customize(int code, String msg) {
        FastResult result = new FastResult();
        result.setCode(code);
        result.setMsg(msg);
        return result;
    }



    public static <T>FastResult<T> customize(int code, T data) {
        FastResult<T> result = new FastResult<T>();
        result.setCode(code);
        result.setData(data);
        return result;
    }

    public static <T>FastResult<T> customize(int code, T data, String msg) {
        FastResult<T> result = new FastResult<T>();
        result.setCode(code);
        result.setData(data);
        result.setMsg(msg);
        return result;
    }

    public FastResult() {
    }

    public FastResult(int code) {
        this.code = code;
    }

    public FastResult(int code, T data) {
        this.code = code;
        this.data = data;
    }

    public FastResult(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public FastResult(int code, T data, String msg) {
        this.code = code;
        this.data = data;
        this.msg = msg;
    }



    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}

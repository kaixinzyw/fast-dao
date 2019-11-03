package com.fast.utils;

import cn.hutool.http.HttpStatus;

import java.io.Serializable;

/**
 * @author 张亚伟 https://github.com/kaixinzyw
 */
public class FastResponse<T> implements Serializable {

    private static final long serialVersionUID = 6330060397747273715L;

    private int code = HttpStatus.HTTP_OK;
    private String msg;
    private T data;

    public static FastResponse error(String msg) {
        FastResponse result = new FastResponse();
        result.setCode(HttpStatus.HTTP_INTERNAL_ERROR);
        result.setMsg(msg);
        return result;
    }

    public static FastResponse error(int code, String msg) {
        FastResponse result = new FastResponse();
        result.setMsg(msg);
        result.setCode(code);
        return result;
    }

    public FastResponse() {
    }

    public FastResponse(T data) {
        this.data = data;
    }

    public FastResponse(T data, String errMsg) {
        if (data == null) {
            this.code = HttpStatus.HTTP_INTERNAL_ERROR;
            this.msg = errMsg;
        } else {
            this.data = data;
        }
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

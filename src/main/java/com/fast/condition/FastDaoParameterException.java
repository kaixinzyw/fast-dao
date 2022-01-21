package com.fast.condition;

/**
 * 参数异常
 * @author 张亚伟 https://github.com/kaixinzyw
 */
public class FastDaoParameterException extends RuntimeException {
    private static final long serialVersionUID = 8270327595554777527L;

    public FastDaoParameterException(String message) {
        super(message);
    }
}

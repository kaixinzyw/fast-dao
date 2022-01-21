package com.fast.condition;

/**
 * 参数异常
 * @author 张亚伟 https://github.com/kaixinzyw
 */
public class FastDaoException extends RuntimeException {
    private static final long serialVersionUID = 6013423359959125123L;

    public FastDaoException(String message) {
        super(message);
    }
}

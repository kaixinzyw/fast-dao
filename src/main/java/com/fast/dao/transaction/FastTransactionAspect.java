package com.fast.dao.transaction;


import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

/**
 * 事务拦截器
 *
 * @author zyw
 */
@Aspect
@Component
public class FastTransactionAspect {

    @Pointcut("@annotation(com.fast.dao.transaction.FastTransactionAuto)")
    public void pointCut() {
    }


    /**
     * 方法执行前,开启事务
     */
    @Before("pointCut()")
    public void autoOpenTransactionBefore() {
        FastTransaction.open();
    }

    /**
     * 方法执行后,提交事务
     */
    @After("pointCut()")
    public void autoCommitTransactionAfter() {
        FastTransaction.commit();
    }

    /**
     * 方法异常后,回滚事务
     */
    @AfterThrowing("pointCut()")
    public void autoRollbackTransactionAfterThrowing(){
        FastTransaction.rollback();
    }

}

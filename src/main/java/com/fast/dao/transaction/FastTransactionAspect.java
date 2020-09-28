package com.fast.dao.transaction;


import cn.hutool.core.util.StrUtil;
import com.fast.dao.jdbc.TransactionInfo;
import io.netty.util.concurrent.FastThreadLocal;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 事务拦截器
 *
 * @author zyw
 */
@Aspect
@Component
public class FastTransactionAspect {

    private static final FastThreadLocal<String> transactionMethodNameThreadLocal = new FastThreadLocal<>();


    @Pointcut("@annotation(com.fast.dao.transaction.FastTransactionAuto)")
    public void pointCut() {
    }


    /**
     * 方法执行前,开启事务
     */
    @Before("pointCut()")
    public void autoOpenTransactionBefore(JoinPoint point) {
        String threadLocalMethodName = transactionMethodNameThreadLocal.get();
        if (threadLocalMethodName != null) {
            return;
        }
        threadLocalMethodName = point.getSignature().getName();
        transactionMethodNameThreadLocal.set(threadLocalMethodName);
        FastTransaction.open();
    }

    /**
     * 方法执行后,提交事务
     */
    @After("pointCut()")
    public void autoCommitTransactionAfter(JoinPoint point) {
        String threadLocalMethodName = transactionMethodNameThreadLocal.get();
        if (StrUtil.equals(point.getSignature().getName(), threadLocalMethodName)) {
            FastTransaction.commit();
        }
    }

    /**
     * 方法异常后,回滚事务
     */
    @AfterThrowing("pointCut()")
    public void autoRollbackTransactionAfterThrowing(JoinPoint point) {
        String threadLocalMethodName = transactionMethodNameThreadLocal.get();
        if (StrUtil.equals(point.getSignature().getName(), threadLocalMethodName)) {
            FastTransaction.rollback();
        }
    }

}

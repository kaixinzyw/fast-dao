package com.fast.dao.transaction;


import cn.hutool.core.text.StrBuilder;
import cn.hutool.core.util.StrUtil;
import io.netty.util.concurrent.FastThreadLocal;
import org.aspectj.lang.JoinPoint;
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

    private static final FastThreadLocal<StrBuilder> transactionMethodNameThreadLocal = new FastThreadLocal<>();


    @Pointcut("@annotation(com.fast.dao.transaction.FastAutoTransaction)")
    public void pointCut() {
    }


    /**
     * 方法执行前,开启事务
     * @param point 点
     */
    @Before("pointCut()")
    public void autoOpenTransactionBefore(JoinPoint point) {
        StrBuilder path = transactionMethodNameThreadLocal.get();
        if (path != null) {
            return;
        }
        path = StrUtil.strBuilder(point.getSignature().getDeclaringTypeName(), StrUtil.DOT, point.getSignature().getName());
        transactionMethodNameThreadLocal.set(path);
        FastTransaction.open();
    }

    /**
     * 方法执行后,提交事务
     * @param point 点
     */
    @After("pointCut()")
    public void autoCommitTransactionAfter(JoinPoint point) {
        StrBuilder threadLocalMethodName = transactionMethodNameThreadLocal.get();
        if (threadLocalMethodName == null) {
            return;
        }
        StrBuilder path = StrUtil.strBuilder(point.getSignature().getDeclaringTypeName(), StrUtil.DOT, point.getSignature().getName());
        if (StrUtil.equals(threadLocalMethodName.toString(), path.toString())) {
            FastTransaction.commit();
            transactionMethodNameThreadLocal.remove();
        }
    }

    /**
     * 方法异常后,回滚事务
     * @param point 点
     */
    @AfterThrowing("pointCut()")
    public void autoRollbackTransactionAfterThrowing(JoinPoint point) {
        StrBuilder threadLocalMethodName = transactionMethodNameThreadLocal.get();
        if (threadLocalMethodName == null) {
            return;
        }
        StrBuilder path = StrUtil.strBuilder(point.getSignature().getDeclaringTypeName(), StrUtil.DOT, point.getSignature().getName());
        if (StrUtil.equals(path.toString(), threadLocalMethodName.toString())) {
            FastTransaction.rollback();
            transactionMethodNameThreadLocal.remove();
        }
    }

}

package com.fast.dao.transaction;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.IdUtil;
import com.fast.config.FastDaoAttributes;
import com.fast.dao.jdbc.TransactionInfo;
import io.netty.util.concurrent.FastThreadLocal;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import java.util.HashMap;
import java.util.Map;

/**
 * 手动事务管理
 *
 * @author 张亚伟 https://github.com/kaixinzyw
 */
public class FastTransaction {

    private static final String TRANSACTION_SYNCHRONIZATION_NAME = "SYNCHRONIZATION_NEVER";
    private static final FastThreadLocal<Map<String, TransactionInfo>> tranThreadLocalMap = new FastThreadLocal<>();

    /**
     * 开启事务
     */
    public static void open() {
        TransactionInfo transaction = getTransactionInfo();
        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        transaction.setStatus(transaction.getTran().getTransaction(def));
    }

    /**
     * 提交手动事务
     */
    public static void commit() {
        Map<String, TransactionInfo> transactionInfoMap = tranThreadLocalMap.get();
        if (CollUtil.isEmpty(transactionInfoMap)) {
            return;
        }
        for (String key : transactionInfoMap.keySet()) {
            TransactionInfo transaction = transactionInfoMap.get(key);
            transaction.getTran().commit(transaction.getStatus());
        }
        tranThreadLocalMap.remove();
    }

    /**
     * 回滚手动事务
     */
    public static void rollback() {
        Map<String, TransactionInfo> transactionInfoMap = tranThreadLocalMap.get();
        if (CollUtil.isEmpty(transactionInfoMap)) {
            return;
        }
        for (String key : transactionInfoMap.keySet()) {
            TransactionInfo transaction = transactionInfoMap.get(key);
            transaction.getTran().rollback(transaction.getStatus());
            transaction.setStatus(null);
        }
        tranThreadLocalMap.remove();
    }

    /**
     * 切换数据源时系统自动调用
     */
    public static void switchTransaction() {
        Map<String, TransactionInfo> transactionInfoMap = tranThreadLocalMap.get();
        if (CollUtil.isNotEmpty(transactionInfoMap)) {
            open();
        }
    }

    private static TransactionInfo getTransactionInfo() {
        Map<String, TransactionInfo> transactionInfoMap = tranThreadLocalMap.get();
        if (transactionInfoMap == null) {
            transactionInfoMap = new HashMap<>();
            tranThreadLocalMap.set(transactionInfoMap);
        }
        TransactionInfo transactionInfo = new TransactionInfo();
        DataSourceTransactionManager transactionManager = new DataSourceTransactionManager(FastDaoAttributes.getDataSource());
        transactionManager.setTransactionSynchronizationName(TRANSACTION_SYNCHRONIZATION_NAME);
        transactionInfo.setTran(transactionManager);
        transactionInfoMap.put(IdUtil.fastSimpleUUID(), transactionInfo);
        return transactionInfo;
    }
}

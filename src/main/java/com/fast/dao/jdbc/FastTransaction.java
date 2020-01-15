package com.fast.dao.jdbc;

import com.fast.config.FastDaoAttributes;
import io.netty.util.concurrent.FastThreadLocal;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.support.DefaultTransactionDefinition;

/**
 * 手动事务管理
 * @author 张亚伟 https://github.com/kaixinzyw
 */
public class FastTransaction {

    private static final FastThreadLocal<TransactionInfo> tranThreadLocal = new FastThreadLocal<>();

    /**
     * 开启手动事务
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
        TransactionInfo transaction = tranThreadLocal.get();
        if (transaction == null || transaction.getStatus() == null) {
            throw new RuntimeException("事务未开启,不能进行事务提交");
        }
        transaction.getTran().commit(transaction.getStatus());
        transaction.setStatus(null);
    }

    /**
     * 回滚手动事务
     */
    public static void rollback() {
        TransactionInfo transaction = tranThreadLocal.get();
        if (transaction == null || transaction.getStatus() == null) {
            throw new RuntimeException("事务未开启,不能进行事务回滚");
        }
        transaction.getTran().rollback(transaction.getStatus());
        transaction.setStatus(null);
    }


    private static TransactionInfo getTransactionInfo() {
        TransactionInfo transactionInfo = tranThreadLocal.get();
        if (transactionInfo == null) {
            transactionInfo = new TransactionInfo();
            NamedParameterJdbcTemplate jdbcTemplate = JdbcConnection.getJdbcTemplate();
            if (jdbcTemplate != null) {
                transactionInfo.setTran(new DataSourceTransactionManager(jdbcTemplate.getJdbcTemplate().getDataSource()));
            } else {
                transactionInfo.setTran(new DataSourceTransactionManager(FastDaoAttributes.getDataSource()));
            }
            tranThreadLocal.set(transactionInfo);
        }
        return transactionInfo;
    }
}

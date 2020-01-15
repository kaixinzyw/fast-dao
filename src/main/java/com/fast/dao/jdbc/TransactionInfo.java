package com.fast.dao.jdbc;

import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.TransactionStatus;

/**
 * @author 张亚伟 https://github.com/kaixinzyw
 */
public class TransactionInfo {

    private DataSourceTransactionManager tran;
    private TransactionStatus status;

    public DataSourceTransactionManager getTran() {
        return tran;
    }

    public void setTran(DataSourceTransactionManager tran) {
        this.tran = tran;
    }

    public TransactionStatus getStatus() {
        return status;
    }

    public void setStatus(TransactionStatus status) {
        this.status = status;
    }
}

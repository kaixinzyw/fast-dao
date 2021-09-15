package com.fast.test;

import com.alibaba.druid.pool.DruidDataSource;


import javax.sql.DataSource;

public class TransactionTest {

    private static DataSource getDataSource1() {
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setUrl("jdbc:mysql://127.0.0.1:3306/fast-test?useUnicode=true&characterEncoding=utf-8&useSSL=false&serverTimezone=UTC");
        dataSource.setUsername("root");
        dataSource.setPassword("kaixinzyw");
        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        return dataSource;
    }

    private static DataSource getDataSource2() {
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setUrl("jdbc:mysql://127.0.0.1:3306/my_test?useUnicode=true&characterEncoding=utf-8&useSSL=false&serverTimezone=UTC");
        dataSource.setUsername("root");
        dataSource.setPassword("kaixinzyw");
        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        return dataSource;
    }

    public static void main(String[] args) {
//        FastUserTest user = new FastUserTest();
//        user.setUserName("FastDao");
//        FastDaoConfig.dataSourceThreadLocal(getDataSource1());
//        FastTransaction.open();
//
//        FastUserTestFastDAO.create().dao().insert(user);
//        FastDaoConfig.dataSourceThreadLocal(getDataSource2());
//        user.setId(user.getId()+1);
//        FastUserTestFastDAO.create().dao().insert(user);
//        FastTransaction.commit();

    }


}

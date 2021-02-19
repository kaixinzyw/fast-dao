package com.fast.demo;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.fastjson.JSONObject;
import com.fast.config.FastDaoConfig;
import com.fast.demo.pojo.Brand;
import com.fast.demo.pojo.fast.BrandFastDAO;

import javax.sql.DataSource;
import java.util.List;

public class ToManyTest {

    static {
        //加载配置文件
        FastSetConfigTest.fastDaoConfig();
    }

    private static DataSource getDataSource1() {
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setUrl("jdbc:mysql://127.0.0.1:3306/my_test?useUnicode=true&characterEncoding=utf-8&useSSL=false&serverTimezone=UTC");
        dataSource.setUsername("root");
        dataSource.setPassword("kaixinzyw");
        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        return dataSource;
    }
    public static void main(String[] args) {
        FastDaoConfig.dataSourceThreadLocal(getDataSource1());
        List<Brand> list = BrandFastDAO.create().dao().findPage(1,4).getList();
        System.out.println(JSONObject.toJSONString(list,true));
    }

}

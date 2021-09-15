package com.fast.condition;

public class OrderByQuery {

    /**
     * 排序字段名
     */
    private String orderByName;

    /**
     * 是否降序
     */
    private Boolean isDesc;

    public OrderByQuery(String orderByName, Boolean isDesc) {
        this.orderByName = orderByName;
        this.isDesc = isDesc;
    }

    public String getOrderByName() {
        return orderByName;
    }

    public void setOrderByName(String orderByName) {
        this.orderByName = orderByName;
    }

    public Boolean getDesc() {
        return isDesc;
    }

    public void setDesc(Boolean desc) {
        isDesc = desc;
    }
}

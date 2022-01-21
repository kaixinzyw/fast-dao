package com.fast.aspect;

public enum ExpanderOccasion {
    /**
     * 执行方法 SELECT与COUNT同属查询
     */
    INSERT("insert"), SELECT("select"), COUNT("count"), UPDATE("update"), DELETE("delete");
    public String method;

    ExpanderOccasion(String method) {
        this.method = method;
    }
}

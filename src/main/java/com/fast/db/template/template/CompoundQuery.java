package com.fast.db.template.template;


import java.util.*;

/**
 * 查询条件封装
 * @author 张亚伟 398850094@qq.com
 */
public class CompoundQuery {

    private CompoundQuery or;


    private Object equalObject;

    /**
     * 查询特定字段
     */
    private Set<String> showFields;

    /**
     * 屏蔽特定字段
     */
    private Set<String> hideFields;

    /**
     * 去重字段
     */
    private Set<String> distinctFields;

    /**
     * 范围查询
     */
    private List<MapperQueryObject.RangeQuery> rangeQuery;

    /**
     * 排序
     */
    private List<MapperQueryObject.OrderByQuery> orderByQuery;

    /**
     * IN查询
     */
    private List<MapperQueryObject.InQuery> inQuery;

    /**
     * 字段不为空查询
     */
    private Collection<String> notNullFieldsQuery;

    /**
     * 字段为空查询
     */
    private Collection<String> nullFieldsQuery;

    /**
     * 字段等于查询
     */
    private List<MapperQueryObject.FieldQuery> equalFieldQuery;

    /**
     * 字段大于等于查询
     */
    private List<MapperQueryObject.FieldQuery> greaterOrEqualFieldsQuery;

    /**
     * 字段小于等于查询
     */
    private List<MapperQueryObject.FieldQuery> lessOrEqualFieldsQuery;

    /**
     * 字段大于等于查询
     */
    private List<MapperQueryObject.FieldQuery> greaterFieldsQuery;

    /**
     * 字段小于等于查询
     */
    private List<MapperQueryObject.FieldQuery> lessFieldsQuery;

    /**
     * 字段全模糊查询
     */
    private List<MapperQueryObject.FieldQuery> likeQuery;

    /**
     * 添加and条件后自定义SQL
     */
    private List<String> andSql;

    private Integer page;
    private Integer size;

    public Object getEqualObject() {
        return equalObject;
    }

    public void setEqualObject(Object equalObject) {
        this.equalObject = equalObject;
    }

    public Set<String> getShowFields() {
        return showFields;
    }

    public void setShowFields(Set<String> showFields) {
        this.showFields = showFields;
    }

    public void addShowField(String showField) {
        if (this.showFields != null) {
            this.showFields.add(showField);
        } else {
            this.showFields = new HashSet<>();
            this.showFields.add(showField);
        }
    }

    public Set<String> getHideFields() {
        return hideFields;
    }

    public void setHideFields(Set<String> hideFields) {
        this.hideFields = hideFields;
    }

    public void addHideField(String hideField) {
        if (this.hideFields != null) {
            this.hideFields.add(hideField);
        } else {
            this.hideFields = new HashSet<>();
            this.hideFields.add(hideField);
        }
    }

    public Set<String> getDistinctFields() {
        return distinctFields;
    }

    public void setDistinctFields(Set<String> distinctFields) {
        this.distinctFields = distinctFields;
    }

    public void addDistinctField(String distinctField) {
        if (this.distinctFields != null) {
            this.distinctFields.add(distinctField);
        } else {
            this.distinctFields = new HashSet<>();
            this.distinctFields.add(distinctField);
        }
    }

    public List<MapperQueryObject.RangeQuery> getRangeQuery() {
        return rangeQuery;
    }

    public void setRangeQuery(List<MapperQueryObject.RangeQuery> rangeQuery) {
        this.rangeQuery = rangeQuery;
    }

    public void addRangeQuery(String rangeName, Object rangeMin, Object rangeMax) {
        if (rangeQuery != null) {
            this.rangeQuery.add(new MapperQueryObject.RangeQuery(rangeName, rangeMin, rangeMax));
        } else {
            this.rangeQuery = new ArrayList<>(Arrays.asList(new MapperQueryObject.RangeQuery(rangeName, rangeMin, rangeMax)));
        }
    }

    public List<MapperQueryObject.OrderByQuery> getOrderByQuery() {
        return orderByQuery;
    }

    public void setOrderByQuery(List<MapperQueryObject.OrderByQuery> orderByQuery) {
        this.orderByQuery = orderByQuery;
    }

    public void addOrderByQuery(String orderByName, Boolean isDesc) {
        if (this.orderByQuery != null) {
            this.orderByQuery.add(new MapperQueryObject.OrderByQuery(orderByName, isDesc));
        } else {
            this.orderByQuery = new ArrayList<>(Arrays.asList(new MapperQueryObject.OrderByQuery(orderByName, isDesc)));
        }
    }

    public List<MapperQueryObject.InQuery> getInQuery() {
        return inQuery;
    }

    public void setInQuery(List<MapperQueryObject.InQuery> inQuery) {
        this.inQuery = inQuery;
    }

    public void addInQuery(String inName, Collection inValues) {

        if (this.inQuery != null) {
            this.inQuery.add(new MapperQueryObject.InQuery(inName, inValues));
        } else {
            this.inQuery = new ArrayList<>();
            inQuery.add(new MapperQueryObject.InQuery(inName, inValues));
        }
    }

    public void addInQuery(String inName, Object... inValues) {

        if (this.inQuery != null) {
            this.inQuery.add(new MapperQueryObject.InQuery(inName, new ArrayList<>(Arrays.asList(inValues))));
        } else {
            this.inQuery = new ArrayList<>(Arrays.asList(new MapperQueryObject.InQuery(inName, new ArrayList<>(Arrays.asList(inValues)))));
        }
    }



    public Collection<String> getNotNullFieldsQuery() {
        return notNullFieldsQuery;
    }

    public void setNotNullFieldsQuery(Collection<String> notNullFieldsQuery) {
        this.notNullFieldsQuery = notNullFieldsQuery;
    }

    public void addNotNullFieldsQuery(String... notNullFields) {
        if (this.notNullFieldsQuery != null) {
            this.notNullFieldsQuery.addAll(new HashSet<>(Arrays.asList(notNullFields)));
        } else {
            this.notNullFieldsQuery = new HashSet<>(Arrays.asList(notNullFields));
        }
    }

    public void addNotNullFieldsQuery(Collection<String> notNullFields) {
        if (this.notNullFieldsQuery != null) {
            this.notNullFieldsQuery.addAll(notNullFields);
        } else {
            this.notNullFieldsQuery = new HashSet<>(notNullFields);
        }
    }

    public Collection<String> getNullFieldsQuery() {
        return nullFieldsQuery;
    }

    public void setNullFieldsQuery(Collection<String> nullFieldsQuery) {
        this.nullFieldsQuery = nullFieldsQuery;
    }

    public void addNullFieldsQuery(String... nullFields) {
        if (this.nullFieldsQuery != null) {
            this.nullFieldsQuery.addAll(new HashSet<>(Arrays.asList(nullFields)));
        } else {
            this.nullFieldsQuery = new HashSet<>(Arrays.asList(nullFields));
        }
    }

    public void addNullFieldsQuery(Collection<String> nullFields) {
        if (this.nullFieldsQuery != null) {
            this.nullFieldsQuery.addAll(nullFields);
        } else {
            this.nullFieldsQuery = new HashSet<>(nullFields);
        }
    }

    public List<MapperQueryObject.FieldQuery> getEqualFieldQuery() {
        return equalFieldQuery;
    }

    public void setEqualFieldQuery(List<MapperQueryObject.FieldQuery> equalFieldQuery) {
        this.equalFieldQuery = equalFieldQuery;
    }

    public void addEqualFieldQuery(String fieldName, Object value) {
        if (this.equalFieldQuery != null) {
            this.equalFieldQuery.add(new MapperQueryObject.FieldQuery(fieldName, value));
        } else {
            this.equalFieldQuery = new ArrayList<>(Arrays.asList(new MapperQueryObject.FieldQuery(fieldName, value)));
        }
    }

    public List<MapperQueryObject.FieldQuery> getGreaterOrEqualFieldsQuery() {
        return greaterOrEqualFieldsQuery;
    }

    public void setGreaterOrEqualFieldsQuery(List<MapperQueryObject.FieldQuery> greaterOrEqualFieldsQuery) {
        this.greaterOrEqualFieldsQuery = greaterOrEqualFieldsQuery;
    }

    public void addGreaterOrEqualFieldsQuery(String fieldName, Object value) {
        if (this.greaterOrEqualFieldsQuery != null) {
            this.greaterOrEqualFieldsQuery.add(new MapperQueryObject.FieldQuery(fieldName, value));
        } else {
            this.greaterOrEqualFieldsQuery = new ArrayList<>(Arrays.asList(new MapperQueryObject.FieldQuery(fieldName, value)));
        }
    }

    public List<MapperQueryObject.FieldQuery> getLessOrEqualFieldsQuery() {
        return lessOrEqualFieldsQuery;
    }

    public void setLessOrEqualFieldsQuery(List<MapperQueryObject.FieldQuery> lessOrEqualFieldsQuery) {
        this.lessOrEqualFieldsQuery = lessOrEqualFieldsQuery;
    }

    public void addLessOrEqualFieldsQuery(String fieldName, Object value) {
        if (this.lessOrEqualFieldsQuery != null) {
            this.lessOrEqualFieldsQuery.add(new MapperQueryObject.FieldQuery(fieldName, value));
        } else {
            this.lessOrEqualFieldsQuery = new ArrayList<>(Arrays.asList(new MapperQueryObject.FieldQuery(fieldName, value)));
        }
    }


    public List<MapperQueryObject.FieldQuery> getGreaterFieldsQuery() {
        return greaterFieldsQuery;
    }

    public void setGreaterFieldsQuery(List<MapperQueryObject.FieldQuery> greaterFieldsQuery) {
        this.greaterFieldsQuery = greaterFieldsQuery;
    }

    public void addGreaterFieldsQuery(String fieldName, Object value) {
        if (this.greaterFieldsQuery != null) {
            this.greaterFieldsQuery.add(new MapperQueryObject.FieldQuery(fieldName, value));
        } else {
            this.greaterFieldsQuery = new ArrayList<>(Arrays.asList(new MapperQueryObject.FieldQuery(fieldName, value)));
        }
    }


    public List<MapperQueryObject.FieldQuery> getLessFieldsQuery() {
        return lessFieldsQuery;
    }

    public void setLessFieldsQuery(List<MapperQueryObject.FieldQuery> lessFieldsQuery) {
        this.lessFieldsQuery = lessFieldsQuery;
    }

    public void addLessFieldsQuery(String fieldName, Object value) {
        if (this.lessFieldsQuery != null) {
            this.lessFieldsQuery.add(new MapperQueryObject.FieldQuery(fieldName, value));
        } else {
            this.lessFieldsQuery = new ArrayList<>(Arrays.asList(new MapperQueryObject.FieldQuery(fieldName, value)));
        }
    }

    public List<MapperQueryObject.FieldQuery> getLikeQuery() {
        return likeQuery;
    }

    public void setLikeQuery(List<MapperQueryObject.FieldQuery> likeQuery) {
        this.likeQuery = likeQuery;
    }

    public void addLikeQuery(String fieldName, Object value) {
        if (this.likeQuery != null) {
            this.likeQuery.add(new MapperQueryObject.FieldQuery(fieldName, value));
        } else {
            this.likeQuery = new ArrayList<>(Arrays.asList(new MapperQueryObject.FieldQuery(fieldName, value)));
        }
    }

    public List<String> getAndSql() {
        return andSql;
    }

    public void setAndSql(List<String> andSql) {
        this.andSql = andSql;
    }

    public void addAndSql(String... andSql) {
        if (this.andSql != null) {
            this.andSql.addAll(new ArrayList<>(Arrays.asList(andSql)));
        } else {
            this.andSql = new ArrayList<>(Arrays.asList(andSql));
        }
    }

    public void addAndSql(List<String> andSql) {
        if (this.andSql != null) {
            this.andSql.addAll(andSql);
        } else {
            this.andSql = new ArrayList<>(andSql);
        }
    }

    public CompoundQuery getOr() {
        return or;
    }

    public void setOr(CompoundQuery or) {
        this.or = or;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }
}

package com.fast.condition;

import com.fast.mapper.TableMapper;
import com.fast.mapper.TableMapperUtil;

import java.io.Serializable;
import java.util.Map;

/**
 * 自定义列更新
 * 注意,同一列操作不能进行多次操作,多次操作只会最后一次生效
 *
 * @author 张亚伟 https://github.com/kaixinzyw
 */
public class CustomizeUpdate<P,T> implements Serializable {

    private static final long serialVersionUID = -848871486184328929L;
    /**
     * 条件封装
     */
    private final FastExample<P,T> fastExample;
    /**
     * 字段名
     */
    private final String fieldName;
    /**
     * 更新列
     */
    private final String tableColumnName;

    public CustomizeUpdate(Class<P> pojoClass, FastExample<P,T> fastExample, String fieldName) {
        this.fastExample = fastExample;
        this.fieldName = fieldName;
        TableMapper tableMapper = TableMapperUtil.getTableMappers(pojoClass);
        this.tableColumnName = tableMapper.getShowTableNames().get(fieldName);
    }

    /**
     * this加value加法运算
     * tableColumnName = tableColumnName + val
     * 如:thisAdd(10) 则 tableColumnName = tableColumnName + 10
     * 可以使用${参数名}  new HashMap(参数名,数据) 进行占位
     *
     * @param val  参数
     * @param data 条件
     * @return 条件封装
     */
    public FastExample.FieldCriteria<P,T> thisAdd(Object val, Map<String, Object> data) {
        fastExample.conditionPackages().addCustomUpdateColumns(fieldName, new CustomizeUpdateData(fieldName, tableColumnName + " + " + val, data));
        return fastExample.field(fieldName);
    }


    /**
     * 加法运算
     * @param value  参数
     * @return 条件封装
     */
    public FastExample.FieldCriteria<P,T> thisAdd(Number value) {
        return thisAdd(value,null);
    }

    /**
     * this加value减法运算
     * tableColumnName = tableColumnName - val
     * 如:thisMinus(10) 则 tableColumnName = tableColumnName - 10
     * 可以使用${参数名}  new HashMap(参数名,数据) 进行占位
     *
     * @param val  参数
     * @param data 条件
     * @return 条件封装
     */
    public FastExample.FieldCriteria<P,T> thisSbu(Object val, Map<String, Object> data) {
        fastExample.conditionPackages().addCustomUpdateColumns(fieldName, new CustomizeUpdateData(fieldName, tableColumnName + " - " + val, data));
        return fastExample.field(fieldName);
    }

    /**
     * 减法运算
     * @param value  参数
     * @return 条件封装
     */
    public FastExample.FieldCriteria<P,T> thisSbu(Number value) {
        return thisSbu(value,null);
    }

    /**
     * this加value乘法运算
     * tableColumnName = tableColumnName * val
     * 如:thisMinus(10) 则 tableColumnName = tableColumnName * 10
     * 可以使用${参数名}  new HashMap(参数名,数据) 进行占位
     *
     * @param val  参数
     * @param data 条件
     * @return 条件封装
     */
    public FastExample.FieldCriteria<P,T> thisMul(Object val, Map<String, Object> data) {
        fastExample.conditionPackages().addCustomUpdateColumns(fieldName, new CustomizeUpdateData(fieldName, tableColumnName + " * " + val, data));
        return fastExample.field(fieldName);
    }

    /**
     * 乘法运算
     * @param value  参数
     * @return 条件封装
     */
    public FastExample.FieldCriteria<P,T> thisMul(Number value) {
        return thisMul(value,null);
    }
    /**
     * this加value除法运算
     * tableColumnName = tableColumnName / val
     * 如:thisMinus(10) 则 tableColumnName = tableColumnName / 10
     * 可以使用${参数名}  new HashMap(参数名,数据) 进行占位
     *
     * @param val  参数
     * @param data 条件
     * @return 条件封装
     */
    public FastExample.FieldCriteria<P,T> thisDiv(Object val, Map<String, Object> data) {
        fastExample.conditionPackages().addCustomUpdateColumns(fieldName, new CustomizeUpdateData(fieldName, tableColumnName + " / " + val, data));
        return fastExample.field(fieldName);
    }
    /**
     * 除法运算
     * @param value  参数
     * @return 条件封装
     */
    public FastExample.FieldCriteria<P,T> thisDiv(Number value) {
        return thisDiv(value,null);
    }
    /**
     * this加value取模运算
     * tableColumnName = tableColumnName % val
     * 如:thisMinus(10) 则 tableColumnName = tableColumnName % 10
     * 可以使用${参数名}  new HashMap(参数名,数据) 进行占位
     *
     * @param val  参数
     * @param data 条件
     * @return 条件封装
     */
    public FastExample.FieldCriteria<P,T> thisModulo(Object val, Map<String, Object> data) {
        fastExample.conditionPackages().addCustomUpdateColumns(fieldName, new CustomizeUpdateData(fieldName, tableColumnName + " % " + val, data));
        return fastExample.field(fieldName);
    }
    /**
     * 取模运算
     * @param value  参数
     * @return 条件封装
     */
    public FastExample.FieldCriteria<P,T> thisModulo(Number value) {
        return thisModulo(value,null);
    }


    /**
     * 自定义字段运算
     * tableColumnName = customizeVal
     * 如:customize("user_age + 1") 则 tableColumnName = user_age + 1
     * 可以使用${参数名}  new HashMap(参数名,数据) 进行占位
     *
     * @param customizeVal 操作列后的自定义信息
     * @param data         条件
     * @return 条件封装
     */
    public FastExample.FieldCriteria<P,T> customize(String customizeVal, Map<String, Object> data) {
        fastExample.conditionPackages().addCustomUpdateColumns(fieldName, new CustomizeUpdateData(fieldName, customizeVal, data));
        return fastExample.field(fieldName);
    }

    public static class CustomizeUpdateData implements Serializable{
        private static final long serialVersionUID = 5067765705944232020L;
        private String fieldName;
        private String sql;
        private Map<String, Object> data;

        public CustomizeUpdateData(String fieldName, String sql, Map<String, Object> data) {
            this.fieldName = fieldName;
            this.sql = sql;
            this.data = data;
        }

        public String getFieldName() {
            return fieldName;
        }

        public void setFieldName(String fieldName) {
            this.fieldName = fieldName;
        }

        public String getSql() {
            return sql;
        }

        public void setSql(String sql) {
            this.sql = sql;
        }

        public Map<String, Object> getData() {
            return data;
        }

        public void setData(Map<String, Object> data) {
            this.data = data;
        }
    }

}

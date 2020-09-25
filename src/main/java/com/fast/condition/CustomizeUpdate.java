package com.fast.condition;

import com.fast.mapper.TableMapper;
import com.fast.mapper.TableMapperUtil;

import java.util.Map;

/**
 * 自定义列更新
 * 注意,同一列操作不能进行多次操作,多次操作只会最后一次生效
 *
 * @author 张亚伟 https://github.com/kaixinzyw
 */
public class CustomizeUpdate<Pojo> {

    /**
     * 条件封装
     */
    private FastExample<Pojo> fastExample;
    /**
     * 字段名
     */
    private String fieldName;
    /**
     * 更新列
     */
    private String tableColumnName;

    public CustomizeUpdate(Class<Pojo> pojoClass, FastExample fastExample, String fieldName) {
        this.fastExample = fastExample;
        this.fieldName = fieldName;
        TableMapper<Pojo> tableMapper = TableMapperUtil.getTableMappers(pojoClass);
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
    public FastExample.Criteria<Pojo> thisAdd(Object val, Map<String, Object> data) {
        fastExample.conditionPackages().addCustomUpdateColumns(fieldName, new CustomizeUpdateData(fieldName, tableColumnName + " + " + val, data));
        return fastExample.field(fieldName);
    }


    /**
     * 加法运算
     * @param value  参数
     * @return 条件封装
     */
    public FastExample.Criteria<Pojo> thisAdd(Number value) {
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
    public FastExample.Criteria<Pojo> thisSbu(Object val, Map<String, Object> data) {
        fastExample.conditionPackages().addCustomUpdateColumns(fieldName, new CustomizeUpdateData(fieldName, tableColumnName + " - " + val, data));
        return fastExample.field(fieldName);
    }

    /**
     * 减法运算
     * @param value  参数
     * @return 条件封装
     */
    public FastExample.Criteria<Pojo> thisSbu(Number value) {
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
    public FastExample.Criteria<Pojo> thisMul(Object val, Map<String, Object> data) {
        fastExample.conditionPackages().addCustomUpdateColumns(fieldName, new CustomizeUpdateData(fieldName, tableColumnName + " * " + val, data));
        return fastExample.field(fieldName);
    }

    /**
     * 乘法运算
     * @param value  参数
     * @return 条件封装
     */
    public FastExample.Criteria<Pojo> thisMul(Number value) {
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
    public FastExample.Criteria<Pojo> thisDiv(Object val, Map<String, Object> data) {
        fastExample.conditionPackages().addCustomUpdateColumns(fieldName, new CustomizeUpdateData(fieldName, tableColumnName + " / " + val, data));
        return fastExample.field(fieldName);
    }
    /**
     * 除法运算
     * @param value  参数
     * @return 条件封装
     */
    public FastExample.Criteria<Pojo> thisDiv(Number value) {
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
    public FastExample.Criteria<Pojo> thisModulo(Object val, Map<String, Object> data) {
        fastExample.conditionPackages().addCustomUpdateColumns(fieldName, new CustomizeUpdateData(fieldName, tableColumnName + " % " + val, data));
        return fastExample.field(fieldName);
    }
    /**
     * 取模运算
     * @param value  参数
     * @return 条件封装
     */
    public FastExample.Criteria<Pojo> thisModulo(Number value) {
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
    public FastExample.Criteria<Pojo> customize(String customizeVal, Map<String, Object> data) {
        fastExample.conditionPackages().addCustomUpdateColumns(fieldName, new CustomizeUpdateData(fieldName, customizeVal, data));
        return fastExample.field(fieldName);
    }

    public static class CustomizeUpdateData {
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

package com.fast.condition;

import com.fast.mapper.TableMapper;
import com.fast.mapper.TableMapperUtil;

/**
 * 自定义列更新
 * 注意,同一列操作不能进行多次操作,多次操作只会最后一次生效
 * @author 张亚伟 https://github.com/kaixinzyw
 */
public class CustomUpdate<Pojo> {

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

    public CustomUpdate(Class<Pojo> pojoClass, FastExample fastExample, String fieldName) {
        this.fastExample = fastExample;
        this.fieldName = fieldName;
        TableMapper<Pojo> tableMapper = TableMapperUtil.getTableMappers(pojoClass);
        this.tableColumnName = tableMapper.getShowTableNames().get(fieldName);
    }

    /**
     * tableColumnName = tableColumnName + val
     * 如:thisAdd(10) 则 tableColumnName = tableColumnName + 10
     * @param val 参数
     * @return 条件封装
     */
    public FastExample.Criteria<Pojo> thisAdd(Object val) {
        fastExample.conditionPackages().addCustomUpdateColumns(fieldName, tableColumnName + " + " + val);
        return fastExample.field(fieldName);
    }

    /**
     * tableColumnName = tableColumnName - val
     * 如:thisMinus(10) 则 tableColumnName = tableColumnName - 10
     * @param val 参数
     * @return 条件封装
     */
    public FastExample.Criteria<Pojo> thisMinus(Object val) {
        fastExample.conditionPackages().addCustomUpdateColumns(fieldName, tableColumnName + " - " + val);
        return fastExample.field(fieldName);
    }

    /**
     * tableColumnName = tableColumnName symbol val
     * 如:thisCustomize("-",10) 则 tableColumnName = tableColumnName - 10
     * @param symbol 操作连接符 如: '-','+'
     * @param val 参数
     * @return 条件封装
     */
    public FastExample.Criteria<Pojo> thisCustomize(String symbol, Object val) {
        fastExample.conditionPackages().addCustomUpdateColumns(fieldName, tableColumnName + symbol + val);
        return fastExample.field(fieldName);
    }

    /**
     * tableColumnName = columnName symbol val
     * 如:customize("user_age","-",10) 则 tableColumnName = user_age - 10
     * @param columnName 运算列
     * @param symbol 操作连接符 如: '-','+'
     * @param val 参数
     * @return 条件封装
     */
    public FastExample.Criteria<Pojo> customize(String columnName, String symbol, Object val) {
        fastExample.conditionPackages().addCustomUpdateColumns(fieldName, columnName + symbol + val);
        return fastExample.field(fieldName);
    }

    /**
     * tableColumnName = customizeVal
     * 如:customize("user_age + 1") 则 tableColumnName = user_age + 1
     * @param customizeVal 操作列后的自定义信息
     * @return 条件封装
     */
    public FastExample.Criteria<Pojo> customize(String customizeVal) {
        fastExample.conditionPackages().addCustomUpdateColumns(fieldName, customizeVal);
        return fastExample.field(fieldName);
    }

}

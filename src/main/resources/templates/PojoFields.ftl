<#assign beanName = table.pojoName/>
<#assign properties = table.properties/>
<#assign properties2 = table.properties2/>
<#assign propertiesAnColumns = table.propertiesAnColumns/>
<#assign keys = properties?keys/>
package ${table.pojoFieldsPackPath};

/**
* 字段表 ${table.tableDesc}
*/
public class ${table.pojoFieldsName} {

<#list properties2 as bean>
    /**
    *${bean.propertyDesc}
    */
    public static final String ${bean.propertyName ? cap_first} = "${bean.propertyName}";
</#list>

}

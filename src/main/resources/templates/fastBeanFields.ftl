package ${conf.basePackage}.${conf.beanPackage}.${conf.beanTemplate}.${conf.beanFieldsPackage}<#if prefixName??>.${prefixName}</#if>;

/**
* 实体bean字段表 ${table.tableDesc}
* @author 张亚伟 398850094@qq.com ${.now}
*/
public class ${beanName}Fields {

<#assign properties = table.properties/>
<#assign properties2 = table.properties2/>
<#assign propertiesAnColumns = table.propertiesAnColumns/>
<#assign keys = properties?keys/>
<#list properties2 as bean>

    /**
    *${bean.propertyDesc}
    */
    public static final String ${bean.propertyName ? cap_first} = "${bean.propertyName}";

</#list>


}

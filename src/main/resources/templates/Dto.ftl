<#assign beanName = table.pojoName/>
<#assign properties = table.properties/>
<#assign properties2 = table.properties2/>
<#assign propertiesAnColumns = table.propertiesAnColumns/>
<#assign ids = table.primaryKey/>
<#assign keys = properties?keys/>
<#-- bean template -->
package ${table.dtoPackPath};

import java.io.Serializable;
<#if conf.useLombok>
import lombok.Data;
</#if>
<#list table.packages as package>
${package}
</#list>

/**
* 实体bean ${table.tableDesc}
* @author ${.now}
*/
<#if conf.useLombok>
@Data
</#if>
public class ${table.dtoName} implements Serializable {

    private static final long serialVersionUID = 1L;

<#list properties2 as bean>
    <#if (ids[bean.propertyName])??>
    /**
    *${bean.propertyDesc}
    */
    private ${bean.propertyType} ${bean.propertyName};
    <#else>
    /**
    *${bean.propertyDesc}
    */
    private ${bean.propertyType} ${bean.propertyName};
    </#if>
</#list>

<#if !conf.useLombok>
    <#list keys as key>
    public  ${properties["${key}"]} get${key?cap_first}() {
        return this.${key};
    }
    public  void set${key?cap_first}(${properties["${key}"]} ${key}) {
        this.${key} = ${key};
    }

    </#list>
</#if>
}

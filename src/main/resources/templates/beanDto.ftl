<#-- bean template -->
package ${conf.basePackage}.${conf.dtoPackage}<#if prefixName??>.${prefixName}</#if>;
import java.io.Serializable;
<#if conf.useLombok>
import lombok.Data;
</#if>
<#list table.packages as package>
    ${package}
</#list>
/**
* 实体bean ${table.tableDesc}
* @author 张亚伟 398850094@qq.com ${.now}
*/
<#if conf.useLombok>
@Data
</#if>
public class ${beanName}Dto implements Serializable {

    private static final long serialVersionUID = 1L;
<#assign properties = table.properties/>
<#assign properties2 = table.properties2/>
<#assign propertiesAnColumns = table.propertiesAnColumns/>
<#assign ids = table.primaryKey/>
<#assign deletedField = conf.deletedField/>
<#assign noDeletedDefaults = conf.noDeletedDefaults/>
<#assign keys = properties?keys/>
<#list properties2 as bean>

    <#if (ids[bean.propertyName])??>
    /**
    *${bean.propertyDesc}
    */
    private ${bean.propertyType} ${bean.propertyName};
    <#elseif bean.propertyName==deletedField>
    /**
    *${bean.propertyDesc}
    */
    private ${bean.propertyType} ${bean.propertyName} = ${noDeletedDefaults};
    <#else>
    /**
    *${bean.propertyDesc}
    */
    private ${bean.propertyType} ${bean.propertyName};
    </#if>

</#list>
<#if !conf.useLombok>

    <#list keys as key>

    public  ${properties["${key}"]} get${key?cap_first}(){
        return this.${key};
    }

    public  void set${key?cap_first}(${properties["${key}"]} ${key}){
        this.${key} = ${key};
    }

    </#list>
</#if>

}

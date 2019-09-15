<#-- bean template -->
package ${conf.basePackage}.${conf.beanPackage}<#if prefixName??>.${prefixName}</#if>;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.Column;
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
@Table(name = "${table.tableName}")
public class ${beanName} implements Serializable {

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
    @Id
    @Column(name = "${propertiesAnColumns[bean.propertyName]}")
    private ${bean.propertyType} ${bean.propertyName};
    <#elseif bean.propertyName==deletedField>
    /**
    *${bean.propertyDesc}
    */
    @Column(name = "${propertiesAnColumns[bean.propertyName]}")
    private ${bean.propertyType} ${bean.propertyName} = ${noDeletedDefaults};
    <#else>
    /**
    *${bean.propertyDesc}
    */
    @Column(name = "${propertiesAnColumns[bean.propertyName]}")
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

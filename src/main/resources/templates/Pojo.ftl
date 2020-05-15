<#assign beanName = table.pojoName/>
<#assign properties = table.properties/>
<#assign properties2 = table.properties2/>
<#assign propertiesAnColumns = table.propertiesAnColumns/>
<#assign ids = table.primaryKey/>
<#assign keys = properties?keys/>
<#-- bean template -->
package ${table.pojoPackPath};

import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.Column;
import java.io.Serializable;
<#if conf.useLombok>
import lombok.Data;
import lombok.experimental.Accessors;
</#if>
<#list table.packages as package>
${package}
</#list>

/**
* ${table.tableDesc}
*/
<#if conf.useLombok>
@Data
@Accessors(chain=true)
</#if>
@Table(name = "${table.tableName}")
public class ${beanName} implements Serializable {

    private static final long serialVersionUID = 1L;

<#list properties2 as bean>
    <#if (ids[bean.propertyName])??>
    /**
    *${bean.propertyDesc}
    */
    @Id
    @Column(name = "${propertiesAnColumns[bean.propertyName]}")
    private ${bean.propertyType} ${bean.propertyName};

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
    public ${properties["${key}"]} get${key?cap_first}() {
        return this.${key};
    }
    public ${beanName} set${key?cap_first}(${properties["${key}"]} ${key}) {
        this.${key} = ${key};
        return this;
    }

    </#list>
</#if>
}

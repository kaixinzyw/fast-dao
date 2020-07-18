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
import lombok.experimental.Accessors;
</#if>
<#if conf.useDTOSwagger2>
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
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
<#if conf.useDTOSwagger2>
@ApiModel("${beanName} ${table.tableDesc}")
</#if>
public class ${table.dtoName} implements Serializable {

    private static final long serialVersionUID = 1L;

<#list properties2 as bean>

    /**
    *${bean.propertyDesc}
    */
    <#if conf.useDTOSwagger2>
    @ApiModelProperty(value = "${bean.propertyDesc}")
    </#if>
    private ${bean.propertyType} ${bean.propertyName};

</#list>

<#if !conf.useLombok>
    <#list keys as key>
    public ${properties["${key}"]} get${key?cap_first}() {
        return this.${key};
    }
    public ${table.dtoName} set${key?cap_first}(${properties["${key}"]} ${key}) {
        this.${key} = ${key};
        return this;
    }

    </#list>
</#if>
}

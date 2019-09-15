<#-- bean template -->
package ${conf.basePackage}.${conf.beanPackage}.${conf.beanTemplate}<#if prefixName??>.${prefixName}</#if>;
import ${conf.basePackage}.${conf.beanPackage}.${conf.beanTemplate}.${conf.beanFieldsPackage}<#if prefixName??>.${prefixName}</#if>.${beanName}Fields;
import ${conf.basePackage}.${conf.beanPackage}<#if prefixName??>.${prefixName}</#if>.${beanName};
import com.fast.jdbc.template.DBTemplate;
import com.fast.jdbc.template.FastFunction;
import com.fast.jdbc.template.FastExample;
import com.fast.jdbc.base.BaseTemplate;

/**
* Bean快速查询扩展基础类
* @author 张亚伟 398850094@qq.com ${.now}
*/
public class ${beanName}Template extends BaseTemplate<${beanName}>{

    public static FastFunction<${beanName}> fastTemplate(){
        return new FastFunction<>(${beanName}.class);
    }

    public void fastEqualTo${beanName}(${beanName} pojo){
        fastExample.equalObject(pojo);
    }
<#assign properties = table.properties/>
<#assign keys = properties?keys/>
<#list keys as key>
    public FastExample.Criteria fast${key?cap_first}(){
        return fastExample.field(${beanName}Fields.${key?cap_first});
    }
</#list>



}
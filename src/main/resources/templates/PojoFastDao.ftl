<#assign beanName = table.pojoName/>
<#assign prefixName = table.prefixName/>
<#assign properties = table.properties/>
<#assign properties2 = table.properties2/>
<#assign keys = properties?keys/>
package ${table.pojoFastDaoPackPath};

import ${table.pojoClassPackPath};
import com.fast.base.BaseFastDAO;
import com.fast.condition.FastExample;
<#list table.packages as package>
${package}
</#list>

/**
* ${table.tableDesc}
*/
public class ${table.pojoFastDaoName} extends BaseFastDAO<${beanName}, ${table.pojoFastDaoName}> {
    private ${table.pojoFastDaoName}(){super.fastExample=new FastExample<>(${beanName}.class,this);}
    public static ${table.pojoFastDaoName} create(){return new ${table.pojoFastDaoName}();}
    public static ${table.pojoFastDaoName} create(Object object) {return new ${table.pojoFastDaoName}().equalObject(object);}
<#list properties2 as bean>

    /**
    *${bean.propertyDesc}
    */
    public ${table.pojoFastDaoName} ${bean.propertyName}(${bean.propertyType} ${bean.propertyName}){return fastExample.field("${bean.propertyName}").valEqual(${bean.propertyName});}
    public FastExample.FieldCriteria<${beanName},${table.pojoFastDaoName}> ${bean.propertyName}(){return fastExample.field("${bean.propertyName}");}
</#list>

}
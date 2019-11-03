<#assign beanName = table.pojoName/>
<#assign prefixName = table.prefixName/>
<#assign properties = table.properties/>
<#assign keys = properties?keys/>
package ${table.pojoFastDaoPackPath};

import ${table.pojoClassPackPath};
import com.fast.base.BaseFastDao;
import com.fast.example.BeanFactory;
import com.fast.example.FastExample;
<#list table.packages as package>
${package}
</#list>

/**
* @author ${.now}
*/
public class ${table.pojoFastDaoName} extends BaseFastDao<${beanName}> {

    public static ${table.pojoFastDaoName} create(){return BeanFactory.getBean(${table.pojoFastDaoName}.class);}
    public static ${table.pojoFastDaoName} create(${beanName} ${beanName?uncap_first}) {
        ${table.pojoFastDaoName} fastDao = BeanFactory.getBean(${table.pojoFastDaoName}.class);
        fastDao.equalPojo(${beanName?uncap_first});
        return fastDao;
    }

<#list keys as key>
    public FastExample.Criteria<${beanName}> ${key}(${properties["${key}"]}... ${key}s){return fastExample.field("${key}").valEqual(${key}s);}
</#list>

}
<#assign beanName = table.pojoName/>
<#assign prefixName = table.prefixName/>
<#assign properties = table.properties/>
<#assign keys = properties?keys/>
package ${table.pojoFastDaoPackPath};

import ${table.pojoClassPackPath};
import com.fast.base.BaseFastDao;
import com.fast.condition.FastExample;
<#list table.packages as package>
${package}
</#list>

/**
* @author ${.now}
*/
public class ${table.pojoFastDaoName} extends BaseFastDao<${beanName}> {

    public static ${table.pojoFastDaoName} create(){return new ${table.pojoFastDaoName}();}
    public static ${table.pojoFastDaoName} create(Object object) {
        ${table.pojoFastDaoName} fastDao = new ${table.pojoFastDaoName}();
        fastDao.equalObject(object);
        return fastDao;
    }

<#list keys as key>
    public FastExample.Criteria<${beanName}> ${key}(${properties["${key}"]}... ${key}s){return fastExample.field("${key}").valEqual(${key}s);}
</#list>

}
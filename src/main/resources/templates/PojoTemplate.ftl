<#assign beanName = table.pojoName/>
<#assign prefixName = table.prefixName/>
<#assign properties = table.properties/>
<#assign keys = properties?keys/>
package ${table.pojoTemplatePackPath};

import ${table.pojoFieldsClassPackPath};
import ${table.pojoClassPackPath};
import com.alibaba.fastjson.JSONObject;
import com.fast.db.template.config.AutomaticParameterAttributes;
import com.fast.db.template.utils.FastValueUtil;
import com.fast.db.template.base.BaseTemplate;
import com.fast.db.template.template.FastDao;
import com.fast.db.template.template.FastExample;
import com.fast.db.template.utils.page.PageInfo;
import java.util.List;
<#list table.packages as package>
${package}
</#list>

/**
* Bean快速查询扩展基础类
* @author ${.now}
*/
public class ${table.pojoTemplateName} extends BaseTemplate<${beanName}> {

    public FastDao<${beanName}> dao(){return fastExample.dao();}

    private ${table.pojoTemplateName}(FastExample<${beanName}> fastExample){super.fastExample = fastExample;}
    public static ${table.pojoTemplateName} create(){return new ${beanName}Template(new FastExample<>(${beanName}.class));}
    public static ${table.pojoTemplateName} create(${beanName} ${beanName?uncap_first}){return new ${beanName}Template(new FastExample<>(${beanName}.class, ${beanName?uncap_first}));}

<#list keys as key>
    public FastExample.Criteria<${beanName}> ${key}(){return fastExample.field(${beanName}Fields.${key?cap_first});}
    public FastExample.Criteria<${beanName}> ${key}(${properties["${key}"]} ${key}){return fastExample.field(${beanName}Fields.${key?cap_first}).valEqual(${key});}
</#list>

}
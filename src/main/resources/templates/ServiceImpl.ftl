<#assign beanName = table.pojoName/>
<#assign prefixName = table.prefixName/>
package ${table.servicePackPath};

import ${table.iserviceClassPackPath};
import ${table.pojoClassPackPath};
import com.fast.db.template.base.FastBaseServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class ${table.serviceName} extends FastBaseServiceImpl<${beanName}> implements ${table.iserviceName} {

}
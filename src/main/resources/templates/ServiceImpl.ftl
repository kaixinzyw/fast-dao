<#assign beanName = table.pojoName/>
<#assign prefixName = table.prefixName/>
package ${table.servicePackPath};

import com.fast.base.FastBaseServiceImpl;
import ${table.iserviceClassPackPath};
import org.springframework.stereotype.Service;
import ${table.pojoClassPackPath};

@Service
public class ${table.serviceName} extends FastBaseServiceImpl<${beanName}> implements ${table.iserviceName} {

}
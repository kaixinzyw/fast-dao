<#assign beanName = table.pojoName/>
<#assign prefixName = table.prefixName/>
package ${table.servicePackPath};

import ${table.iserviceClassPackPath};
import org.springframework.stereotype.Service;

@Service
public class ${table.serviceName} implements ${table.iserviceName}{

}
<#assign beanName = table.pojoName/>
package ${table.iservicePackPath};

import com.fast.base.IFastBaseService;
import ${table.pojoClassPackPath};

public interface ${table.iserviceName} extends IFastBaseService<${beanName}> {

}
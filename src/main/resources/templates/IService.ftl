<#assign beanName = table.pojoName/>
package ${table.iservicePackPath};

import ${table.pojoClassPackPath};
import com.fast.db.template.base.IFastBaseService;

public interface ${table.iserviceName} extends IFastBaseService<${beanName}> {

}
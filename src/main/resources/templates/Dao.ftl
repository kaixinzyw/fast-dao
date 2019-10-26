<#assign beanName = table.pojoName/>
package ${table.daoPackPath};

import ${table.pojoClassPackPath};
import org.springframework.stereotype.Repository;
import com.fast.db.template.base.FastBaseDao;

@Repository
public class ${table.daoName} extends FastBaseDao<${beanName}> {

}
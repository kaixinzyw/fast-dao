<#assign beanName = table.pojoName/>
package ${table.daoPackPath};

import ${table.pojoClassPackPath};
import org.springframework.stereotype.Repository;
import com.fast.base.FastBaseDAO;

@Repository
public class ${table.daoName} extends FastBaseDAO<${beanName}> {

}
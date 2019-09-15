package ${conf.basePackage}.${conf.daoPackage}<#if prefixName??>.${prefixName}</#if>;

import ${conf.basePackage}.${conf.beanPackage}<#if prefixName??>.${prefixName}</#if>.${beanName};
import org.springframework.stereotype.Repository;
import com.fast.jdbc.base.FastBaseDao;

@Repository
public class ${beanName}Dao extends FastBaseDao<${beanName}>{

}
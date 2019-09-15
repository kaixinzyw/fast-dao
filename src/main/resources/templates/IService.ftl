package ${conf.basePackage}.${conf.servicePackage}<#if prefixName??>.${prefixName}</#if>;

import ${conf.basePackage}.${conf.beanPackage}<#if prefixName??>.${prefixName}</#if>.${beanName};
import com.fast.jdbc.base.IFastBaseService;

public interface I${beanName}Service extends IFastBaseService<${beanName}> {

}
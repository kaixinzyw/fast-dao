package ${conf.basePackage}.${conf.servicePackage}<#if prefixName??>.${prefixName}</#if>.impl;

import ${conf.basePackage}.${conf.servicePackage}<#if prefixName??>.${prefixName}</#if>.I${beanName}Service;
import ${conf.basePackage}.${conf.beanPackage}<#if prefixName??>.${prefixName}</#if>.${beanName};
import com.fast.jdbc.base.FastBaseServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class ${beanName}ServiceImpl extends FastBaseServiceImpl<${beanName}> implements I${beanName}Service {

}
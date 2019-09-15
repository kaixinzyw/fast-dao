package com.fast.db.template.code.bean;



import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author zhangjh
 */
public class Conf {
	private static List allModules = Arrays.asList("bean", "bean_fields_package", "bean_template", "service", "impl", "dao", "dto");
	/**
	 * 包路径
	 */
	private String basePackage;
	/**
	 * Pojo文件路径
	 */
	private String beanPackage;
	/**
	 * Pojo字段文件路径
	 */
	private String beanFieldsPackage;
	/**
	 * DB模板文件路径
	 */
	private String beanTemplate;
	/**
	 * service文件路径
	 */
	private String servicePackage;
	/**
	 * DAO文件路径
	 */
	private String daoPackage;
	/**
	 * DTO文件路径
	 */
	private String dtoPackage;
	/**
	 * 需要生成的表
	 */
	private String tables;
	/**
	 * 需要生成的模块
	 */
	private String needModules;
	/**
	 * 需要生成的模块集合
	 */
	private List<String> modules;
	/**
	 * 逻辑删除字段
	 */
	private String deletedField;
	/**
	 * 逻辑删除未删除值,目前只支持布尔
	 */
	private String noDeletedDefaults;
	/**
	 * 数据库驱动
	 */
	private String driverClass;
	/**
	 * 数据库URL
	 */
	private String url;
	/**
	 * 数据库账户
	 */
	private String user;
	/**
	 * 数据库密码
	 */
	private String password;
	/**
	 * 是否覆盖生成文件
	 */
	private boolean force;
	/**
	 * 表名前缀
	 */
	private boolean prefix;

	/**
	 * 表名前缀名称
	 */
	private String prefixName;

	/**
	 * 是否需要将表字段转成驼峰
	 */
	private boolean underline2Camel;

	/**
	 * 是否使用Lombok插件
	 */
	private boolean useLombok;



	public List<String> getModules() {
		List<String> modules;
		String needModules = getNeedModules();
		if (null == needModules) {
			modules = Arrays.asList(CodeCreateParameter.CodeCreateModule.Base.codeModule.split(","));
		}else if ("all".equals(needModules)) {
			modules = allModules;
		}else {
			modules = Arrays.asList(needModules.split(","));
		}
		return modules;
	}


	/**
	 * @return
	 */
	public Conf getConf(CodeCreateParameter codeParameter) {

		this.setBasePackage(codeParameter.getBasePackage());

		if (StringUtils.isEmpty(this.getBasePackage())) {
			throw new RuntimeException("FastMapper:创建文件时包路径不能为空");
		}


		/**
		 * 需要生成的表
		 */
		String tables;
		if (codeParameter.getCreateTables()!=null&&codeParameter.getCreateTables().size()>0) {
			tables = StringUtils.collectionToDelimitedString(codeParameter.getCreateTables(),",");
		}else {
			tables = "all";
		}
		this.setTables(tables);


		/**
		 * 需要创建的模块
		 */
		String needModules;
		if (codeParameter.getNeedModules()!=null&&codeParameter.getNeedModules().size()>0) {
			needModules = StringUtils.collectionToDelimitedString(codeParameter.getNeedModules(),",");
		}else {
			needModules = CodeCreateParameter.CodeCreateModule.Base.codeModule;
		}
		this.setNeedModules(needModules);

		/**
		 * 是否要强制创建文件
		 */
		this.setForce(codeParameter.getForce());

		/**
		 * 下划线转驼峰
		 */
		this.setUnderline2Camel(codeParameter.getUnderline2CamelStr());

		/**
		 * talbe是否有前缀，比如t_com_company，前缀是t_com，true = hava，false = no;
		 */
		this.setPrefix(codeParameter.getPrefix());
		this.setPrefixName(codeParameter.getPrefixName());

		this.setDeletedField(codeParameter.getDeletedField());
		this.setNoDeletedDefaults(codeParameter.getNoDeletedDefaults());
		this.setBeanPackage( "pojo");
		this.setBeanFieldsPackage("fields");
		this.setBeanTemplate("template");
		this.setDaoPackage("dao");
		this.setDtoPackage("dto");
		this.setServicePackage("service");

		this.driverClass=codeParameter.getDriverClass();
		this.url = codeParameter.getUrl();
		this.user = codeParameter.getUser();
		this.password = codeParameter.getPassword();
		this.useLombok = codeParameter.getUseLombok();
		return this;
	}

	public boolean isUnderline2Camel() {
		return underline2Camel;
	}

	public void setUnderline2Camel(boolean underline2Camel) {
		this.underline2Camel = underline2Camel;
	}

	public void setModules(List<String> modules) {
		this.modules = modules;
	}

	public static List getAllModules() {
		return allModules;
	}

	public static void setAllModules(List allModules) {
		Conf.allModules = allModules;
	}

	public String getBasePackage() {
		return basePackage;
	}

	public void setBasePackage(String basePackage) {
		this.basePackage = basePackage;
	}

	public String getBeanPackage() {
		return beanPackage;
	}

	public void setBeanPackage(String beanPackage) {
		this.beanPackage = beanPackage;
	}

	public String getBeanFieldsPackage() {
		return beanFieldsPackage;
	}

	public void setBeanFieldsPackage(String beanFieldsPackage) {
		this.beanFieldsPackage = beanFieldsPackage;
	}

	public String getBeanTemplate() {
		return beanTemplate;
	}

	public void setBeanTemplate(String beanTemplate) {
		this.beanTemplate = beanTemplate;
	}

	public String getServicePackage() {
		return servicePackage;
	}

	public void setServicePackage(String servicePackage) {
		this.servicePackage = servicePackage;
	}

	public String getDaoPackage() {
		return daoPackage;
	}

	public void setDaoPackage(String daoPackage) {
		this.daoPackage = daoPackage;
	}

	public String getDtoPackage() {
		return dtoPackage;
	}

	public void setDtoPackage(String dtoPackage) {
		this.dtoPackage = dtoPackage;
	}

	public String getTables() {
		return tables;
	}

	public void setTables(String tables) {
		this.tables = tables;
	}

	public String getNeedModules() {
		return needModules;
	}

	public void setNeedModules(String needModules) {
		this.needModules = needModules;
	}

	public String getDeletedField() {
		return deletedField;
	}

	public void setDeletedField(String deletedField) {
		this.deletedField = deletedField;
	}

	public String getNoDeletedDefaults() {
		return noDeletedDefaults;
	}

	public void setNoDeletedDefaults(String noDeletedDefaults) {
		this.noDeletedDefaults = noDeletedDefaults;
	}

	public String getDriverClass() {
		return driverClass;
	}

	public void setDriverClass(String driverClass) {
		this.driverClass = driverClass;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public boolean isForce() {
		return force;
	}

	public void setForce(boolean force) {
		this.force = force;
	}

	public boolean isPrefix() {
		return prefix;
	}

	public void setPrefix(boolean prefix) {
		this.prefix = prefix;
	}

	public String getPrefixName() {
		return prefixName;
	}

	public void setPrefixName(String prefixName) {
		this.prefixName = prefixName;
	}

	public boolean isUseLombok() {
		return useLombok;
	}

	public void setUseLombok(boolean useLombok) {
		this.useLombok = useLombok;
	}
}

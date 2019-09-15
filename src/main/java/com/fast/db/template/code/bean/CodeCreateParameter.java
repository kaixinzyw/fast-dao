package com.fast.db.template.code.bean;

import java.util.HashSet;
import java.util.Set;

public class CodeCreateParameter {
    /**
     * 需要生成模型的表
     */
    private Set<String> createTables = new HashSet<>();
    /**
     * talbe是否有前缀，比如t_com_company，前缀是t_com;
     */
    private Boolean prefix = Boolean.FALSE;
    private String prefixName;
    /**
     * 下划线转驼峰
     */
    private Boolean underline2CamelStr = Boolean.TRUE;
    /**
     * 是否要强制创建文件
     */
    private Boolean force = Boolean.FALSE;
    /**
     * 是否使用Lombok插件
     */
    private Boolean useLombok = Boolean.TRUE;
    /**
     * 需要创建的模块
     */
    private Set<String> needModules = new HashSet<>();
    private String deletedField = "deleted";
    private String noDeletedDefaults = "false";
    private String basePackage;
    private String driverClass;
    private String url;
    private String user;
    private String password;
    /**
     * 模型创建列表
     */
    public enum CodeCreateModule {
        /**
         * 基本生成类
         */
        Base("bean,bean_template,bean_fields_package"),
        /**
         * Service类
         */
        Service("service"),
        /**
         * Service实现类
         */
        ServiceImpl("impl"),
        /**
         * Dto类
         */
        Dto("dto"),
        /**
         * Dao类
         */
        Dao("dao"),
        /**
         * 生成所有文件
         */
        All("all");
        String codeModule;

        CodeCreateModule(String codeModule) {
            this.codeModule = codeModule;
        }
    }

    public void setCreateTables(String... tables) {
        for (String table : tables) {
            createTables.add(table);
        }
    }

    public Boolean getPrefix() {
        return prefix;
    }

    public void setPrefix(Boolean prefix) {
        this.prefix = prefix;
    }

    public Boolean getUnderline2CamelStr() {
        return underline2CamelStr;
    }

    public void setUnderline2CamelStr(Boolean underline2CamelStr) {
        this.underline2CamelStr = underline2CamelStr;
    }

    public Boolean getForce() {
        return force;
    }

    public void setForce(Boolean force) {
        this.force = force;
    }

    public Boolean getUseLombok() {
        return useLombok;
    }

    public void setUseLombok(Boolean useLombok) {
        this.useLombok = useLombok;
    }

    public void setNeedModules(CodeCreateModule... modules) {
        for (CodeCreateModule module : modules) {
            needModules.add(module.codeModule);
        }
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

    public String getBasePackage() {
        return basePackage;
    }

    public void setBasePackage(String basePackage) {
        this.basePackage = basePackage;
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

    public Set<String> getCreateTables() {
        return createTables;
    }

    public void setCreateTables(Set<String> createTables) {
        this.createTables = createTables;
    }

    public String getPrefixName() {
        return prefixName;
    }

    public void setPrefixName(String prefixName) {
        this.prefixName = prefixName;
    }

    public Set<String> getNeedModules() {
        return needModules;
    }

}

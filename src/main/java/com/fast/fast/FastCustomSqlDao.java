package com.fast.fast;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.text.StrBuilder;
import cn.hutool.core.util.StrUtil;
import com.fast.condition.ConditionPackages;
import com.fast.utils.SqlTemplateUtil;
import com.fast.utils.page.PageInfo;

import java.util.List;
import java.util.Map;

/**
 * 自定义SQL执行器
 *
 * @param <T> 自定义SQL操作的对象泛型
 * @author 张亚伟 https://github.com/kaixinzyw
 */
public class FastCustomSqlDao<T> {

    private ConditionPackages<T> conditionPackages;

    private FastCustomSqlDao() {
    }

    /**
     * 自定义SQL执行器初始化,如需使用,必须调用此方法进行初始化创建
     *
     * @param clazz  自定义SQL操作的类
     * @param sql    自定义sql语句,如果有占位符,使用#{参数名}进行描述 例:select * from xxx where userName=${userName}
     * @param params 占位符参数,如果使用占位符进行条件参数封装,必须传入条件参数 如上,需要使用Map put("userName","XXX")
     * @param <T>    自定义SQL操作的对象泛型
     * @return 自定义SQL执行器
     */
    public static <T> FastCustomSqlDao<T> create(Class<T> clazz, String sql, Object params) {
        FastCustomSqlDao<T> customSqlDao = new FastCustomSqlDao<>();
        customSqlDao.conditionPackages = ConditionPackages.create(clazz);
        customSqlDao.conditionPackages.customSQL(sql, SqlTemplateUtil.getMap(params));
        return customSqlDao;
    }

    /**
     * 自定义SQL执行器初始化,如需使用,必须调用此方法进行初始化创建
     *
     * @param clazz  自定义SQL操作的类
     * @param path   resource 目录下的文件,文件编码必须为UTF-8
     * @param params 占位符参数,如果使用占位符进行条件参数封装,必须传入条件参数 如上,需要使用Map put("userName","XXX")
     * @param <T>    自定义SQL操作的对象泛型
     * @return 自定义SQL执行器
     */
    public static <T> FastCustomSqlDao<T> createResource(Class<T> clazz, String path, Object params) {
        return create(clazz, SqlTemplateUtil.getSql(path, params), params);
    }

    /**
     * 自定义SQL执行器初始化,如需使用,必须调用此方法进行初始化创建
     *
     * @param clazz   自定义SQL操作的类
     * @param path    resource 目录下的文件,文件编码必须为UTF-8
     * @param sqlList resource 额外的SQL 在SQL尾部进行拼接
     * @param params  占位符参数,如果使用占位符进行条件参数封装,必须传入条件参数 如上,需要使用Map put("userName","XXX")
     * @param <T>     自定义SQL操作的对象泛型
     * @return 自定义SQL执行器
     */
    public static <T> FastCustomSqlDao<T> createResource(Class<T> clazz, String path, List<String> sqlList, Object params) {
        StrBuilder strBuilder = StrUtil.strBuilder(SqlTemplateUtil.getSql(path, params));
        if (CollUtil.isNotEmpty(sqlList)) {
            for (String sql : sqlList) {
                if (StrUtil.isNotEmpty(sql)) {
                    strBuilder.append(sql).append(System.lineSeparator());
                }
            }
        }
        return create(clazz, strBuilder.toString(), params);
    }

    public FastDao<T> dao(){
        return FastDao.init(conditionPackages);
    }



    /**
     * 通过查询条件查询一条数据
     *
     * @return 数据结果
     */
    public T findOne() {
        return DaoTemplate.init(conditionPackages).findOne();
    }

    /**
     * 通过查询条件查询所符合要求的所有数据
     *
     * @return 数据结果
     */
    public List<T> findAll() {
        return DaoTemplate.init(conditionPackages).findAll();
    }

    /**
     * 通过查询条件查询所符合要求的数据数量
     *
     * @return 查询到的数据条数
     */
    public Integer findCount() {
        return DaoTemplate.init(conditionPackages).findCount();
    }

    /**
     * 通过查询条件查询所符合要求的数据,并进行分页
     *
     * @param pageNum       页数
     * @param pageSize      条数
     * @param navigatePages 页面数量
     * @return 分页对象, 内包含分页信息和查询到的数据
     */
    public PageInfo<T> findPage(int pageNum, int pageSize, int navigatePages) {
        return DaoTemplate.<T>init(conditionPackages).findPage(pageNum, pageSize, navigatePages);
    }

    /**
     * 通过查询条件查询所符合要求的数据,并进行分页,默认9条
     *
     * @param pageNum  页数
     * @param pageSize 条数
     * @return 分页对象, 内包含分页信息和查询到的数据
     */
    public PageInfo<T> findPage(int pageNum, int pageSize) {
        return DaoTemplate.<T>init(conditionPackages).findPage(pageNum, pageSize, 9);
    }

    /**
     * 删除数据 本操作不会使用逻辑删除方式
     *
     * @return 删除影响到的数据条数
     */
    public Integer delete() {
        return DaoTemplate.init(conditionPackages).delete();
    }

    public String getSql() {
        return conditionPackages.getCustomSql();
    }

    public Map<String, Object> getParams() {
        return conditionPackages.getCustomSqlParams();
    }
}

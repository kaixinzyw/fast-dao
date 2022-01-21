package com.fast.utils;

import cn.hutool.core.io.IoUtil;
import cn.hutool.core.io.resource.ClassPathResource;
import cn.hutool.core.lang.Singleton;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.fast.config.FastDaoAttributes;
import org.beetl.core.Configuration;
import org.beetl.core.GroupTemplate;
import org.beetl.core.Template;
import org.beetl.core.resource.StringTemplateResourceLoader;

import java.util.HashMap;
import java.util.Map;

/**
 * sql模板解析
 *
 * @author Java
 */
public class SqlTemplateUtil {

    private static final Map<String, String> SQL_MAP = new HashMap<>();

    /**
     * 获得模板解析后的sql
     *
     * @param path   路径
     * @param params 参数个数
     * @return {@link String}
     */
    public static String getSql(String path, Object params) {
        return resolve(getSqlTemplate(path), params);
    }

    /**
     * 获得sql模板
     *
     * @param path 路径
     * @return {@link String}
     */
    public static String getSqlTemplate(String path) {
        String sql = SQL_MAP.get(path);
        if (sql == null) {
            ClassPathResource resource = new ClassPathResource(StrUtil.strBuilder(
                    FastDaoAttributes.sqlTemplatePrefix, path, FastDaoAttributes.sqlTemplateSuffix).toString());
            sql = IoUtil.read(resource.getStream()).toString();
            SQL_MAP.put(path, sql);
        }
        return sql;
    }

    /**
     * SQL模板解析
     *
     * @param sql    sql模板
     * @param params 模板数据
     * @return {@link String}
     */
    public static String resolve(String sql, Object params) {
        GroupTemplate groupTemplate = Singleton.get("SqlTemplateResolve", () -> {
            //初始化代码
            StringTemplateResourceLoader resourceLoader = new StringTemplateResourceLoader();
            Configuration cfg = Configuration.defaultConfiguration();
            cfg.setPlaceholderStart("@{");
            cfg.setStatementStart("-- @");
            cfg.setStatementEnd(System.lineSeparator());
            cfg.setStatementStart2("-- ");
            cfg.setStatementEnd2(System.lineSeparator());
            cfg.setHtmlTagSupport(false);
            cfg.setSafeOutput(true);
//            cfg.setPlaceholderStart2("${");
//            cfg.setPlaceholderEnd2("}");
            return new GroupTemplate(resourceLoader, cfg);
        });
        //获取模板
        Template template = groupTemplate.getTemplate(sql);
        template.binding(getMap(params));
        //渲染结果
        return template.render();
    }

    /**
     * 获得Map集合
     *
     * @param params 数据
     * @return {@link Map}
     */
    public static Map<String, Object> getMap(Object params) {
        if (params == null) {
            return new HashMap<>();
        }
        return JSONObject.parseObject(JSONObject.toJSONString(params), new TypeReference<Map<String, Object>>() {
        });
    }

}

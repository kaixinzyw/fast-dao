package com.fast.db.template.code.creator.impl;


import cn.hutool.core.util.StrUtil;
import com.fast.db.template.code.bean.Conf;
import com.fast.db.template.code.bean.TableInfo;
import com.fast.db.template.code.creator.AbstractFileCreator;
import freemarker.template.Template;
import freemarker.template.TemplateException;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 创建bean
 *
 * @author zhangjh
 */
public class BeanClassCreator extends AbstractFileCreator {
    private static BeanClassCreator creator;

    private BeanClassCreator() {
        super();
    }

    private BeanClassCreator(Conf conf) {
        super();
        init(conf);
    }

    public static synchronized BeanClassCreator getInstance(Conf conf) {
        if (null == creator) {
            creator = new BeanClassCreator(conf);
        }
        return creator;
    }

    @Override
    public void createFile(TableInfo tableInfo) throws IOException, TemplateException {
        String ftl = "bean.ftl";
        String fileName = tableInfo.getBeanName();
        String selfPath = conf.getBeanPackage();
        Map<String, Object> root = new HashMap<String, Object>();
        root.put("table", tableInfo);
        root.put("conf", conf);
        String prefixName = "";
        /**
         * 有表名类别
         */
        if (conf.isPrefix()) {
            if (StrUtil.isNotEmpty(conf.getPrefixName())) {
                prefixName = conf.getPrefixName();
                fileName = fileName.substring(conf.getPrefixName().length());
            } else {
                prefixName = tableInfo.getBeanName().substring(0, 3).toLowerCase();
            }
        }
        if (StrUtil.isNotEmpty(prefixName)) {
            root.put("prefixName", prefixName);
        }
        root.put("beanName", fileName);

        Template temp = cfg.getTemplate(ftl);
        fileName = javaPath + selfPath + separator + prefixName + separator + fileName + ".java";
        createFile(fileName, root, temp);


    }


}

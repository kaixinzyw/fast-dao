package com.fast.db.template.code.factory;


import com.fast.db.template.code.bean.Conf;
import com.fast.db.template.code.creator.FileCreator;
import com.fast.db.template.code.creator.impl.*;

/**
 * 生成代码
 */
public class SimpleFactory {
    private SimpleFactory() {
        super();
    }

    public static FileCreator create(String module, Conf conf) {

        FileCreator creator = null;
        if ("bean".equals(module)) {
            creator = BeanClassCreator.getInstance(conf);
        } else if ("bean_fields_package".equals(module)) {
            creator = BeanFieldsClassCreator.getInstance(conf);
        }else if ("bean_template".equals(module)) {
            creator = BeanFastFileCreator.getInstance(conf);
        }else if (module.equals("service")) {
            creator = ServiceClassCreator.getInstance(conf);
        } else if (module.equals("impl")) {
            creator = ServiceImplClassCreator.getInstance(conf);
        }  else if (module.equals("dao")) {
            creator = DaoClassCreator.getInstance(conf);
        }else if (module.equals("dto")) {
            creator = DtoClassCreator.getInstance(conf);
        }
        return creator;

    }
}

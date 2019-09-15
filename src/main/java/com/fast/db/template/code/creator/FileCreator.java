package com.fast.db.template.code.creator;


import com.fast.db.template.code.bean.TableInfo;
import freemarker.template.TemplateException;

import java.io.File;
import java.io.IOException;

/**
 *
 */
public interface FileCreator {
    String separator = File.separator;
    void createFile(TableInfo tableInfo) throws IOException, TemplateException;

}

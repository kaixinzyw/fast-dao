package com.fast.aspect;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Singleton;
import cn.hutool.core.util.StrUtil;
import com.fast.fast.FastDaoParam;

import java.util.ArrayList;
import java.util.List;

public class FastDaoExpanderRunner {

    private static List<Class<FastDaoExpander>> insertOccasion = new ArrayList<>();
    private static List<Class<FastDaoExpander>> deleteOccasion = new ArrayList<>();
    private static List<Class<FastDaoExpander>> updateOccasion = new ArrayList<>();
    private static List<Class<FastDaoExpander>> selectOccasion = new ArrayList<>();
    private static Boolean isExpander = Boolean.FALSE;

    public static void addFastDaoExpander(Class<FastDaoExpander> expanderClass) {
        FastDaoExpander expander = Singleton.get(expanderClass);
        if (expander == null || CollUtil.isEmpty(expander.occasion())) {
            return;
        }
        for (ExpanderOccasion occasion : expander.occasion()) {
            if (occasion.equals(ExpanderOccasion.INSERT)) {
                insertOccasion.add(expanderClass);
                continue;
            }
            if (occasion.equals(ExpanderOccasion.DELETE)) {
                deleteOccasion.add(expanderClass);
                continue;
            }
            if (occasion.equals(ExpanderOccasion.UPDATE)) {
                updateOccasion.add(expanderClass);
                continue;
            }
            if (occasion.equals(ExpanderOccasion.SELECT)) {
                selectOccasion.add(expanderClass);
                continue;
            }
        }
        isExpander = Boolean.TRUE;
    }

    public static boolean runBeforeFastDaoExpander(FastDaoParam param) {
        if (!isExpander) {
            return true;
        }
        List<Class<FastDaoExpander>> expanders = getExpanders(param);
        if (CollUtil.isEmpty(expanders)) {
            return true;
        }

        for (Class<FastDaoExpander> expanderClass : expanders) {
            FastDaoExpander expander = Singleton.get(expanderClass);
            if (!expander.before(param)) {
                return false;
            }
        }
        return true;
    }

    public static void runAfterFastDaoExpander(FastDaoParam param) {
        if (!isExpander) {
            return;
        }
        List<Class<FastDaoExpander>> expanders = getExpanders(param);
        if (CollUtil.isEmpty(expanders)) {
            return;
        }

        for (Class<FastDaoExpander> expanderClass : expanders) {
            Singleton.get(expanderClass).after(param);
        }
    }


    private static List<Class<FastDaoExpander>> getExpanders(FastDaoParam param) {
        List<Class<FastDaoExpander>> expanders = null;
        String sql = StrUtil.trimStart(param.getSql());
        if (StrUtil.startWithIgnoreCase(sql, ExpanderOccasion.INSERT.name())) {
            expanders = insertOccasion;
        } else if (StrUtil.startWithIgnoreCase(sql, ExpanderOccasion.DELETE.name())) {
            expanders = deleteOccasion;
        } else if (StrUtil.startWithIgnoreCase(sql, ExpanderOccasion.UPDATE.name())) {
            if (param.getLogicDelete()) {
                expanders = deleteOccasion;
            } else {
                expanders = updateOccasion;
            }
        } else if (StrUtil.startWithIgnoreCase(sql, ExpanderOccasion.SELECT.name())) {
            expanders = selectOccasion;
        }
        return expanders;
    }


}

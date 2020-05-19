package com.fast.aspect;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Singleton;
import com.fast.fast.FastDaoParam;

import java.util.ArrayList;
import java.util.List;

public class FastDaoExpanderRunner {

    private static List<Class<FastDaoExpander>> insertOccasion = new ArrayList<>();
    private static List<Class<FastDaoExpander>> deleteOccasion = new ArrayList<>();
    private static List<Class<FastDaoExpander>> updateOccasion = new ArrayList<>();
    private static List<Class<FastDaoExpander>> selectOccasion = new ArrayList<>();
    private static Boolean isExpander = Boolean.FALSE;

    private static final String INSERT = "insert";
    private static final String SELECT = "select";
    private static final String COUNT = "count";
    private static final String UPDATE = "update";
    private static final String DELETE = "delete";


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

    public static boolean runBeforeFastDaoExpander(FastDaoParam param, String methodName) {
        if (!isExpander) {
            return true;
        }
        List<Class<FastDaoExpander>> expanders = getExpanders(param, methodName);
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

    public static void runAfterFastDaoExpander(FastDaoParam param, String methodName) {
        if (!isExpander) {
            return;
        }
        List<Class<FastDaoExpander>> expanders = getExpanders(param, methodName);
        if (CollUtil.isEmpty(expanders)) {
            return;
        }

        for (Class<FastDaoExpander> expanderClass : expanders) {
            Singleton.get(expanderClass).after(param);
        }
    }


    private static List<Class<FastDaoExpander>> getExpanders(FastDaoParam param, String methodName) {
        List<Class<FastDaoExpander>> expanders = null;
        if (methodName.equals(INSERT)) {
            expanders = insertOccasion;
        } else if (methodName.equals(DELETE)) {
            expanders = deleteOccasion;
        } else if (methodName.equals(UPDATE)) {
            if (param.getLogicDelete()) {
                expanders = deleteOccasion;
            } else {
                expanders = updateOccasion;
            }
        } else if (methodName.equals(SELECT) || methodName.equals(COUNT)) {
            expanders = selectOccasion;
        }
        return expanders;
    }


}

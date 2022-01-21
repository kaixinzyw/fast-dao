package com.fast.aspect;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Singleton;
import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.StrUtil;
import com.fast.condition.ConditionPackages;

import java.util.ArrayList;
import java.util.List;

public class FastDaoExpanderRunner {

    private static final List<Class<FastDaoExpander>> INSERT_OCCASION = new ArrayList<>();
    private static final List<Class<FastDaoExpander>> DELETE_OCCASION = new ArrayList<>();
    private static final List<Class<FastDaoExpander>> UPDATE_OCCASION = new ArrayList<>();
    private static final List<Class<FastDaoExpander>> SELECT_OCCASION = new ArrayList<>();
    private static Boolean isExpander = Boolean.FALSE;


    public static void addFastDaoExpander(Class<FastDaoExpander> expanderClass) {
        FastDaoExpander expander = Singleton.get(expanderClass);
        if (expander == null || CollUtil.isEmpty(expander.occasion())) {
            return;
        }
        for (ExpanderOccasion occasion : expander.occasion()) {
            if (occasion.method.equals(ExpanderOccasion.INSERT.method)) {
                INSERT_OCCASION.add(expanderClass);
                continue;
            }
            if (occasion.method.equals(ExpanderOccasion.DELETE.method)) {
                DELETE_OCCASION.add(expanderClass);
                continue;
            }
            if (occasion.method.equals(ExpanderOccasion.UPDATE.method)) {
                UPDATE_OCCASION.add(expanderClass);
                continue;
            }
            if (occasion.method.equals(ExpanderOccasion.SELECT.method) || occasion.method.equals(ExpanderOccasion.COUNT.method)) {
                SELECT_OCCASION.add(expanderClass);
            }
        }
        isExpander = Boolean.TRUE;
    }

    public static void runBeforeFastDaoExpander(ConditionPackages conditionPackages, String methodName) {
        if (!isExpander) {
            return;
        }
        List<Class<FastDaoExpander>> expanders = getExpanders(conditionPackages, methodName);
        if (CollUtil.isEmpty(expanders)) {
            return;
        }

        for (Class<FastDaoExpander> expanderClass : expanders) {
            FastDaoExpander expander = Singleton.get(expanderClass);
            expander.before(conditionPackages);
        }
    }

    public static void runAfterFastDaoExpander(ConditionPackages conditionPackages, String methodName) {
        if (!isExpander) {
            return;
        }
        List<Class<FastDaoExpander>> expanders = getExpanders(conditionPackages, methodName);
        if (CollUtil.isEmpty(expanders)) {
            return;
        }

        for (Class<FastDaoExpander> expanderClass : expanders) {
            Singleton.get(expanderClass).after(conditionPackages);
        }
    }


    private static List<Class<FastDaoExpander>> getExpanders(ConditionPackages conditionPackages, String methodName) {
        List<Class<FastDaoExpander>> expanders = null;
        if (StrUtil.equals(methodName, ExpanderOccasion.INSERT.method)) {
            expanders = INSERT_OCCASION;
        } else if (methodName.equals(ExpanderOccasion.DELETE.method)) {
            expanders = DELETE_OCCASION;
        } else if (methodName.equals(ExpanderOccasion.UPDATE.method)) {
            if (BooleanUtil.isTrue(conditionPackages.getLogicDelete())) {
                expanders = DELETE_OCCASION;
            } else {
                expanders = UPDATE_OCCASION;
            }
        } else if (methodName.equals(ExpanderOccasion.SELECT.method) || methodName.equals(ExpanderOccasion.COUNT.method)) {
            expanders = SELECT_OCCASION;
        }
        return expanders;
    }


}

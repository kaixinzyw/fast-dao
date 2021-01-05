package com.fast.utils;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ArrayUtil;
import com.alibaba.fastjson.JSONObject;
import com.fast.utils.page.PageInfo;

import java.util.*;

/**
 * 对象转换工具类
 *
 * @author 张亚伟 https://github.com/kaixinzyw
 */
public class BeanCopyUtil {

    public static <T> List<T> copy(List list, Class<T> clazz) {
        if (list == null) {
            return null;
        }
        List<T> ts = new ArrayList<>();
        for (Object obj : list) {
            ts.add(copy(obj, clazz));
        }
        return ts;

    }

    public static <T> Set<T> copy(Set set, Class<T> clazz) {
        if (set == null) {
            return null;
        }

        Set<T> ts = new HashSet<>();
        for (Object obj : set) {
            ts.add(copy(obj, clazz));
        }
        return ts;
    }

    public static <T> T copy(Map<String, Object> m, Class<T> clazz) {
        if (CollUtil.isEmpty(m)) {
            return null;
        }
        return BeanUtil.mapToBeanIgnoreCase(m, clazz, true);
    }

    public static <T> PageInfo<T> copy(PageInfo page, Class<T> clazz) {
        if (page == null) {
            return null;
        }
        page.setList(copy(page.getList(), clazz));
        return page;
    }

    public static <T> T copy(Class<T> clazz, Object... objs) {
        try {
            JSONObject json = new JSONObject();
            if (ArrayUtil.isEmpty(objs)) {
                return null;
            }
            for (Object o : objs) {
                json.putAll(BeanUtil.beanToMap(o));
            }
            return json.toJavaObject(clazz);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T copy(Object obj, Class<T> clazz) {
        try {
            if (obj == null) {
                return null;
            }
            T t = clazz.newInstance();
            BeanUtil.copyProperties(obj, t, true);
            return t;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}

package com.fast.utils;

import cn.hutool.core.collection.CollUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Function;

public class BeanConvertUtil {

    public static <T, U> Map<U, T> toMap(List<T> list, Function<? super T, ? extends U> valueMapper) {
        if (CollUtil.isEmpty(list)) {
            return null;
        }
        Map<U, T> map = new HashMap<>();
        for (T t : list) {
            map.put(valueMapper.apply(t), t);
        }
        return map;
    }

    public static <T, U> Map<U, List<T>> toMapGroup(List<T> list, Function<? super T, ? extends U> valueMapper) {
        if (CollUtil.isEmpty(list)) {
            return null;
        }
        Map<U, List<T>> map = new HashMap<>();
        for (T t : list) {
            U key = valueMapper.apply(t);
            List<T> vs = map.get(key);
            if (CollUtil.isEmpty(vs)) {
                vs = new ArrayList<>();
                map.put(key, vs);
            }
            vs.add(t);
        }
        return map;
    }

    public static <T1, T2, K> void merge(List<T1> l1, Function<T1, K> k1, List<T2> l2, Function<T2, K> k2, BiConsumer<T1, T2> s1) {
        for (T1 t1 : l1) {
            K v1 = k1.apply(t1);
            for (T2 t2 : l2) {
                K v2 = k2.apply(t2);
                if (v1.equals(v2)) {
                    s1.accept(t1, t2);
                    break;
                }
            }
        }
    }

    public static <T1, T2, K> void mergeList(List<T1> l1, Function<T1, K> k1, List<T2> l2, Function<T2, K> k2, BiConsumer<T1, List<T2>> s1) {
        for (T1 t1 : l1) {
            K v1 = k1.apply(t1);
            List<T2> ts2 = new ArrayList<>();
            for (T2 t2 : l2) {
                K v2 = k2.apply(t2);
                if (v1.equals(v2)){
                    ts2.add(t2);
                }
            }
            s1.accept(t1, ts2);
        }
    }





}

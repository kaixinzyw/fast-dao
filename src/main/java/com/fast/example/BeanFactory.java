package com.fast.example;

import cn.hutool.core.bean.DynaBean;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.BooleanUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;

public class BeanFactory {

    private static Map<Class, LinkedBlockingQueue> beanFactoryMap = new HashMap<>();

    public static <T> T getBean(Class<T> clazz) {
        LinkedBlockingQueue<T> queue = beanFactoryMap.get(clazz);
        if (queue == null) {
            queue = createQueue(clazz);
        }
        T t = queue.poll();
        if (t == null) {
            return DynaBean.create(clazz, null).getBean();
        }
        return t;
    }

    private static synchronized <T> LinkedBlockingQueue<T> createQueue(Class<T> clazz) {
        LinkedBlockingQueue queue = beanFactoryMap.get(clazz);
        if (queue != null) {
            return queue;
        }
        queue = new LinkedBlockingQueue<>(10);
        beanFactoryMap.put(clazz, queue);
        return queue;
    }

    static {
        addBean();
    }

    private static void addBean() {
        ThreadUtil.excAsync(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    ThreadUtil.sleep(10);
                    for (Class clazz : beanFactoryMap.keySet()) {
                        LinkedBlockingQueue queue = beanFactoryMap.get(clazz);
                        boolean offer = queue.offer(DynaBean.create(clazz, null).getBean());
                        while (offer){
                            offer = queue.offer(DynaBean.create(clazz, null).getBean());
                        }
                    }
                }
            }
        }, false);
    }
}

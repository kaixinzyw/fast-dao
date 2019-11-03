package com.fast.mapper;

import cn.hutool.core.thread.ThreadUtil;

import java.util.concurrent.LinkedBlockingQueue;


/**
 * @author 张亚伟 https://github.com/kaixinzyw
 */
public class FastDaoAttributesBeanFactory {

    private static LinkedBlockingQueue<FastDaoThreadLocalAttributes> beanFactory = new LinkedBlockingQueue<>(20);

    static {
        addBean();
    }


    public static <T> FastDaoThreadLocalAttributes<T> getBean() {
        FastDaoThreadLocalAttributes<T> poll = beanFactory.poll();
        if (poll == null) {
            return FastDaoThreadLocalAttributes.create();
        }
        return poll;
    }

    private static void addBean() {
        ThreadUtil.excAsync(new Runnable() {
            @Override
            public void run() {
                try {
                    while (true) {
                        beanFactory.put(FastDaoThreadLocalAttributes.create());
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, false);
    }

}

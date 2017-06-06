package com.ifitmix.common.context;

/**
 * Created by zhangtao on 2017/5/11.
 */
public class SystemContextHolder {

    /**
     * 线程变量
     */
    private static ThreadLocal<SystemContext> userThread = new ThreadLocal<>();

    /**
     * 设置线程变量
     * @param systemContext
     */
    public static void put(SystemContext systemContext) {
        userThread.set(systemContext);
    }

    /**
     * 线程变量
     * @return
     */
    public static SystemContext get() {
        return userThread.get();
    }

    /**
     * 请空线程变量
     */
    public static void remove() {
        userThread.remove();
    }

}

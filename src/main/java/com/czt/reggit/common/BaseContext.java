package com.czt.reggit.common;

/**
 * 基于ThreadLocal封装工具类，用户保存和获取当前登录用户id
 * 作用范围在某一个线程之内
 * 每一次请求就是一个线程
 */
public class BaseContext {
    private static ThreadLocal<Long> threadLocal = new ThreadLocal<>();

    public static void setCurrentId(Long id){
        threadLocal.set(id);
    }

    public static Long getCurrentId(){
        return threadLocal.get();
    }
}

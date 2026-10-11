package com.dingdingcatering.context;

/**
 * 基于 ThreadLocal 的用户上下文
 * <p>
 * 用于在当前线程中存储和获取当前登录用户的 ID，
 * 避免在方法调用链中反复传递用户 ID 参数。
 * <p>
 * 使用场景：
 * 1. 登录成功后，将用户 ID 存入 ThreadLocal
 * 2. 业务层通过 getCurrentId() 获取当前用户 ID
 * 3. 请求结束后，由拦截器调用 removeCurrentId() 清理，防止内存泄漏
 */
public class BaseContext {

    private static final ThreadLocal<Long> threadLocal = new ThreadLocal<>();

    /**
     * 将当前用户 ID 存入 ThreadLocal
     *
     * @param id 用户 ID
     */
    public static void setCurrentId(Long id) {
        threadLocal.set(id);
    }

    /**
     * 获取当前线程中存储的用户 ID
     *
     * @return 用户 ID，若未设置则返回 null
     */
    public static Long getCurrentId() {
        return threadLocal.get();
    }

    /**
     * 移除当前线程中存储的用户 ID
     * 必须在请求结束时调用，防止 ThreadLocal 内存泄漏
     */
    public static void removeCurrentId() {
        threadLocal.remove();
    }

}

package com.chengtai.fashionmgmt.module.auth.context;

/**
 * 当前请求登录用户上下文。
 */
public final class LoginUserContext {
    private static final ThreadLocal<LoginUser> HOLDER = new ThreadLocal<>();

    /**
     * 私有构造方法：工具类不允许创建实例。
     */
    private LoginUserContext() {}

    /**
     * 将当前请求的登录用户绑定到线程上下文，供业务层读取。
     */
    public static void set(LoginUser user) {
        HOLDER.set(user);
    }

    /**
     * 获取当前请求线程绑定的登录用户，未登录时返回 null。
     */
    public static LoginUser get() {
        return HOLDER.get();
    }

    /**
     * 清理线程上下文中的登录用户，防止线程复用时串号。
     */
    public static void clear() {
        HOLDER.remove();
    }

    /**
     * 请求期间的登录用户快照。
     */
    public record LoginUser(Long id, String loginName, String staffName) {}
}

package com.chengtai.fashionmgmt.module.auth.context;

/**
 * 当前请求登录用户上下文。
 */
public final class LoginUserContext {
    private static final ThreadLocal<LoginUser> HOLDER = new ThreadLocal<>();

    private LoginUserContext() {}

    public static void set(LoginUser user) {
        HOLDER.set(user);
    }

    public static LoginUser get() {
        return HOLDER.get();
    }

    public static void clear() {
        HOLDER.remove();
    }

    /**
     * 请求期间的登录用户快照。
     */
    public record LoginUser(Long id, String loginName, String staffName) {}
}

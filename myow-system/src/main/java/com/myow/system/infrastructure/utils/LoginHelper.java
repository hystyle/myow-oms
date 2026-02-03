package com.myow.system.infrastructure.utils;

import cn.dev33.satoken.stp.StpUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.NamedThreadLocal;

/**
 * @author: yss
 * @date: 2026-02-03 22:09
 * @description: 登录帮助类
 */
@Slf4j
public class LoginHelper {

    private LoginHelper() {
    }

    /**
     * 存储当前请求线程的用户信息对象
     * （通常是 SysUser / LoginUserVO / UserDetail 等）
     */
    private static final ThreadLocal<Object> CURRENT_USER = new NamedThreadLocal<>("current-login-user");

    /**
     * 存储当前登录用户ID（最常用，性能更好）
     */
    private static final ThreadLocal<Long> CURRENT_USER_ID = new NamedThreadLocal<>("current-user-id");


    // ───────────────────────────────────────────────
    //  写入方法（一般在过滤器/拦截器/登录成功后调用）
    // ───────────────────────────────────────────────

    /**
     * 设置当前线程的登录用户对象
     * （推荐在登录成功后、或每次请求鉴权通过后调用）
     */
    public static void setCurrentUser(Object user) {
        if (user != null) {
            CURRENT_USER.set(user);
        }
    }

    /**
     * 只存用户ID（性能更高，推荐大多数场景使用此方式）
     */
    public static void setCurrentUserId(Long userId) {
        if (userId != null && userId > 0) {
            CURRENT_USER_ID.set(userId);
        }
    }

    /**
     * 同时存 userId 和 user 对象（最完整的方式）
     */
    public static void setCurrentLoginInfo(Long userId, Object user) {
        setCurrentUserId(userId);
        setCurrentUser(user);
    }

    // ───────────────────────────────────────────────
    //  读取方法（业务代码中最常调用的地方）
    // ───────────────────────────────────────────────
    /**
     * 获取当前登录用户的ID（最常用、最推荐的方式）
     *
     * @return 用户ID，未登录返回 null
     */
    public static Long getUserId() {
        Long id = CURRENT_USER_ID.get();
        if (id != null) {
            return id;
        }

        // 兼容直接使用 Sa-Token 的场景
        if (StpUtil.isLogin()) {
            return StpUtil.getLoginIdAsLong();
        }
        return null;
    }

    /**
     * 获取当前登录用户ID（String 格式）
     */
    public static String getUserIdStr() {
        Long id = getUserId();
        return id != null ? id.toString() : null;
    }

    /**
     * 获取当前登录用户完整对象（泛型安全版）
     */
    @SuppressWarnings("unchecked")
    public static <T> T getCurrentUser() {
        Object user = CURRENT_USER.get();
        if (user != null) {
            return (T) user;
        }

        // 降级：如果 ThreadLocal 没值，但 sa-token 认为已登录，尝试从 sa-session 取
        if (StpUtil.isLogin()) {
            return (T) StpUtil.getSession().get("currentUser");
        }
        return null;
    }

    /**
     * 获取当前登录用户（要求必须存在，否则抛异常）
     */
    public static <T> T getCurrentUserOrThrow() {
        T user = getCurrentUser();
        if (user == null) {
            throw new IllegalStateException("当前请求未登录或用户信息丢失");
        }
        return user;
    }

    /**
     * 获取当前登录用户的用户名（常用于日志、操作记录）
     * <p>需要你的用户对象有 getUsername() 方法</p>
     */
    public static String getUsername() {
        Object user = getCurrentUser();
        if (user == null) {
            return "未登录";
        }

        try {
            return (String) user.getClass().getMethod("getUsername").invoke(user);
        } catch (Exception e) {
            return "未知用户";
        }
    }

    /**
     * 当前请求是否已登录
     */
    public static boolean isLogin() {
        return getUserId() != null || StpUtil.isLogin();
    }

    // ───────────────────────────────────────────────
    //  清理方法（必须在每个请求结束时调用！）
    // ───────────────────────────────────────────────

    /**
     * 清理当前线程的登录信息（非常重要！）
     * <p>建议放在：</p>
     * <ul>
     *     <li>Filter 的 finally 块</li>
     *     <li>HandlerInterceptor 的 afterCompletion 方法</li>
     *     <li>Spring 的 OncePerRequestFilter</li>
     * </ul>
     */
    public static void clear() {
        CURRENT_USER.remove();
        CURRENT_USER_ID.remove();
    }

    // ───────────────────────────────────────────────
    //  便捷方法 - 登出时调用
    // ───────────────────────────────────────────────

    /**
     * 登出当前用户（同时清理 ThreadLocal 和 Sa-Token 会话）
     */
    public static void logout() {
        if (StpUtil.isLogin()) {
            StpUtil.logout();
        }
        clear();
    }

}

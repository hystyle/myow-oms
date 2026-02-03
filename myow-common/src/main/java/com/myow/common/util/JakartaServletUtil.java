package com.myow.common.util;

import jakarta.servlet.http.HttpServletRequest;

import java.util.Enumeration;

/**
 * @author: yss
 * @date: 2026-02-03 21:04
 * @description:
 */
public class JakartaServletUtil {

    private static final String[] HEADERS_TO_TRY = {
            "X-Forwarded-For",
            "Proxy-Client-IP",
            "WL-Proxy-Client-IP",
            "HTTP_X_FORWARDED_FOR",
            "HTTP_CLIENT_IP",
            "HTTP_X_REAL_IP",
            "X-Real-IP"
    };

    private static final String UNKNOWN = "unknown";

    /**
     * 获取当前请求的客户端真实IP地址<br/>
     * 优先级顺序：X-Forwarded-For → 其他代理头 → request.getRemoteAddr()
     * <p>
     * 支持多级代理，会取第一个有效的非内网IP（最接近客户端的IP）
     *
     * @return 客户端IP，找不到返回 "unknown"
     */
    public static String getClientIP(HttpServletRequest request) {
        if (request == null) {
            return UNKNOWN;
        }

        // 优先检查各种常见的代理头
        String ip = null;
        for (String header : HEADERS_TO_TRY) {
            ip = request.getHeader(header);
            if (isValidIp(ip)) {
                break;
            }
        }

        // 如果代理头都没有有效值，则使用最原始的方式
        if (!isValidIp(ip)) {
            ip = request.getRemoteAddr();
        }

        // X-Forwarded-For 可能包含多个IP（经过多个代理）
        // 格式示例： "203.0.113.195, 198.51.100.178, 192.168.1.1"
        // 通常取第一个（最左边）为真实客户端IP
        if (ip != null && ip.contains(",")) {
            ip = ip.split(",")[0].trim();
        }

        // 清理可能出现的空格或端口（极少数情况）
        if (ip != null) {
            ip = ip.trim();
            // 极少数情况下会出现 :port 的形式，也清理掉
            int colonIndex = ip.indexOf(':');
            if (colonIndex > 0) {
                ip = ip.substring(0, colonIndex);
            }
        }

        return ip != null && !ip.isEmpty() ? ip : UNKNOWN;
    }


    /**
     * 判断IP是否有效（非unknown、非127.0.0.1、非0:0:0:0:0:0:0:1、不为空）
     */
    private static boolean isValidIp(String ip) {
        if (ip == null || ip.isEmpty() || UNKNOWN.equalsIgnoreCase(ip)) {
            return false;
        }

        // 过滤本地回环地址
        return !"127.0.0.1".equals(ip) && !"0:0:0:0:0:0:0:1".equals(ip);
    }

    /**
     * 忽略大小写获得请求header中的信息
     *
     * @param request        请求对象{@link HttpServletRequest}
     * @param nameIgnoreCase 忽略大小写头信息的KEY
     * @return header值
     */
    public static String getHeaderIgnoreCase(HttpServletRequest request, String nameIgnoreCase) {
        final Enumeration<String> names = request.getHeaderNames();
        String name;
        while (names.hasMoreElements()) {
            name = names.nextElement();
            if (name != null && name.equalsIgnoreCase(nameIgnoreCase)) {
                return request.getHeader(name);
            }
        }

        return null;
    }
}

package com.jnshu.util;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * @ClassName cookieUtil
 * @decription cookie工具类
 * @Author 韦延伦
 * @Date 2020/8/13 14:50
 * @Version 1.0
 */
public class CookieUtil {
    /**
     * @param response 携带cookie的响应
     * @param name     cookie名
     * @param value    cookie值
     * @param maxAge   cookie的最大存在时间
     */
    public static void setCookie(HttpServletResponse response, String name, String value, int maxAge) {
        Cookie cookie = new Cookie(name, value);
        cookie.setMaxAge(maxAge);
        cookie.setPath("/");
        try {
            cookie.setValue(URLEncoder.encode(value, "utf-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        response.addCookie(cookie);
    }

    /**
     * 获取cookie的方法
     * @param cookies 从客户端取得cookie数组
     * @param name
     * @return
     */
    public static Cookie getCookie(Cookie[] cookies, String name) {
        if (cookies == null) {
            return null;
        } else {
            for (Cookie cookie : cookies) {
                if (name.equals(cookie.getName())) {
                    return cookie;
                }
            }
        }
        return null;
    }
}

package com.jnshu.util;

import com.jnshu.service.UserService;
import io.jsonwebtoken.Claims;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLDecoder;

/**
 * @ClassName MyIntercept
 * @Description 拦截器
 * @Author 韦延伦
 * @Date 2020/8/12 19:56
 * @Version 1.0
 */
public class MyIntercept implements HandlerInterceptor {
    @Autowired
    UserService userService;
    private static Logger logger = LoggerFactory.getLogger(MyIntercept.class);
    //密钥
    private static final String SECRE="1234";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {

        //从请求中获取cookie
        Cookie cookie = CookieUtil.getCookie(request.getCookies(), "token");
        //判断客户端是否有cookie
        if (cookie != null) {
            //从cookie得到token
            String value = URLDecoder.decode(cookie.getValue(), "utf-8");
            logger.info("从客户端得到的taken：" + value);
            //解密token
            Claims token = JJWTUtil.parseJWT(value, SECRE);
            if(token!=null){
                return true;
            }else {
                return false;
            }
        } else {
            logger.info("用户没有登录");
            //如果没有检测到登录状态就重定向到登录界面
            response.sendRedirect(request.getContextPath() + "/loginpage");
            return false;
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {

    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) {

    }
}

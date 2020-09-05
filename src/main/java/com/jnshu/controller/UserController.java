package com.jnshu.controller;

import com.jnshu.pojo.User;
import com.jnshu.service.UserService;
import com.jnshu.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @ClassName UserController
 * @Description
 * @Author 韦延伦
 * @Date 2020/8/11 21:44
 * @Version 1.0
 */
@Controller
public class UserController {
    private static Logger logger = LoggerFactory.getLogger(UserController.class);
    @Autowired
    UserService userService;
    @Autowired
    AliUtil aliUtil;
    @Autowired
    RedisService redisService;
    private static final String KEY="1234";

    @RequestMapping("/loginpage")
    public String user() {
        return "login";
    }

    @RequestMapping("/phone/registpage")
    public String regist() {
        return "phoneRegist";
    }

    @RequestMapping("/mail/registpage")
    public String registPhone() {

        return "mailRegist";
    }

    @RequestMapping("/u/img/page")
    public String uploadImg() {
        return "upload";
    }

    /**
     * 用户登录接口
     *
     * @return
     */
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public String doLogin(HttpServletRequest request, Model model, HttpServletResponse response) throws UnsupportedEncodingException {
        //获取账号密码
        String username = request.getParameter("username");
        String password = request.getParameter("pwd");

        User user = userService.findUser(username);
        if (user != null) {
            if (DigestUtils.md5DigestAsHex(password.getBytes("UTF-8")).equals(user.getPassword())){
                //获取token
                String token=JJWTUtil.getJWT(String.valueOf(user.getId()),user.getUsername(),new Date(), KEY);
                //把token装到cookie中发送到浏览器
                CookieUtil.setCookie(response,"token",token,60*3);
                return "redirect:/u/profession";
            } else {
                model.addAttribute("msg", "密码错误");
                return "login";
            }
        }
        return "login";
    }

    /**
     * 注销接口
     *
     * @param response
     * @return
     */
    @RequestMapping("/logout")
    public String logOut(HttpServletResponse response) {
        Cookie cookie = new Cookie("token", null);
        cookie.setMaxAge(0);
        cookie.setPath("/");
        response.addCookie(cookie);
        return "login";
    }

    /**
     * 手机注册接口
     *
     * @param
     * @param
     * @return
     */
    @RequestMapping(value = "/phone/regist", method = RequestMethod.POST)
    public String doPhoneRegist(HttpServletRequest request, Model model) throws UnsupportedEncodingException {
        //获取表单信息
        String username = request.getParameter("username");
        String password = request.getParameter("pwd");
        String phone = request.getParameter("phone");
        String code = request.getParameter("code");
        User user1 = userService.findUser(username);
        User user2 = userService.findUser(phone);
        if (redisService.get(phone) != null) {
            if (code.equals(redisService.get(phone))) {
                /**
                 * 手机注册
                 * 判断用户名是否已经存在和比对验证码是否正确
                 */
                if (user1 == null && user2 == null) {
                    User userPhone = new User();
                    userPhone.setPhone(phone);
                    userPhone.setUsername(username);
                    //md5加密密码
                    String md5Password=DigestUtils.md5DigestAsHex(password.getBytes("UTF-8"));
                    userPhone.setPassword(md5Password);
                    userService.registUser(userPhone);
                    return "login";
                } else {
                    model.addAttribute("msg", "该用户名或者手机号已经被注册");
                    return "phoneRegist";
                }
            } else {
                model.addAttribute("msgCode", "输入的验证码错误");
                return "phoneRegist";
            }
        } else {
            model.addAttribute("magCode", "验证码已经过期");
            return "phoneRegist";
        }

    }

    /**
     * 邮箱注册接口
     *
     * @param
     * @param
     * @return
     */
    @RequestMapping(value = "/mail/regist", method = RequestMethod.POST)
    public String domailRegist(HttpServletRequest request, Model model) throws UnsupportedEncodingException {
        //获取表单信息
        String username = request.getParameter("username");
        String password = request.getParameter("pwd");
        String mail = request.getParameter("mail");
        String code = request.getParameter("code");
        User user1 = userService.findUser(username);
        User user2 = userService.findUser(mail);
        if (redisService.get(mail) != null) {
            if (code.equals(redisService.get(mail))) {
                /**
                 * 手机注册
                 * 判断用户名是否已经存在和比对验证码是否正确
                 */
                if (user1 == null && user2 == null) {
                    User userMail = new User();
                    userMail.setEmail(mail);
                    String md5Password=DigestUtils.md5DigestAsHex(password.getBytes("UTF-8"));
                    userMail.setUsername(username);
                    userMail.setPassword(md5Password);
                    userService.registUser(userMail);
                    return "login";
                } else {
                    model.addAttribute("msg", "用户名或者邮箱已存在");
                    return "mailRegist";
                }
            } else {
                model.addAttribute("msgCode", "验证码输入错误");
                return "mailRegist";
            }
        } else {
            model.addAttribute("magCode", "验证码已经过期");
            return "mailRegist";
        }
    }

    /**
     * 发送手机验证码接口
     *
     * @param phone 手机号码
     * @return
     */
    @RequestMapping(value = "/phone/code", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> sentPhoneCode(@RequestParam(value = "phone", required = false) String phone) {
        Map<String, Object> map = new HashMap<>();
        //验证手机号码格式
        String phoneRegex = "^1[3|4|5|7|8][0-9]\\d{4,8}$";
        Pattern p = Pattern.compile(phoneRegex);
        Matcher m = p.matcher(phone);
        boolean isPhone = m.matches();
        //判断手机号格式，如果不对给页面显示错误信息
        if (!isPhone) {
            logger.info("手机格式不对");
            map.put("msg", "手机格式不对");
        } else {
            aliUtil.sendPhoneCode(phone, map);
        }
        return map;
    }

    /**
     * 发送邮箱验证码接口
     *
     * @param mail 邮箱号码
     * @return
     */
    @RequestMapping(value = "/mail/code", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> sentMailCode(@RequestParam(value = "mail", required = false) String mail) {
        Map<String, Object> map = new HashMap<>();
        //验证邮箱
        String mailRegex = "^\\s*\\w+(?:\\.{0,1}[\\w-]+)*@[a-zA-Z0-9]+(?:[-.][a-zA-Z0-9]+)*\\.[a-zA-Z]+\\s*$";
        Pattern p1 = Pattern.compile(mailRegex);
        Matcher m1 = p1.matcher(mail);
        boolean isMail = m1.matches();
        //判断邮箱号格式，如果不对给页面显示错误信息
        if (!isMail) {
            logger.info("邮箱格式不对");
            map.put("msg", "邮箱格式错误");
        } else {
            aliUtil.sendMailCode(mail, map);
        }
        return map;
    }

    /**
     * 上传头像接口
     * @param file
     * @param model
     * @param request
     * @return
     */
    @RequestMapping(value = "u/img",method = RequestMethod.POST)
    public String uploadImg(@RequestParam MultipartFile file,Model model,HttpServletRequest request){
        try {
            String head = userService.updateHead(file, request.getParameter("username"));
            model.addAttribute("data", head);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ImgException e) {
            e.printStackTrace();
        }
        return "upload";
    }
}

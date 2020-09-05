package com.jnshu.service;

import com.jnshu.pojo.User;
import com.jnshu.util.ImgException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * @ClassName AccountService
 * @Description
 * @Author 韦延伦
 * @Date 2020/8/12 19:39
 * @Version 1.0
 */
public interface UserService {
    /**
     * 注册用户
     * @param user
     * @return
     */
    int registUser(User user);

    /**
     * 查找用户
     * @param username
     * @return
     */
    User findUser(String username);

    String updateHead(MultipartFile file, String username) throws IOException, ImgException;

}

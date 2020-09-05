package com.jnshu.dao;

import com.jnshu.pojo.User;

/**
 * @ClassName UserMapper
 * @Description
 * @Author 韦延伦
 * @Date 2020/8/12 19:34
 * @Version 1.0
 */
public interface UserMapper {
    /**
     * 注册用户
     * @param account
     * @return
     */
    int registUser(User account);

    /**
     * 查找用户
     * @param username 用户名称
     * @return
     */
    User findUser(String username);

    boolean updateHead(User user);


}
